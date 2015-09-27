package ru.cpb9.geotarget.akka.messages;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class AllMessagesSubscribe implements Serializable
{
    public static final AllMessagesSubscribe INSTANCE = new AllMessagesSubscribe();

    private AllMessagesSubscribe()
    {

    }
}
