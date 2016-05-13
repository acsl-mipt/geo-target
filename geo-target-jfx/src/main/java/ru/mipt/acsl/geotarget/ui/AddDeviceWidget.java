package ru.mipt.acsl.geotarget.ui;

import akka.actor.ActorRef;
import c10n.C10N;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.device.modeling.flying.*;
import ru.mipt.acsl.geotarget.DeviceController;
import ru.mipt.acsl.geotarget.exchange.mavlink.MavlinkDeviceExchangeController;
import ru.mipt.acsl.geotarget.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class AddDeviceWidget extends Widget
{
    @NotNull
    private static Messages I = C10N.get(Messages.class);
    @NotNull
    private final DeviceController deviceController;
    @NotNull
    private final ActorRef tmServer;

    public AddDeviceWidget(@NotNull DeviceController deviceController, @NotNull ActorRef tmServer)
    {
        super(I.addDevice());
        this.tmServer = tmServer;
        this.deviceController = deviceController;
        ObservableList<DeviceTypeInfo> deviceTypes = FXCollections.observableArrayList(
                new ModelDeviceTypeInfo(),
                new MavlinkDeviceTypeInfo());
        Label typeLabel = new Label(I.deviceType());
        ComboBox<DeviceTypeInfo> deviceTypeComboBox = new ComboBox<>(deviceTypes);

        Group deviceTypeConfigPane = new Group();

        Runnable deviceTypeSelectAction = () ->
        {
            deviceTypeConfigPane.getChildren().clear();
            deviceTypeComboBox.getSelectionModel().getSelectedItem().getTypedConfigFormNode().ifPresent(
                    (n) -> deviceTypeConfigPane.getChildren().add(n));
        };

        deviceTypeComboBox.setOnAction(e -> deviceTypeSelectAction.run());
        deviceTypeComboBox.getSelectionModel().selectFirst();
        deviceTypeSelectAction.run();

        Button addButton = new Button(I.add());
        addButton.setOnAction((e) -> deviceController.getDeviceRegistry().getDevices().addAll(
                deviceTypeComboBox.getSelectionModel().getSelectedItem().newDevice()));
        VBox box = new VBox(typeLabel, deviceTypeComboBox, deviceTypeConfigPane, addButton);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(5);
        setContent(box);
    }

    private abstract static class DeviceTypeInfo
    {
        @NotNull
        public abstract Optional<Node> getTypedConfigFormNode();
        @NotNull
        public abstract Device newDevice();
    }

    private class ModelDeviceTypeInfo extends DeviceTypeInfo
    {
        @NotNull
        private final ComboBox<String> routeComboBox;
        @NotNull
        private final VBox vBox;

        ModelDeviceTypeInfo()
        {
            routeComboBox = new ComboBox<>();
            routeComboBox.getItems().addAll(I.caucasus(), I.moscow(), I.transContinental(), I.infinity());
            routeComboBox.getSelectionModel().selectFirst();
            vBox = new VBox(new Label(I.route()), routeComboBox);
        }

        @NotNull
        @Override
        public Optional<Node> getTypedConfigFormNode()
        {
            return Optional.of(vBox);
        }

        @NotNull
        @Override
        public String toString()
        {
            return I.internalFlyingDeviceModel();
        }

        @NotNull
        @Override
        public Device newDevice()
        {
            switch (routeComboBox.getSelectionModel().getSelectedIndex())
            {
                case 1:
                    return new Device(new FlyingDeviceModelExchangeController(tmServer,
                            new Coordinates(55.973579, 37.412816, 300.),
                            new Route(2, SimplifiedRouteKind.AUTO, true, 0, 50, "Model 2 route",
                                    new RoutePoint(55.930206, 37.518173, 1000., 60., 1),
                                    new RoutePoint(55.751958, 37.618155, 1000., 60.),
                                    new RoutePoint(55.520834, 37.549276, 2000., 60.),
                                    new RoutePoint(55.724031, 37.272329, 3000., 60.),
                                    new RoutePoint(55.916189, 37.846193, 500., 40.),
                                    new RoutePoint(56.31033, 38.130507, 5000., 100., 8))));
                case 2:
                    return new Device(new FlyingDeviceModelExchangeController(tmServer,
                            new Coordinates(33.944054, -118.413939, 0.0),
                            new Route(3, SimplifiedRouteKind.AUTO, true, 0, 50, "Model 3 route",
                                            new RoutePoint(40.774221, -73.872793, 1000.0, 1200.0, 1),
                                            new RoutePoint(48.728777, 2.365703, 1000.0, 1200.0),
                                            new RoutePoint(55.973552, -118.413939, 1000.0, 1200.0),
                                            new RoutePoint(39.91886, 116.385471, 1200.0, 1200.0),
                                            new RoutePoint(35.72109, 139.690143, 4000.0, 1200.0),
                                            new RoutePoint(33.944054, -118.413939, 4000.0, 1200.0, 8)
                                    )));
                case 3:
                    double infStartLatGrad = 55.75435, infStartLongGrad = 37.622864, radiusGrad = 0.1;
                    int segments = 20;
                    List<RoutePoint> infRoute = new ArrayList<>();
                    for (int i = 0; i < segments; i++)
                    {
                        double angleRad = Math.toRadians(180. - i * 360. / segments);
                        infRoute.add(new RoutePoint(infStartLatGrad + radiusGrad * Math.sin(angleRad),
                                infStartLongGrad + radiusGrad + radiusGrad * Math.cos(angleRad), 500.0, 60.0));
                    }
                    for (int i = 0; i < segments; i++)
                    {
                        double angleRad = Math.toRadians(i * 360.0 / segments);
                        infRoute.add(new RoutePoint(infStartLatGrad + radiusGrad * Math.sin(angleRad),
                                infStartLongGrad - radiusGrad + radiusGrad * Math.cos(angleRad), 500.0, 60.0));
                    }
                    return new Device(new FlyingDeviceModelExchangeController(tmServer,
                            new Coordinates(55.75435, 37.622864, 0.0),
                            new Route(4, SimplifiedRouteKind.AUTO, true, 0, 50, "Model 4 route", infRoute)));
                default:
                    return new Device(new FlyingDeviceModelExchangeController(tmServer));
            }
        }
    }

    private class MavlinkDeviceTypeInfo extends DeviceTypeInfo
    {
        private final HBox hbox;
        private final TextField devicePortTextField;

        MavlinkDeviceTypeInfo()
        {
            devicePortTextField = new TextField("14550");
            Label portLabel = new Label(I.localPort());
            hbox = new HBox(devicePortTextField, portLabel);
        }

        @NotNull
        @Override
        public Optional<Node> getTypedConfigFormNode()
        {
            return Optional.of(hbox);
        }

        @NotNull
        @Override
        public String toString()
        {
            return I.mavlink();
        }

        @NotNull
        @Override
        public Device newDevice()
        {
            return new Device(MavlinkDeviceExchangeController.newInstance(
                    Integer.parseInt(devicePortTextField.getText())));
        }
    }
}
