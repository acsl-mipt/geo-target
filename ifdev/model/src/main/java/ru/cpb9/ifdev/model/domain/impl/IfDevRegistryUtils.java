package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Preconditions;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevFqn;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public final class IfDevRegistryUtils
{
    @NotNull
    public static IfDevNamespace getOrCreateNamespaceByFqn(@NotNull IfDevRegistry registry,
                                                           @NotNull String namespaceFqn)
    {
        Preconditions.checkArgument(!namespaceFqn.isEmpty());

        List<IfDevNamespace> namespaces = registry.getRootNamespaces();
        IfDevNamespace namespace = null;

        for (String namespaceName : namespaceFqn.split(Pattern.quote(".")))
        {
            final IfDevNamespace parentNamespace = namespace;
            namespace = namespaces.stream().filter(ns -> ns.getName().asString().equals(namespaceName)).findAny().orElseGet(() -> {
                IfDevNamespace newNamespace = SimpleIfDevNamespace.newInstance(ImmutableIfDevName.newInstanceFromMangledName(namespaceName),
                        Optional.ofNullable(parentNamespace));
                if (parentNamespace != null)
                {
                    parentNamespace.getSubNamespaces().add(newNamespace);
                }
                else
                {
                    namespaces.add(newNamespace);
                }
                return newNamespace;
            });
        }
        //noinspection ConstantConditions
        return namespace;
    }

    @NotNull
    public static Optional<IfDevNamespace> getNamespaceByFqn(@NotNull IfDevRegistry registry,
                                                             @NotNull IfDevFqn namespaceFqn)
    {
        List<IfDevNamespace> namespaces = registry.getRootNamespaces();
        Optional<IfDevNamespace> namespace = Optional.empty();
        for (IfDevName namespaceName : namespaceFqn.getParts())
        {
            namespace = namespaces.stream().filter(ns -> ns.getName().equals(namespaceName)).findAny();
            if (!namespace.isPresent())
            {
                return namespace;
            }
            else
            {
                namespaces = namespace.get().getSubNamespaces();
            }
        }
        return  namespace;
    }
}
