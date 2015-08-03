package ru.cpb9.ifdev.model.domain.impl.proxy;

import ru.cpb9.ifdev.model.domain.IfDevFqn;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.impl.IfDevUriUtils;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public final class IfDevProxyUtils
{
    public static <T extends IfDevReferenceable> IfDevMaybeProxy<T> newProxyFor(@NotNull IfDevFqn namespaceFqn,
                                                                                @NotNull IfDevName name)
    {
        return SimpleIfDevMaybeProxy.proxy(IfDevUriUtils.getUriForNamespaceAndName(namespaceFqn, name));
    }

    private IfDevProxyUtils()
    {
    }
}
