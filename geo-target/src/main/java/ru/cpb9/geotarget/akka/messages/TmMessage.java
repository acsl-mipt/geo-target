package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class TmMessage implements Serializable
{
    private final Object values;

    public TmMessage(@NotNull IfDevMessage message, @NotNull Object values)
    {
        this.values = values;
    }

    public Object getValues()
    {
        return values;
    }
}
