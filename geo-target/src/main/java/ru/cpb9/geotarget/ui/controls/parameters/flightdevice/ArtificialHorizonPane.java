package ru.cpb9.geotarget.ui.controls.parameters.flightdevice;

import ru.cpb9.geotarget.DeviceRegistry;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Kuchuk
 */

/**
 * Only god and Sergey Kartashev knows what this code do.
 * I tried..
 */
public class ArtificialHorizonPane extends Pane
{
    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(ArtificialHorizonPane.class);

    private final DeviceRegistry deviceRegistry;

    public ArtificialHorizonPane(DeviceRegistry deviceRegistry)
    {
        this.deviceRegistry = deviceRegistry;

        ArtificialHorizon artificialHorizon = new ArtificialHorizon();
        artificialHorizon.widthProperty().bind(widthProperty());
        artificialHorizon.heightProperty().bind(heightProperty());

        getChildren().add(artificialHorizon);
    }

    private class ArtificialHorizon extends Canvas
    {
        private static final double BASE_WIDTH = 300;
        private static final double BASE_PITCH_SCALE = 5;
        private static final int BASE_PITCH_SCALE_INTERVAL = 5;
        private static final double NORMAL_FONT_SIZE = 0.045;
        private final Color INDICATOR_BACKGROUND_COLOR = Color.color(0, 0, 0, 0.15);
        

