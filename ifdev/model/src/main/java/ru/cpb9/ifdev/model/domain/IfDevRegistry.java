package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public interface IfDevRegistry
{
    @NotNull
    List<IfDevNamespace> getRootNamespaces();

    @NotNull
    <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull URI uri, @NotNull Class<T> cls);

    @NotNull
    default Optional<IfDevComponent> getComponent(@NotNull String fqn)
    {
        int dotPos = fqn.lastIndexOf('.');
        Optional<IfDevNamespace> namespaceOptional = getNamespace(fqn.substring(0, dotPos));
        if (!namespaceOptional.isPresent())
        {
            return Optional.empty();
        }
        IfDevName componentName = ImmutableIfDevName.newInstanceFromMangledName(fqn.substring(dotPos + 1, fqn.length()));
        return namespaceOptional.get().getComponents().stream().filter((c) -> c.getName().equals(componentName)).findAny();
    }

    @NotNull
    default Optional<IfDevNamespace> getNamespace(@NotNull String fqn)
    {
        List<IfDevNamespace> currentNamespaces = getRootNamespaces();
        Optional<IfDevNamespace> currentNamespace = Optional.empty();
        for (String namespaceName : fqn.split(Pattern.quote(".")))
        {
            if (currentNamespaces == null)
            {
                return Optional.empty();
            }
            IfDevName ifDevName = ImmutableIfDevName.newInstanceFromMangledName(namespaceName);
            currentNamespace = currentNamespaces.stream().filter((n) -> n.getName().equals(ifDevName)).findAny();
            currentNamespaces = currentNamespace.isPresent() ? currentNamespace.get().getSubNamespaces() : null;
        }
        return currentNamespace;
    }

    @NotNull
    default Optional<IfDevMessage> getMessage(@NotNull String fqn)
    {
        int dotPos = fqn.lastIndexOf('.');
        IfDevName ifDevName = ImmutableIfDevName.newInstanceFromMangledName(fqn.substring(dotPos + 1, fqn.length()));
        return getComponent(fqn.substring(0, dotPos)).map((c) -> c.getMessages().stream().filter((m) -> m.getName().equals(ifDevName)).findAny().orElse(null));
    }

    @NotNull
    default IfDevMessage getMessageOrThrow(@NotNull String fqn)
    {
        return getMessage(fqn).get();
    }

}
