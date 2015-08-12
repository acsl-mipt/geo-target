package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.proxy.ProvidePrimitivesIfDevProxyResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevProxyResolver;
import ru.cpb9.ifdev.model.domain.impl.proxy.FindExistingIfDevProxyResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevRegistry implements IfDevRegistry
{
    @NotNull
    private final List<IfDevNamespace> rootNamespaces = new ArrayList<>();
    @NotNull
    private final List<IfDevProxyResolver> proxyResolvers = new ArrayList<>();

    public static IfDevRegistry newInstance()
    {
        return new SimpleIfDevRegistry(new FindExistingIfDevProxyResolver(), new ProvidePrimitivesIfDevProxyResolver());
    }

    private SimpleIfDevRegistry(@NotNull IfDevProxyResolver... resolvers)
    {
        proxyResolvers.addAll(Lists.newArrayList(resolvers));
        rootNamespaces.add(SimpleIfDevNamespace.newInstance(IfDevConstants.SYSTEM_NAMESPACE_NAME,
                Optional.<IfDevNamespace>empty()));

    }

    @NotNull
    @Override
    public List<IfDevNamespace> getRootNamespaces()
    {
        return rootNamespaces;
    }

    @NotNull
    @Override
    public <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull URI uri, @NotNull Class<T> cls)
    {
        for (IfDevProxyResolver resolver : proxyResolvers)
        {
            IfDevResolvingResult<T> result = resolver.resolve(this, uri, cls);
            if (result.getResolvedObject().isPresent())
            {
                return result;
            }
        }
        return SimpleIfDevResolvingResult.immutableEmpty();
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("SimpleIfDevRegistry{rootNamespaces=%s, proxyResolvers=%s}", rootNamespaces, proxyResolvers);
    }

}
