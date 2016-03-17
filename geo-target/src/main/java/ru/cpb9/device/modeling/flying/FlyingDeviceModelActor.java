package ru.cpb9.device.modeling.flying;

import akka.actor.ActorRef;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import gov.nasa.worldwind.geom.Angle;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.ModelActor;
import ru.cpb9.device.modeling.ModelTick;
import ru.cpb9.fsm.Fsm;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.akka.messages.TmMessageValues;
import ru.mipt.acsl.*;
import ru.mipt.acsl.device.modeling.KnownTmMessages;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelActor extends ModelActor
{

    private static final double M_PI_4 = Math.PI / 4.;
    private final FlyingDeviceModelExchangeController exchangeController;
    private final Coordinates coordinates;
    private Angle pitch = Angle.fromDegrees(45.);
    private Angle heading = Angle.fromDegrees(-75.);
    private Angle roll = Angle.fromDegrees(-15.);
    private double throttle = 25.;
    private double accuracyM = 0.;
    private Random random = new Random();
    private FlyingMode mode = FlyingMode.RouteFlying;
    @NotNull
    private FlyingDeviceModelingParameters modelingParameters;
    @NotNull
    private Fsm<FlyingMode, Command> modesStateMachine;
    private double tickS = 1.;
    private long tick = 0;
    private boolean connectionAvailable = true;
    @NotNull
    private Optional<Orientation> freeFlyingTargetOrientation = Optional.empty();
    private double headingDeg = 0.;
    @NotNull
    private Optional<Coordinates> freeFlyingTargetCoordinates = Optional.empty();
    @NotNull
    private List<Route> routes = new ArrayList<>();
    private double speedMps;
    private int nextPoint = 0;
    private int activeRoute;
    private double accelerationMpsps = 0.;
    private static final double METERS_IN_DEGREE = 111319.9;
    private double waypointCheckDistanceM = 50.;
    private double rollDeg = 0.;
    private double pitchDeg = 0.;
    private double batteryLevelPer = random(50., 90.);
    private double shift = random(0., 360.);
    private double signalLevelPer = 0.;

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController, @NotNull ActorRef tmServer)
    {
        this(exchangeController, tmServer, new Coordinates(43.01770, 42.69012, 200.));
    }

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController, @NotNull ActorRef tmServer,
                                  @NotNull Coordinates coordinates)
    {
        this(exchangeController, tmServer, coordinates, new Route(1, SimplifiedRouteKind.AUTO, true, 0, 50, "Default route",
                Lists.newArrayList(
                        new RoutePoint(43.10098, 42.87415, 500., 600., 1),
                        new RoutePoint(43.42899, 42.11609, 500., 600., 1),
                        new RoutePoint(43.60824, 42.93457, 500., 600., 8),
                        new RoutePoint(43.92955, 42.22046, 500., 600., 1),
                        new RoutePoint(44.15068, 42.88513, 500., 600., 1),
                        new RoutePoint(41.91863, 43.76953, 500., 600., 1))),
                FlyingDeviceModelingParameters.newBuilder().build());
    }

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController,
                                  @NotNull ActorRef tmServer,
                                  @NotNull Coordinates coordinates, @NotNull Route route)
    {
        this(exchangeController, tmServer, coordinates, route, FlyingDeviceModelingParameters.newBuilder().build());
    }

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController,
                                  @NotNull ActorRef tmServer,
                                  @NotNull Coordinates coordinates, @NotNull Route route,
                                  @NotNull FlyingDeviceModelingParameters modelingParameters)
    {
        super(tmServer);
        this.exchangeController = exchangeController;
        this.coordinates = coordinates;
        this.routes.add(route);
        this.activeRoute = 0;
        this.modelingParameters = modelingParameters;
        getContext().system().scheduler().schedule(FiniteDuration.Zero(), new FiniteDuration(100, TimeUnit.MILLISECONDS),
                self(), new ModelTick(), getContext().dispatcher(), self());
    }

    @Override
    protected void tick()
    {
        if (FlyingMode.TakingOff == mode && coordinates.getAltitudeM() > modelingParameters.getTakingOffAltitudeM())
        {
            Preconditions.checkState(modesStateMachine.activate(new Command("Navigation.freeFlying")));
        }
        if (FlyingMode.Landing == mode && coordinates.getAltitudeM() < .1)
        {
            Preconditions.checkState(modesStateMachine.activate(new Command("Navigation.endLanding")));
        }
        updateAccuracy();
        updateConnectionAvailable();
        updateHeading();
        updateAcceleration();
        updateSpeed();
        advanceCoords();
        updateRoll();
        updatePitch();
        updateAltitude();
        updateBatteryLevel();
        updateSignalLevel();

        tick++;

        sendTm();
    }

    private void updateSignalLevel()
    {
        double tickDeg = tick + shift + random(0., 9.);
        double tickRad = Math.toRadians(tickDeg);
        signalLevelPer = Math.abs(Math.sin(tickRad) * Math.cos(2 * tickRad) + .4 * Math.cos(tickRad * 64)) / 1.4 * 100.;
    }

    private void updateBatteryLevel()
    {
        batteryLevelPer -= throttle / 5000. * tickS;
    }

    private void updateAltitude()
    {
        if (isBatteryLow() || FlyingMode.EngineReady == mode)
        {
            coordinates.addAltitudeM(-10. * tickS);
        }
        else
        {
            switch (mode)
            {
                case TakingOff:
                    coordinates.addAltitudeM(10. * tickS);
                    break;
                case Landing:
                    coordinates.addAltitudeM(-20. * tickS);
                    break;
                case FreeFlying:
                case RouteFlying: {
                    if (hasWaypoint())
                    {
                        double waypointAltitudeM = getWaypointCoordinates().getAltitudeM();
                        if (waypointAltitudeM == 0.)
                        {
                            waypointAltitudeM = .00001;
                        }
                        double altitudeDeltaPer = (waypointAltitudeM - coordinates.getAltitudeM()) / waypointAltitudeM;
                        coordinates.addAltitudeM(altitudeDeltaPer * speedMps * .05 * tickS);
                    }
                    break;
                }
                default:
                    break;
            }
        }
        coordinates.setAltitudeM(Math.max(coordinates.getAltitudeM(), 0.));
    }

    private boolean isBatteryLow()
    {
        return batteryLevelPer < .01;
    }

    private void updatePitch()
    {
        double tickDeg = tick;
        double tickRad = Math.toRadians(tickDeg);
        pitchDeg = (Math.cos(tickRad / 4) * .5 + Math.sin(tickRad / 4) * .2) * 20.;
    }

    private void updateRoll()
    {
        Optional<Double> targetRollDeg = getTargetRollDeg();
        if (FlyingMode.Waiting == mode)
        {
            rollDeg = M_PI_4;
        }
        else if (targetRollDeg.isPresent())
        {
            rollDeg += (targetRollDeg.get() - rollDeg) * random(0.1, 0.9);
        }
    }

    private Optional<Double> getTargetRollDeg()
    {
        if (mode == FlyingMode.FreeFlying && freeFlyingTargetOrientation.isPresent())
        {
            return Optional.of(freeFlyingTargetOrientation.get().getHeadingDeg());
        }
        else if (hasWaypoint())
        {
            Optional<Double> targetHeadingDeg = getTargetHeadingDeg();
            Preconditions.checkState(targetHeadingDeg.isPresent(), "target heading must be present");
            return Optional.of(computeTargetRollDegForTargetHeadingDeg(targetHeadingDeg.get()));
        }
        return Optional.empty();
    }

    private double computeTargetRollDegForTargetHeadingDeg(double targetHeadingDeg)
    {
        double headingWaypointAnglesDeltaDeg = anglesDeltaDeg(headingDeg, targetHeadingDeg);
        double rollRad = Math.max(Math.min(Math.toRadians(headingWaypointAnglesDeltaDeg), M_PI_4), -M_PI_4);
        return normalizeRollDeg(Math.toDegrees(Math.tan(rollRad)));
    }

    private double normalizeRollDeg(double rollDeg)
    {
        return normalizeAngle180Deg(rollDeg);
    }

    private void advanceCoords()
    {
        advanceLatitudeLongitudeDeg(coordinates, headingDeg, speedMps * tickS);
        if (hasWaypoint())
        {
            double prevLatitudeDeg = coordinates.getLatitudeDeg();
            double prevLongitudeDeg = coordinates.getLongitudeDeg();
            advanceWaypoint(prevLatitudeDeg, prevLongitudeDeg);
        }
    }

    private void advanceWaypoint(double prevLatitudeDeg, double prevLongitudeDeg)
    {
        boolean isReturning = FlyingMode.Returning == mode;
        while (true)
        {
            RoutePoint currentWaypoint = getRouteWaypoint();
            double waypointLatDeg = currentWaypoint.getLatitudeDeg();
            double waypointLongDeg = currentWaypoint.getLongitudeDeg();
            double minDistanceToWaypointM = Math.min(Math.min(degToM(
                                    distanceToLineSegment(prevLatitudeDeg, prevLongitudeDeg, coordinates.getLatitudeDeg(),
                                            coordinates.getLongitudeDeg(), waypointLatDeg, waypointLongDeg)),
                            degToM(distance(prevLatitudeDeg, prevLongitudeDeg, waypointLatDeg, waypointLongDeg))),
                    degToM(distance(coordinates.getLatitudeDeg(), coordinates.getLongitudeDeg(), waypointLatDeg, waypointLongDeg)));
            if (minDistanceToWaypointM > waypointCheckDistanceM)
            {
                break;
            }
            if (routes.size() <= activeRoute)
            {
                // Нет маршрута или неправильный активный маршрут
                break;
            }
            Route activeRoute = routes.get(this.activeRoute);
            if (isReturning)
            {
                if (nextPoint > 0)
                {
                    nextPoint -= 1;
                }
                else if (activeRoute.isRing())
                {
                    nextPoint = activeRoute.size() - 1;
                }
                else
                {
                    break;
                }
            }
            else
            {
                if (nextPoint < activeRoute.size() - 1)
                {
                    nextPoint += 1;
                }
                else if (activeRoute.isRing())
                {
                    nextPoint = 0;
                }
                else
                {
                    break;
                }
            }
        }
    }

    private static double degToM(double degs)
    {
        return degs * METERS_IN_DEGREE;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2.) + Math.pow(y2 - y1, 2.));
    }

    private static double distanceToLineSegment(double x1, double y1, double x2, double y2, double pointX, double pointY)
    {
        double diffX = x2 - x1;
        double diffY = y2 - y1;
        if ((diffX == 0) && (diffY == 0))
        {
            diffX = pointX - x1;
            diffY = pointY - y1;
            return Math.sqrt(diffX * diffX + diffY * diffY);
        }

        double t = ((pointX - x1) * diffX + (pointY - y1) * diffY) / (diffX * diffX + diffY * diffY);

        if (t < 0)
        {
            //point is nearest to the first point i.e x1 and y1
            diffX = pointX - x1;
            diffY = pointY - y1;
        }
        else if (t > 1)
        {
            //point is nearest to the end point i.e x2 and y2
            diffX = pointX - x2;
            diffY = pointY - y2;
        }
        else
        {
            //if perpendicular line intersect the line segment.
            diffX = pointX - (x1 + t * diffX);
            diffY = pointY - (y1 + t * diffY);
        }

        //returning shortest distance
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static void advanceLatitudeLongitudeDeg(@NotNull Coordinates coordinates, double angleDeg, double speedMps)
    {
        double latDeltaM = Math.cos(Math.toRadians(angleDeg)) * speedMps;
        double longDeltaM = Math.sin(Math.toRadians(angleDeg)) * speedMps;
        double latDeltaDeg = mToDeg(latDeltaM);
        double longDeltaDeg = mToDeg(longDeltaM);
        coordinates.addLatitudeDeg(latDeltaDeg);
        coordinates.addLongitudeDeg(longDeltaDeg);
    }

    private static double mToDeg(double meters)
    {
        return meters / METERS_IN_DEGREE;
    }

    private void updateSpeed()
    {
        speedMps += accelerationMpsps * tickS;
        speedMps = limit(speedMps, 0., getMaxSpeedMps());
    }

    private void updateAcceleration()
    {
        double maxAccelerationMpsps = modelingParameters.getMaxAccelerationMpsps();
        switch(mode)
        {
        case Waiting:
            accelerationMpsps = 0.;
            throttle = 30.;
            break;
        case TakingOff:
            accelerationMpsps = maxAccelerationMpsps;
            throttle = 100.;
            break;
        case Landing:
            accelerationMpsps = speedMps > 0. ? -maxAccelerationMpsps : 0.;
            throttle = 100.;
            break;
        case FreeFlying:
        case RouteFlying:
            accelerationMpsps = maxAccelerationMpsps;
            throttle = 100.;
            if (hasWaypoint())
            {
                double waypointSpeedMps = FlyingMode.FreeFlying == mode ? getMaxSpeedMps() : getRouteWaypoint().getSpeedMps();
                if (waypointSpeedMps == 0.)
                {
                    waypointSpeedMps = .000001;
                }
                double speedPer = speedMps / waypointSpeedMps;
                accelerationMpsps += ((speedPer > .9) ? (speedPer > 1.1 ? - maxAccelerationMpsps : .01 * maxAccelerationMpsps) : maxAccelerationMpsps) * tickS;
                accelerationMpsps = limit(accelerationMpsps, - maxAccelerationMpsps, maxAccelerationMpsps);
                throttle = accelerationMpsps < 0.? 0.
                        : Math.min(speedMps / getMaxSpeedMps() * 70.
                                + Math.abs(accelerationMpsps) / maxAccelerationMpsps * 50., 100.);
            }
            break;
        default:
            accelerationMpsps = -maxAccelerationMpsps;
            throttle = 0;
            break;
        }
    }

    private void updateHeading()
    {
        Optional<Double> targetHeadingDeg = getTargetHeadingDeg();
        if (FlyingMode.Waiting == mode)
        {
            headingDeg += modelingParameters.getRotateSpeedDegps() * tickS;
        }
        else if (targetHeadingDeg.isPresent())
        {
            double waypointAngleDeg = targetHeadingDeg.get();
            double headingWaypointAnglesDeltaDeg = anglesDeltaDeg(headingDeg, waypointAngleDeg);
            headingDeg += (headingWaypointAnglesDeltaDeg < 0. ? -1. : 1.)
                    * Math.min(Math.abs(headingWaypointAnglesDeltaDeg),
                modelingParameters.getRotateSpeedDegps() * (1. - speedMps / getMaxSpeedMps() * .3))
            * tickS;
        }
        headingDeg = normalizeHeadingDeg(headingDeg);
    }

    private static double normalizeHeadingDeg(double headingDeg)
    {
        return normalizeAngle180Deg(headingDeg);
    }

    private double getMaxSpeedMps()
    {
        return modelingParameters.getMaxSpeedMps();
    }

    private static double anglesDeltaDeg(double fromDeg, double toDeg)
    {
        return normalizeAngle180Deg(toDeg - fromDeg);
    }

    private static double normalizeAngle180Deg(double angleDeg)
    {
        angleDeg = angleDeg % 360.;
        return angleDeg > 180. ? angleDeg - 360. : (angleDeg < -180. ? angleDeg + 360. : angleDeg);
    }

    @NotNull
    private Optional<Double> getTargetHeadingDeg()
    {
        if (mode == FlyingMode.FreeFlying && freeFlyingTargetOrientation.isPresent())
        {
            return Optional.of(freeFlyingTargetOrientation.get().getHeadingDeg());
        }
        else if (hasWaypoint())
        {
            return Optional.of(normalizeHeadingDeg(angleToWaypointDeg(getWaypointCoordinates())));
        }
        return Optional.empty();
    }

    private double angleToWaypointDeg(@NotNull Coordinates waypointCoordinates)
    {
        return angleFromCoordsDeg(coordinates.getLatitudeDeg(), coordinates.getLongitudeDeg(),
                waypointCoordinates.getLatitudeDeg(),
                waypointCoordinates.getLongitudeDeg());
    }

    private static double angleFromCoordsDeg(double lat1Deg, double long1Deg, double lat2Deg, double long2Deg)
    {
        return Math.toDegrees(angleFromCoordsRad(Math.toRadians(lat1Deg), Math.toRadians(long1Deg),
                Math.toRadians(lat2Deg), Math.toRadians(long2Deg)));
    }

    private static double angleFromCoordsRad(double lat1Rad, double long1Rad, double lat2Rad, double long2Rad)
    {
        double longDeltaRad = long2Rad - long1Rad;
        return Math.atan2(Math.sin(longDeltaRad) * Math.cos(lat2Rad),
                Math.cos(lat1Rad) * Math.sin(lat2Rad) - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(longDeltaRad));
    }

    @NotNull
    private Coordinates getWaypointCoordinates()
    {
        if (mode == FlyingMode.FreeFlying)
        {
            Preconditions.checkState(freeFlyingTargetCoordinates.isPresent(), "free flying coordinates not set");
            return freeFlyingTargetCoordinates.get();
        }
        return getRouteWaypoint();
    }

    @NotNull
    private RoutePoint getRouteWaypoint()
    {
        return getActiveRoute().getPoint(nextPoint);
    }

    private Route getActiveRoute()
    {
        Preconditions.checkState(routes.size() > activeRoute, "invalid active route");
        return routes.get(activeRoute);
    }

    private boolean hasWaypoint()
    {
        if (mode == FlyingMode.FreeFlying)
        {
            return freeFlyingTargetCoordinates.isPresent();
        }
        else
        {
            return (FlyingMode.RouteFlying == mode || FlyingMode.Returning == mode) && getActiveRoute().size() > nextPoint;
        }
    }

    private void updateConnectionAvailable()
    {
        if (random.nextDouble() > Math.min(.99, modelingParameters.isSignalBad() ? 1. : (connectionAvailable ? .1 : .5)) * tickS * .02)
        {
            connectionAvailable = !connectionAvailable;
        }
    }

    private void updateAccuracy()
    {
        double limit = Math.min(0.99, tickS);
        if (random.nextDouble() > limit)
        {
            accuracyM = Math.abs(Math.sin(Math.toRadians(tick + random(0., limit * 360.)))) * 1000.;
        }
    }

    private double random(double from, double to)
    {
        return random.nextDouble() * (to - from) + from;
    }

    private static double limit(double value, double min, double max)
    {
        return Math.min(Math.max(min, value), max);
    }

    private static byte limit(byte value, byte min, byte max)
    {
        return (byte) Math.min(Math.max(min, value), max);
    }

    private void sendTm()
    {
        DeviceGuid deviceGuid = exchangeController.getDeviceGuid().get();
        double latDeg = coordinates.getLatitudeDeg();
        double lonDeg = coordinates.getLongitudeDeg();
        TmMessageValues<MotionComponent.AllMessage> message = new TmMessageValues<>(deviceGuid,
                KnownTmMessages.MotionAll(), new MotionComponent.AllMessage(latDeg,
                lonDeg, coordinates.getAltitudeM(), accuracyM, speedMps, pitch.degrees, heading.degrees, roll.degrees, (short) throttle));
        tmServer.tell(message, getSelf());
        TmMessageValues<DeviceComponent.AllMessage> deviceMessage = new TmMessageValues<>(deviceGuid,
                KnownTmMessages.DeviceAll(), new DeviceComponent.AllMessage((short) batteryLevelPer, (short) signalLevelPer, 0, (short) 0));
        tmServer.tell(deviceMessage, getSelf());


        RoutePoint dogPoint = new RoutePoint(latDeg, lonDeg, coordinates.getAltitudeM(), speedMps, 1);
        if ((mode == FlyingMode.RouteFlying || mode == FlyingMode.Returning) && hasWaypoint())
        {
            dogPoint = getRouteWaypoint();
            double distToWaypointM = distanceToWaypointM(dogPoint);
            double k = speedMps * 10. / distToWaypointM;
            dogPoint.setLatitudeDeg(latDeg + (dogPoint.getLatitudeDeg() - latDeg) * k);
            dogPoint.setLongitudeDeg(lonDeg + (dogPoint.getLongitudeDeg() - lonDeg) * k);
        }
        TmMessageValues<DogPointComponent.AllMessage> dogPointMessage = new TmMessageValues<>(deviceGuid,
                KnownTmMessages.DogPointAll(), new DogPointComponent.AllMessage((float) dogPoint.getLatitudeDeg(),
                (float) dogPoint.getLongitudeDeg(), (float) dogPoint.getAltitudeM()));
        tmServer.tell(dogPointMessage, getSelf());

        TmMessageValues<ModeComponent.AllMessage> modeMessage = new TmMessageValues<>(deviceGuid, KnownTmMessages.ModeAll(),
                new ModeComponent.AllMessage(mode.getCode()));
        tmServer.tell(modeMessage, getSelf());

        /*TmMessageValues<RouteComponent.AllMessage> routeMessage = new TmMessageValues<>(deviceGuid, KnownTmMessages.ROUTE_ALL,
                new RouteComponent.AllMessage(nextPoint, r))*/
    }

    private double distanceToWaypointM(@NotNull RoutePoint waypoint)
    {
        return degToM(distance(waypoint.getLatitudeDeg(), waypoint.getLongitudeDeg(), coordinates.getLatitudeDeg(),
                coordinates.getLongitudeDeg()));
    }

    private class Command
    {
        @NotNull
        private final String name;

        public Command(@NotNull String name)
        {
            this.name = name;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == null || !(o instanceof Command))
            {
                return false;
            }
            return name.equals(((Command)o).name);
        }

        @Override
        public int hashCode()
        {
            return name.hashCode();
        }
    }
}
