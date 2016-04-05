package ru.cpb9.geotarget;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Artem Shein
 */
public final class DateTimeUtils
{
    private static final DateTimeFormatter PARAMETER_UPDATED_FORMAT = DateTimeFormatter.ofPattern("HH:MM:ss.SS");

    @NotNull
    public static String format(@NotNull LocalDateTime localDateTime)
    {
        return localDateTime.format(PARAMETER_UPDATED_FORMAT);
    }

    private DateTimeUtils()
    {

    }
}
