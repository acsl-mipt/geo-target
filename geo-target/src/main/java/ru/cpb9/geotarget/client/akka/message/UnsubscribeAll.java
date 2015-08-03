package ru.cpb9.geotarget.client.akka.message;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class UnsubscribeAll implements Serializable
{
    private static UnsubscribeAll INSTANCE = new UnsubscribeAll();

    private UnsubscribeAll()
    {
    }

    public static UnsubscribeAll getInstance()
    {
        return INSTANCE;
    }
}
