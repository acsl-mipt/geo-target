package ru.cpb9.ifdev.model.domain.impl.proxy;

import ru.cpb9.ifdev.common.Either;
import ru.cpb9.ifdev.model.domain.IfDevProxy;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevResolvingResult;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevMaybeProxy<T extends IfDevReferenceable> extends Either<T, IfDevProxy<T>> implements
        IfDevMaybeProxy<T>
{
    private Optional<T> resolvedObject;
    private Optional<IfDevProxy<T>> proxy;

    private static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxy(@NotNull IfDevProxy<K> proxy)
    {
        return new SimpleIfDevMaybeProxy<>(proxy);
    }

    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxy(@NotNull URI uri)
    {
        return proxy(SimpleIfDevProxy.newInstance(uri));
    }

    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> object(@NotNull K object)
    {
        return new SimpleIfDevMaybeProxy<>(object);
    }

    @Override
    public IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull Class<T> cls)
    {
        if (!isProxy())
        {
            return SimpleIfDevResolvingResult.newInstance(resolvedObject);
        }
        IfDevResolvingResult<T> resolvingResult = proxy.get().resolve(registry, cls);
        if (resolvingResult.getResolvedObject().isPresent())
        {
            resolvedObject = resolvingResult.getResolvedObject();
            proxy = Optional.empty();
            return resolvingResult;
        }
        return SimpleIfDevResolvingResult.error("Can't resolve proxy '%s'", this);
    }

    @Override
    public boolean isProxy()
    {
        return isRight();
    }

    @NotNull
    @Override
    public T getObject()
    {
        return getLeft();
    }

    @Override
    public boolean isLeft()
    {
        return resolvedObject.isPresent();
    }

    @Override
    public boolean isRight()
    {
        return !resolvedObject.isPresent();
    }

    @Override
    @NotNull
    public T getLeft()
    {
        return resolvedObject.get();
    }

    @Override
    @NotNull
    public IfDevProxy<T> getRight()
    {
        return proxy.get();
    }

    private SimpleIfDevMaybeProxy(@NotNull IfDevProxy<T> right)
    {
        resolvedObject = Optional.empty();
        proxy = Optional.of(right);
    }

    private SimpleIfDevMaybeProxy(@NotNull T object)
    {
        resolvedObject = Optional.of(object);
        proxy = Optional.empty();
    }

    @Override
    public String toString()
    {
        return String.format("SimpleIfDevMaybeProxy{%s=%s}", isProxy()? "proxy" : "object", isProxy()? proxy.get() : resolvedObject.get());
    }
}
