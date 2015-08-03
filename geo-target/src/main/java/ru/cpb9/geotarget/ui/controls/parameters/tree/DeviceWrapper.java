package ru.cpb9.geotarget.ui.controls.parameters.tree;

import ru.cpb9.geotarget.model.Device;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * @author Alexander Kuchuk.
 */


public class DeviceWrapper implements TreeNodeWrapper
{
    @NotNull
    private final Device device;

    public DeviceWrapper(Device device)
    {
        this.device = device;
    }

    @Override
    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(device.getName());
    }

    @Override
    public SimpleStringProperty infoProperty() {
        return new SimpleStringProperty(device.getInfo().orElse(""));
    }

    @Override
    public SimpleStringProperty valueProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public SimpleStringProperty typeProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public SimpleObjectProperty<LocalDateTime> timeProperty() {
        return new SimpleObjectProperty<>();
    }
}
