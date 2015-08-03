package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.collect.Lists;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.modeling.ModelingMessage;
import ru.cpb9.ifdev.modeling.ResolvingMessage;
import ru.cpb9.ifdev.modeling.impl.SimpleMessage;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public final class SimpleIfDevResolvingResult<T extends IfDevReferenceable> implements IfDevResolvingResult<T>
{
    private static final IfDevResolvingResult<IfDevReferenceable> EMPTY = newInstance(Optional.<IfDevReferenceable>empty());
    @NotNull
    private final List<ResolvingMessage> messages;
    @NotNull
    private final Optional<T> resolvedObject;

    @NotNull
    public static <T extends IfDevReferenceable> IfDevResolvingResult<T> newInstance(
            @NotNull Optional<T> resolvedObject)
    {
        return new SimpleIfDevResolvingResult<>(resolvedObject, new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends IfDevReferenceable> IfDevResolvingResult<T> immutableEmpty()
    {
        return (IfDevResolvingResult<T>) EMPTY;
    }

    public static <T extends IfDevReferenceable> IfDevResolvingResult<T> error(@NotNull String msg,
                                                                               @NotNull Object... args)
    {
        return new SimpleIfDevResolvingResult<>(Optional.empty(), Lists.newArrayList(SimpleMessage.error(msg, args)));
    }

    public static <T extends IfDevReferenceable> IfDevResolvingResult<T> merge(
            @NotNull IfDevResolvingResult<T> resolvingResult1,
            @NotNull IfDevResolvingResult<T> resolvingResult2)
    {
        resolvingResult1.getMessages().addAll(resolvingResult2.getMessages());
        return resolvingResult1;
    }

    @NotNull
    @Override
    public Optional<T> getResolvedObject()
    {
        return resolvedObject;
    }

    private SimpleIfDevResolvingResult(@NotNull Optional<T> resolvedObject,
                                       @NotNull List<ResolvingMessage> messages)
    {
        this.resolvedObject = resolvedObject;
        this.messages = messages;
    }

    @Override
    public boolean hasError()
    {
        return messages.stream().filter(msg -> msg.getLevel().equals(ModelingMessage.Level.ERROR)).findAny().isPresent();
    }

    @NotNull
    @Override
    public List<ResolvingMessage> getMessages()
    {
        return messages;
    }
}
