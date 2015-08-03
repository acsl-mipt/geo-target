package ru.cpb9.geotarget;

import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface DeviceController
{
    @NotNull
    DeviceRegistry getDeviceRegistry();

    @NotNull
    WorldWindNode getWorldWind();

    void navigateToActiveDevice();

    void requestRepaint();
}
