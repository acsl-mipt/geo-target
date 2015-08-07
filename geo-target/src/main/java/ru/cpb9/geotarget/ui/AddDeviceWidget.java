package ru.cpb9.geotarget.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceController;
import ru.cpb9.geotarget.exchange.MavlinkDevice;

/**
 * @author Artem Shein
 */
public class AddDeviceWidget extends Widget
{
    @NotNull
    private final DeviceController deviceController;

    public AddDeviceWidget(@NotNull DeviceController deviceController)
    {
        super("Add device");
        this.deviceController = deviceController;
        ObservableList<String> deviceTypes = FXCollections.observableArrayList("Mavlink");
        Label typeLabel = new Label("Device type:");
        ComboBox<String> deviceTypeComboBox = new ComboBox<>(deviceTypes);
        TextField devicePortTextField = new TextField("14550");
        Label portLabel = new Label("Local port:");
        Button addButton = new Button("Add");
        addButton.setOnAction((e) ->
                deviceController.getDeviceRegistry().getDevices().add(
                        MavlinkDevice.newMavlinkDevice(Integer.parseInt(devicePortTextField.getText()))));
        HBox box = new HBox(typeLabel, deviceTypeComboBox, portLabel, devicePortTextField, addButton);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        setContent(box);
    }
}
