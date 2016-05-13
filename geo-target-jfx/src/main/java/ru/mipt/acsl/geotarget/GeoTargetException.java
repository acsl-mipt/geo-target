package ru.mipt.acsl.geotarget;

/**
 * @author Artem Shein
 */
public class GeoTargetException extends RuntimeException
{
    public GeoTargetException(Throwable cause)
    {
        super(cause);
    }

    public GeoTargetException(String message)
    {
        super(message);
    }

    public GeoTargetException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
