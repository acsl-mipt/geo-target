package ru.mipt.acsl.geotarget.exchange;

import ru.mipt.acsl.geotarget.DeviceGuid;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface DeviceExchangeController
{
    Optional<DeviceGuid> getDeviceGuid();
    boolean isConnected();
}
