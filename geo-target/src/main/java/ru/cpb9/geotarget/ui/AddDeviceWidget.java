package ru.cpb9.geotarget.ui;

import akka.actor.ActorRef;
import c10n.C10N;
import c10n.C10NMessages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.flying.FlyingDeviceModelActor;
import ru.cpb9.geotarget.*;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;
import ru.cpb9.geotarget.exchange.mavlink.MavlinkDevice;

import java.util.Optional;
import java.util.function.Supplier;

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
        deviceTypeComboBox.getSelectionModel().selectFirst();
        Group deviceTypeConfigPane = new Group();

        deviceTypeComboBox.setOnAction((e) ->
        {
            deviceTypeConfigPane.getChildren().clear();
            deviceTypeComboBox.getSelectionModel().getSelectedItem().getTypedConfigFormNode().ifPresent(
                    (n) -> deviceTypeConfigPane.getChildren().add(n));
        });

        Button addButton = new Button(I.add());
        addButton.setOnAction((e) -> deviceController.getDeviceRegistry().getDevices().add(deviceTypeComboBox.getSelectionModel().getSelectedItem().newDevice()));
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
        public abstract String getTitle();
        @NotNull
        public abstract DeviceExchangeController newDevice();
    }

    private class ModelDeviceTypeInfo extends DeviceTypeInfo
    {

        @NotNull
        @Override
        public Optional<Node> getTypedConfigFormNode()
        {
            return Optional.empty();
        }

        @NotNull
        @Override
        public String getTitle()
        {
            return I.internalFlyingDeviceModel();
        }

        @NotNull
        @Override
        public DeviceExchangeController newDevice()
        {
            return new FlyingDeviceModelActor(tmServer);
        }
    }

    private class MavlinkDeviceTypeInfo extends DeviceTypeInfo
    {
        private final HBox hbox;
        private final TextField devicePortTextField;

        public MavlinkDeviceTypeInfo()
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
        public String getTitle()
        {
            return I.mavlink();
        }

        @NotNull
        @Override
        public DeviceExchangeController newDevice()
        {
            return MavlinkDevice.newMavlinkDevice(Integer.parseInt(devicePortTextField.getText()));
        }
    }
}
