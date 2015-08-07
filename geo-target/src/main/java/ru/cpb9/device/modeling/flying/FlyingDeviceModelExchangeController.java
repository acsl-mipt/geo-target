package ru.cpb9.device.modeling.flying;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.SimpleDeviceGuid;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelExchangeController implements DeviceExchangeController
{
    private static int uniqueId = 0;
    @NotNull
    private DeviceGuid deviceGuid = SimpleDeviceGuid.newInstance("model" + uniqueId++);

    @Override
    public Optional<DeviceGuid> getDeviceGuid()
    {
        return Optional.of(deviceGuid);
    }

    @Override
    public boolean isConnected()
    {
        return true;
    }
}
