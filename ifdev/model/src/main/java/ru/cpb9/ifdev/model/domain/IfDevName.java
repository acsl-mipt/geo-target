package ru.cpb9.ifdev.model.domain;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevName
{
    @NotNull
    String asString();

    @NotNull
    static String mangleName(@NotNull String name)
    {
        if (name.startsWith("^"))
        {
            name = name.substring(1);
        }
        name = name.replaceAll("[ \\\\^]", "");
        Preconditions.checkState(!name.isEmpty(), "invalid name");
        return name;
    }
}