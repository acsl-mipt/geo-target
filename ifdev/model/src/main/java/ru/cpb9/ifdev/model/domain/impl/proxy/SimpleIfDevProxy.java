package ru.cpb9.ifdev.model.domain.impl.proxy;

import com.google.common.base.Charsets;
import ru.cpb9.ifdev.model.domain.IfDevProxy;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

/**
 * @author Artem Shein
 */
public class SimpleIfDevProxy<T extends IfDevReferenceable> implements IfDevProxy<T>
{
    @NotNull
    private final URI uri;

    public static <T extends IfDevReferenceable> SimpleIfDevProxy<T> newInstance(@NotNull URI uri)
    {
        return new SimpleIfDevProxy<>(uri);
    }

    private SimpleIfDevProxy(@NotNull URI uri)
    {
        this.uri = uri;
    }

    @Override
    public IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull Class<T> cls)
    {
        return registry.resolve(uri, cls);
    }

    @Override
    public String toString()
    {
        try
        {
            return String.format("SimpleIfDevProxy{uri=%s}", URLDecoder.decode(uri.toString(), Charsets.UTF_8.name()));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }
}
