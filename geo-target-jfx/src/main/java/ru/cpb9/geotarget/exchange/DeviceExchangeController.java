package ru.cpb9.geotarget.exchange;

import ru.cpb9.geotarget.DeviceGuid;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface DeviceExchangeController
{
    Optional<DeviceGuid> getDeviceGuid();
    boolean isConnected();
}