        ArtificialHorizon()
        {
            final GraphicsContext gc = getGraphicsContext2D();

            SimpleStringProperty pitchProperty = new SimpleStringProperty("0");
            SimpleStringProperty rollProperty = new SimpleStringProperty("0");
            SimpleStringProperty headingProperty = new SimpleStringProperty("0");
            SimpleStringProperty speedProperty = new SimpleStringProperty("0");
            SimpleStringProperty altitudeProperty = new SimpleStringProperty("0");

            SimpleStringProperty nextPointProperty = new SimpleStringProperty("0");
            SimpleStringProperty routesCountPointProperty = new SimpleStringProperty("0");
            SimpleStringProperty activeRouteProperty = new SimpleStringProperty("0");
            SimpleStringProperty targetSpeedProperty = new SimpleStringProperty("0");
            SimpleStringProperty targetAltitudeProperty = new SimpleStringProperty("0");
            SimpleStringProperty targetHeadingProperty = new SimpleStringProperty("0");

            deviceRegistry.activeDeviceProperty().addListener(observable ->
            {
//                TraitInfo traitNavigationMotion = deviceRegistry.activeDeviceProperty().getValue().getTraitOrNull(Trait.NAVIGATION_MOTION);
//                TraitInfo traitNavigationRoutes = deviceRegistry.activeDeviceProperty().getValue().getTraitOrNull(Trait.NAVIGATION_ROUTES);
//                TraitInfo traitRoutePoint = null;
//
//                if (traitNavigationRoutes != null)
//                {
//                    nextPointProperty.bind(traitNavigationRoutes.getStatusMap().get("nextPoint").valueProperty());
//                    routesCountPointProperty.bind(traitNavigationRoutes.getStatusMap().get("count").valueProperty());
//                    activeRouteProperty.bind(traitNavigationRoutes.getStatusMap().get("activeRoute").valueProperty());
//                    int numOfTarget = 0;
//                    do
//                    {
//                        traitRoutePoint = deviceRegistry.activeDeviceProperty().getValue().getTraitOrNull(Trait.NAVIGATION_ROUTES_ROUTE + "" + numOfTarget);
//                        if (traitRoutePoint != null)
//                        {
//                            if (activeRouteProperty.getValue().equals(traitRoutePoint.getStatusMap().get("name").getValue()))
//                            {
//                                traitRoutePoint = deviceRegistry.activeDeviceProperty().getValue().
//                                        getTraitOrNull(Trait.NAVIGATION_ROUTES_ROUTE + "" + numOfTarget + ".Point" + traitRoutePoint
//                                                .getStatusMap().get("name").getValue());
//                                break;
//                            }
//                        }
//                        //Нет совпадения
//                        traitRoutePoint = null;
//                        numOfTarget++;
//                    }while (numOfTarget < Integer.parseInt(routesCountPointProperty.get()) - 1);
//
//                    if (traitRoutePoint != null)
//                    {
//                        targetSpeedProperty.bind(traitRoutePoint.getStatusMap().get("speed").valueProperty());
//                        targetAltitudeProperty.bind(traitRoutePoint.getStatusMap().get("altitude").valueProperty());
//                    }
//                    else
//                    {
//                        LOG.debug("Нет маршрута");
//                    }
//                }
//                if (traitNavigationMotion != null)
//                {
//                    pitchProperty.bind(traitNavigationMotion.getStatusMap().get("pitch").valueProperty());
//                    rollProperty.bind(traitNavigationMotion.getStatusMap().get("roll").valueProperty());
//                    headingProperty.bind(traitNavigationMotion.getStatusMap().get("heading").valueProperty());
//                    speedProperty.bind(traitNavigationMotion.getStatusMap().get("speed").valueProperty());
//                    altitudeProperty.bind(traitNavigationMotion.getStatusMap().get("altitude").valueProperty());
//                }
            });
            new AnimationTimer() {

                @Override
                public void handle(long now)
                {
                    double pitch = Double.parseDouble(pitchProperty.get());
                    double roll = Double.parseDouble(rollProperty.get());
                    double heading = Double.parseDouble(headingProperty.get());
                    double speed = Double.parseDouble(speedProperty.get());
                    double altitude = Double.parseDouble(altitudeProperty.get());
                    double targetHeading = Double.parseDouble(targetHeadingProperty.getValue());
                    double targetAltitude = Double.parseDouble(targetAltitudeProperty.getValue());
                    double targetSpeed = Double.parseDouble(targetSpeedProperty.getValue());

                    try
                    {
                        drawSkyGround(gc, pitch, roll);
                        drawAirFrame(gc);
                        drawPitchScale(gc, pitch);
                        drawRollState(gc, roll);
                        drawCompass(gc, heading, targetHeading);
                        drawIndicators(gc, speed, altitude, targetSpeed, targetAltitude);
                    } catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

        private double baseScale(double w, double h)
        {
            return Math.min(w, h);
        }

        private double pitchTranslate(GraphicsContext gc, double pitch)
        {
            return baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight()) * BASE_PITCH_SCALE * pitch / BASE_WIDTH;
        }

        private double min4(double a, double b, double c, double d)
        {
            if (b < a) a = b;
            if (c < a) a = c;
            if (d < a) a = d;
            return a;
        }

        private double max4(double a, double b, double c, double d)
        {
            if (b > a) a = b;
            if (c > a) a = c;
            if (d > a) a = d;
            return a;
        }

        private void drawSkyGround(GraphicsContext gc, double pitch, double roll) throws NonInvertibleTransformException
        {
            gc.save();

            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
            gc.rotate(-roll);
            gc.translate(0, pitchTranslate(gc, pitch));

            Bounds skyGroundArea = gc.getCanvas().getBoundsInParent();
            Affine invertTransform = gc.getTransform().createInverse();
            Bounds invertRect = invertTransform.transform(skyGroundArea);

            double topLeftX = invertRect.getMinX();
            double topLeftY = invertRect.getMinY();

            double topRightX = invertRect.getMinX() + invertRect.getWidth();
            double topRightY = invertRect.getMinY();

            double bottomLeftX = invertRect.getMinX();
            double bottomLeftY = invertRect.getMinY() + invertRect.getHeight();

            double bottomRightX = invertRect.getMaxX();
            double bottomRightY = invertRect.getMaxY();

            double minX = min4(topLeftX, topRightX, bottomLeftX, bottomRightX);
            double maxX = max4(topLeftX, topRightX, bottomLeftX, bottomRightX);
            double minY = min4(topLeftY, topRightY, bottomLeftY, bottomRightY);
            double maxY = max4(topLeftY, topRightY, bottomLeftY, bottomRightY);

            gc.beginPath();
            gc.moveTo(minX, 0);
            gc.lineTo(minX, minY);
            gc.lineTo(maxX, minY);
            gc.lineTo(maxX, 0);
            gc.lineTo(minX, 0);
            gc.closePath();

            double end = pitchTranslate(gc, 60);

            Color firstColor = new Color(0, 0.12, 0.34, 1);
            Color secondColor = new Color(0.5, 0.76, 0.99, 1);

            gc.setFill(new LinearGradient(0, -end, 0, 0,  false, CycleMethod.NO_CYCLE, new Stop(0.0, firstColor), new Stop(1.0, secondColor)));
            gc.fill();

            gc.beginPath();
            gc.moveTo(minX, 0);
            gc.lineTo(minX, maxY);
            gc.lineTo(maxX, maxY);
            gc.lineTo(maxX, 0);
            gc.lineTo(minX, 0);
            gc.closePath();

            firstColor = new Color(0.41, 0.31, 0.074, 1);
            secondColor = new Color(0.99, 0.8, 0.32, 1);

            gc.setFill(new LinearGradient(0, end, 0, 0,  false, CycleMethod.NO_CYCLE, new Stop(0.0, firstColor), new Stop(1.0, secondColor)));
            gc.fill();

            gc.restore();
        }
        private void drawAirFrame(GraphicsContext gc)
        {
            gc.save();

            Bounds skyGroundArea = gc.getCanvas().getBoundsInParent();
            double width = baseScale(skyGroundArea.getWidth(), skyGroundArea.getHeight());

            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(-0.15 * width, 0, -0.1 * width, 0);
            gc.strokeLine(-0.1 * width, 0, -0.05 * width, 0.05 * width);
            gc.strokeLine(-0.05 * width, 0.05 * width, 0, 0);
            gc.strokeLine(0, 0, 0.05 * width, 0.05 * width);
            gc.strokeLine(0.05 * width, 0.05 * width, 0.1 * width, 0);
            gc.strokeLine(0.1 * width, 0, 0.15 * width, 0);
            gc.restore();

        }
        private void drawPitchScale(GraphicsContext gc, double pitch)
        {
            gc.save();
            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);

            Affine savedTransform = gc.getTransform();

            double normalWidth = 0.13 * baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            double normalFontSize = Font.getDefault().getSize();

            int startPitch = (int) (((double) Math.round(pitch / BASE_PITCH_SCALE_INTERVAL))*BASE_PITCH_SCALE_INTERVAL);
            startPitch -= 15;
            int maxPitch = startPitch + 30;


            for (int currentPitch = startPitch; currentPitch <= maxPitch; currentPitch += BASE_PITCH_SCALE_INTERVAL)
            {
                gc.translate(0, pitchTranslate(gc, pitch - currentPitch));
                boolean isMajor = ((currentPitch % (BASE_PITCH_SCALE_INTERVAL * 2)) == 0);
                if (isMajor)
                {
                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(3);
                    gc.strokeLine(-normalWidth, 0, -normalWidth / 2, 0);
                    gc.strokeLine(normalWidth / 2, 0, normalWidth, 0);


                    int displayPitch = currentPitch;
                    if (displayPitch > 90)
                        displayPitch = 180 - currentPitch;
                    else if (displayPitch < -90)
                        displayPitch = -180 - currentPitch;

                    gc.setFill(Color.WHITE);
                    gc.fillText(String.valueOf(displayPitch), -normalWidth - normalFontSize * 2, 0);
                    gc.fillText(String.valueOf(displayPitch), normalWidth + normalFontSize * 2, 0);
                }
                else
                {
                    gc.setFill(Color.WHITE);
                    double pointSize = 0.01 * baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                    gc.fillOval(-pointSize/2, -pointSize/2, pointSize, pointSize);
                }

                gc.setTransform(savedTransform);
            }
            gc.restore();
        }
        private void drawRollState(GraphicsContext gc, double roll)
        {
            gc.save();

            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);

            double size = 0.9 * baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            int ROLL_SCALE_RANGE = 60;
            int RESOLUTION = 10;

            Affine savedTransform = gc.getTransform();

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);

