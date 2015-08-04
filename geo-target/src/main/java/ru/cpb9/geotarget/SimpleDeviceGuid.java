package ru.cpb9.geotarget;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class SimpleDeviceGuid implements DeviceGuid
{
    @NotNull
    private final String uniqueName;

    public static DeviceGuid newInstance(String name)
    {
        return new SimpleDeviceGuid(name);
    }

    @Override
    @NotNull
    public String toString()
    {
        return uniqueName;
    }

    private SimpleDeviceGuid(@NotNull String uniqueName)
    {
        this.uniqueName = uniqueName;
    }
}
