package ru.cpb9.geotarget.ui;

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
import ru.cpb9.geotarget.*;
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

    public AddDeviceWidget(@NotNull DeviceController deviceController)
    {
        super(I.addDevice());
        this.deviceController = deviceController;
        ObservableList<DeviceTypeInfo> deviceTypes = FXCollections.observableArrayList(
                new DeviceTypeInfo(I.internalFlyingDeviceModel(), () -> null),
                new DeviceTypeInfo(I.mavlink(), () -> {
                    TextField devicePortTextField = new TextField("14550");
                    Label portLabel = new Label(I.localPort());
                    return Optional.of(new HBox(devicePortTextField, portLabel));
                }));
        Label typeLabel = new Label(I.deviceType());
        ComboBox<DeviceTypeInfo> deviceTypeComboBox = new ComboBox<>(deviceTypes);
        deviceTypeComboBox.getSelectionModel().selectFirst();
        Group deviceTypeConfigPane = new Group();

        deviceTypeComboBox.setOnAction((e) ->
        {
            deviceTypeConfigPane.getChildren().clear();
            deviceTypeComboBox.getSelectionModel().getSelectedItem().getTypedConfigFormProducer().get().ifPresent((n) -> deviceTypeConfigPane.getChildren().add(n));
        });

        Button addButton = new Button(I.add());
        /*addButton.setOnAction((e) ->
                deviceController.getDeviceRegistry().getDevices().add(
                        MavlinkDevice.newMavlinkDevice(Integer.parseInt(devicePortTextField.getText()))));*/
        VBox box = new VBox(typeLabel, deviceTypeComboBox, deviceTypeConfigPane, addButton);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(5);
        setContent(box);
    }

    private static class DeviceTypeInfo
    {
        @NotNull
        private final String title;
        @NotNull
        private final Supplier<Optional<Node>> typedConfigFormProducer;

        @NotNull
        public Supplier<Optional<Node>> getTypedConfigFormProducer()
        {
            return typedConfigFormProducer;
        }

        public DeviceTypeInfo(@NotNull String title, @NotNull Supplier<Optional<Node>> typedConfigFormProducer)
        {
            this.title = title;
            this.typedConfigFormProducer = typedConfigFormProducer;
        }

        @Override
        public String toString()
        {
            return title;
        }
    }
}