            for (int angle = -ROLL_SCALE_RANGE; angle <= ROLL_SCALE_RANGE; angle += RESOLUTION)
            {
                gc.rotate(angle);

                double length = 0.02 * baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

                boolean isMajor = ((angle % (RESOLUTION * 2)) == 0);
                if (isMajor)
                {
                    length *= 2;
                }

                gc.setStroke(Color.WHITE);
                gc.setLineWidth(3);
                gc.strokeLine(0, size / 2, 0, size / 2 + length);

                gc.setTransform(savedTransform);
            }
            gc.strokeArc(-size / 2, -size / 2, size, size, 210, 120, ArcType.OPEN);
            gc.rotate(roll);

            gc.beginPath();

            double x = baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight()) * 0.05;
            gc.translate(0, baseScale(gc.getCanvas().getWidth(), gc.getCanvas().getHeight()) * 0.4);
            gc.beginPath();
            gc.lineTo(-x/2, 0);
            gc.lineTo(x/2, 0);
            gc.lineTo(0, x);
            gc.lineTo(-x/2, 0);
            gc.closePath();

            gc.setFill(Color.WHITE);
            gc.fill();

            gc.restore();
        }
        private void drawCompass(GraphicsContext gc, double heading, double targetHeading)
        {
            gc.save();

            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
            Bounds skyGroundArea = gc.getCanvas().getBoundsInParent();

            double width = baseScale(skyGroundArea.getWidth(), skyGroundArea.getHeight());

            double x = width * 0.05;

            gc.setLineWidth(2);

            gc.translate(0, -width * 0.37);

            gc.beginPath();
            gc.moveTo(x / 2, x);
            gc.lineTo(-x / 2, x);
            gc.lineTo(0, 0);
            gc.closePath();

            gc.setFill(Color.YELLOW);
            gc.fill();

            int COMPASS_RESOLUTION = 5;
            int COMPASS_SCALE = 60;

            while(heading > 359)
                heading -= 360;
            while(heading < 0)
                heading += 360;

            int startHeading = (int) Math.round(heading / COMPASS_RESOLUTION)*COMPASS_RESOLUTION;

            startHeading -= COMPASS_SCALE / 2;

            int stopHeading = startHeading + COMPASS_SCALE;

            gc.translate(0, -width * 0.02);

            Affine savedTransform = gc.getTransform();

            double height = 0.02;
            gc.setFill(Color.WHITE);
            String displayString;
            for (int h = startHeading; h <= stopHeading; h += COMPASS_RESOLUTION)
            {
                gc.translate(-(heading - h) * width * 0.01, 0);

                boolean isMajor = ((h % (COMPASS_RESOLUTION * 2)) == 0);
                double ht;

                if (isMajor)
                {
                    ht = height;
                }
                else
                    ht = height / 2;

                gc.setStroke(Color.WHITE);
                gc.strokeLine(0, -ht / 2 * width, 0, ht / 2 * width);

                boolean isMajorMajor = ((h % (COMPASS_RESOLUTION * 2)) == 0);
                if (isMajorMajor)
                {
                    int displayHeading = h;

                    if (displayHeading > 359)
                        displayHeading = h - 360;
                    else if (displayHeading < 0)
                        displayHeading = h + 360;

                    switch (displayHeading)
                    {
                        case 0:
                            displayString = "N";
                            break;
                        case 90:
                            displayString = "E";
                            break;
                        case 180:
                            displayString = "S";
                            break;
                        case 270:
                            displayString = "W";
                            break;
                        default:
                            displayString = String.valueOf(displayHeading);
                    }
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.setFont(Font.font(NORMAL_FONT_SIZE * width));
                    gc.fillText(displayString, 0, -2 * ht * width);
                }

                gc.setTransform(savedTransform);
            }

            double valueBackgroundWidth = height * 3 * width;

            gc.setLineWidth(3);
            gc.setFill(INDICATOR_BACKGROUND_COLOR);
            gc.fillRect(-valueBackgroundWidth * 1.5, -valueBackgroundWidth * 2.2, valueBackgroundWidth * 3, valueBackgroundWidth * 1.1);

            String headingText = String.format("%03.0f", heading);

            gc.setFill(Color.WHITE);
            gc.fillText(headingText, 0, -valueBackgroundWidth - width * 0.02);

            gc.setFill(INDICATOR_BACKGROUND_COLOR);
            gc.fillRect(-width * 0.47, -valueBackgroundWidth * 1.7, valueBackgroundWidth * 2.6, valueBackgroundWidth);
            headingText = String.format("TH: %2.2f", targetHeading);

            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font(width * NORMAL_FONT_SIZE * 0.8));
            gc.fillText(headingText, -width * 0.42 + valueBackgroundWidth / 2, -valueBackgroundWidth - width * 0.001);

            gc.restore();
        }
        private void drawIndicators(GraphicsContext gc, double speed, double altitude, double targetSpeed, double targetAltitude)
        {
            gc.save();

            gc.translate(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
            Bounds skyGroundArea = gc.getCanvas().getBoundsInParent();

            double scaleFactor = baseScale(skyGroundArea.getWidth(), skyGroundArea.getHeight());

            double posX = 0.35 * scaleFactor;


            double fontSize = scaleFactor * NORMAL_FONT_SIZE;

            double backgroundWidth = 0.15 * scaleFactor;
            double backgroundHeight = 0.5 * scaleFactor;

            double backgroundPos = 0.47 * scaleFactor;

            gc.setFill(INDICATOR_BACKGROUND_COLOR);
            gc.fillRect(-backgroundPos, -backgroundHeight / 2, backgroundWidth, backgroundHeight);
            gc.fillRect(backgroundPos - backgroundWidth, -backgroundHeight / 2, backgroundWidth, backgroundHeight);

            String status = String.format("%d", (int)speed);

            gc.setFont(Font.font(fontSize));
            gc.setFill(Color.WHITE);
            gc.fillText(status, -posX * 1.2, 0);
            status = String.format("%d", (int)altitude);
            gc.fillText(status, posX * 0.9 + backgroundWidth / 3, 0);

            double backgroundHeight2 = backgroundHeight / 6;

            gc.setFill(Color.YELLOW);

            gc.setFill(INDICATOR_BACKGROUND_COLOR);
            gc.fillRect(-backgroundPos, -backgroundHeight / 2 - backgroundHeight2 - 1, backgroundWidth, backgroundHeight2);
            gc.fillRect(backgroundPos - backgroundWidth, -backgroundHeight / 2 - backgroundHeight2 - 1, backgroundWidth, backgroundHeight2);


            gc.setFont(Font.font(fontSize * 0.8));
            gc.setFill(Color.YELLOW);
            status = String.format("%3.0f", targetSpeed);
            gc.fillText("TS " + status, -posX * 1.3, -backgroundHeight / 2 - backgroundHeight2 + fontSize);
            status = String.format("%3.0f", targetAltitude);
            gc.fillText("AL " + status, posX * 0.75 + backgroundWidth / 2, -backgroundHeight / 2 - backgroundHeight2 + fontSize);

            gc.restore();
        }
    }
}
