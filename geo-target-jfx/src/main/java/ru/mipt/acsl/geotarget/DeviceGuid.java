package ru.mipt.acsl.geotarget;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public interface DeviceGuid extends Serializable
{
    @NotNull
    String toString();
}
