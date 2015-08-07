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

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof DeviceGuid))
        {
            return false;
        }
        return uniqueName.equals(o.toString());
    }

    @Override
    public int hashCode()
    {
        return uniqueName.hashCode();
    }

    private SimpleDeviceGuid(@NotNull String uniqueName)
    {
        this.uniqueName = uniqueName;
    }
}
