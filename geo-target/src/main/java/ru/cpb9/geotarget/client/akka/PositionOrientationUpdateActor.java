package ru.cpb9.geotarget.client.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;
import ru.cpb9.geotarget.model.Device;
import ru.cpb9.geotarget.DeviceRegistry;
import ru.cpb9.geotarget.model.PositionOrientation;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cpb9.geotarget.model.TmParameter;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexander Kuchuk
 */


public class PositionOrientationUpdateActor extends UntypedActor
{
    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(PositionOrientationUpdateActor.class);

    @NotNull
    private final DeviceRegistry deviceRegistry;

    public PositionOrientationUpdateActor(@NotNull DeviceRegistry deviceRegistry) {
        this.deviceRegistry = deviceRegistry;
    }

    @Override
    public void preStart()
    {
        deviceRegistry.getDevices().addListener((Observable observable) -> {

            for (DeviceExchangeController device : deviceRegistry.getDevices())
            {
                // TODO
            }
        });
    }


    @Override
    public void onReceive(Object o) throws Exception
    {
        try
        {
            // TODO
        }
        catch (Throwable e)
        {
            LOG.error("Actor failed: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    private void updatePosition(List<TmParameter> tmParamList, ObservableList<PositionOrientation> devicePos)
    {
        PositionOrientation posOrient = new PositionOrientation();

        Stream<TmParameter> navMotionStream = tmParamList.stream().filter(
                tmparam -> "Navigation.Motion".equals(tmparam.getTrait()));
        double latitude = Double.parseDouble(
                navMotionStream.filter(tmparam -> "latitude".equals(tmparam.getStatus())).findAny().get().getValue());
        double longitude = Double.parseDouble(
                navMotionStream.filter(tmparam -> "longitude".equals(tmparam.getStatus())).findAny().get().getValue());
        double altitude = Double.parseDouble(
                navMotionStream.filter(tmparam -> "altitude".equals(tmparam.getStatus())).findAny().get().getValue());
        posOrient.setPitch(Double.parseDouble(
                navMotionStream.filter(tmparam -> "pitch".equals(tmparam.getStatus())).findAny().get().getValue()));
        posOrient.setRoll(Double.parseDouble(
                navMotionStream.filter(tmparam -> "roll".equals(tmparam.getStatus())).findAny().get().getValue()));
        posOrient.setHeading(Double.parseDouble(
                navMotionStream.filter(tmparam -> "heading".equals(tmparam.getStatus())).findAny().get().getValue()));

        posOrient.setPosition(new Position(Angle.fromDegreesLatitude(latitude), Angle.fromDegreesLongitude(longitude), altitude));
        devicePos.add(posOrient);
    }

}
