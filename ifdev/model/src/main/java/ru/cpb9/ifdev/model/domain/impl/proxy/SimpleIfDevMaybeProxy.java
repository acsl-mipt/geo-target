package ru.cpb9.ifdev.model.domain.impl.proxy;

import com.google.common.collect.Lists;
import ru.cpb9.common.Either;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.IfDevUtils;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevFqn;
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
    @NotNull
    private Optional<T> resolvedObject;
    @NotNull
    private Optional<IfDevProxy<T>> proxy;

    @NotNull
    private static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxy(@NotNull IfDevProxy<K> proxy)
    {
        return new SimpleIfDevMaybeProxy<>(proxy);
    }

    @NotNull
    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxy(@NotNull URI uri)
    {
        return proxy(SimpleIfDevProxy.newInstance(uri));
    }

    @NotNull
    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxy(@NotNull IfDevFqn namespaceFqn,
                                                                          @NotNull IfDevName name)
    {
        return proxy(IfDevUtils.getUriForNamespaceAndName(namespaceFqn, name));
    }

    @NotNull
    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxyForSystem(@NotNull IfDevName ifDevName)
    {
        return proxy(ImmutableIfDevFqn.newInstance(Lists.newArrayList(IfDevConstants.SYSTEM_NAMESPACE_NAME)), ifDevName);
    }

    public static <K extends IfDevReferenceable> IfDevMaybeProxy<K> proxyDefaultNamespace(@NotNull IfDevFqn elementFqn, @NotNull IfDevNamespace defaultNamespace)
    {
        return elementFqn.size() > 1? proxy(elementFqn.copyDropLast(), elementFqn.getLast()) : proxy(defaultNamespace.getFqn(), elementFqn.getLast());
    }

    @NotNull
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

    @NotNull
    @Override
    public IfDevProxy<T> getProxy()
    {
        return proxy.get();
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
