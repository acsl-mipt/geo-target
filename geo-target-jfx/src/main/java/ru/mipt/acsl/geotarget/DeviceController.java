package ru.mipt.acsl.geotarget;

import ru.mipt.acsl.geotarget.ui.controls.WorldWindNode;

/**
 * @author Artem Shein
 */
public interface DeviceController {

    DeviceRegistry getDeviceRegistry();

    WorldWindNode getWorldWind();

    void navigateToActiveDevice();

    void requestRepaint();

    static DeviceController newInstance(DeviceRegistry deviceRegistry, WorldWindNode worldWind)
    {
        return new DeviceControllerImpl(deviceRegistry, worldWind);
    }

}