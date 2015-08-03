package ru.cpb9.geotarget.client.akka.message;

import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */

public class SubscribeAll implements PersistentSubscribe<UnsubscribeAll>
{
    private static final SubscribeAll INSTANCE = new SubscribeAll();

    private SubscribeAll()
    {
    }

    public static SubscribeAll getInstance()
    {
        return INSTANCE;
    }

    @NotNull
    @Override
    public UnsubscribeAll getUnsubscribeMessage()
    {
        return UnsubscribeAll.getInstance();
    }
}
