package ru.cpb9.geotarget;

/**
 * @author Artem Shein
 */
public final class NullUtils
{
    public static boolean isNotNull(Object... objects)
    {
        for (Object object : objects)
        {
            if (object == null)
            {
                return false;
            }
        }
        return true;
    }

    private NullUtils()
    {
    }
}
