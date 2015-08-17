package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import ru.cpb9.ifdev.model.domain.*;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.type.IfDevType;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public final class IfDevUtils
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

    @NotNull
    public static URI getUriForNamespaceAndName(@NotNull IfDevFqn namespaceFqn, @NotNull IfDevName name)
    {
        List<IfDevName> namespaceNameParts = new ArrayList<>(namespaceFqn.getParts());
        namespaceNameParts.add(name);
        try
        {
            return URI.create("/" + URLEncoder
                    .encode(String.join("/", namespaceNameParts.stream().map(IfDevName::asString).collect(
                            Collectors.toList())), Charsets.UTF_8.name()));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }

    public static List<String> getUriParts(@NotNull URI uri)
    {
        try
        {
            return Lists.newArrayList(
                    URLDecoder.decode(uri.getPath(), Charsets.UTF_8.name()).substring(1).split(Pattern.quote("/")));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }

    @NotNull
    public static List<IfDevNamespace> mergeRootNamespaces(@NotNull List<IfDevNamespace> namespaces)
    {
        List<IfDevNamespace> result = new ArrayList<>();
        namespaces.stream().forEach(n -> mergeNamespaceToNamespacesList(result, n));
        return result;
    }

    private static void mergeNamespaceToNamespacesList(@NotNull List<IfDevNamespace> list,
                                                       @NotNull IfDevNamespace namespace)
    {
        Optional<IfDevNamespace> targetNamespace = list.stream().filter(n -> n.getName().equals(namespace.getName())).findAny();
        if (targetNamespace.isPresent())
        {
            mergeNamespaceTo(targetNamespace.get(), namespace);
        }
        else
        {
            list.add(namespace);
        }
    }

    private static void mergeNamespaceTo(@NotNull IfDevNamespace targetNamespace, @NotNull IfDevNamespace namespace)
    {
        List<IfDevNamespace> subNamespaces = targetNamespace.getSubNamespaces();
        namespace.getSubNamespaces().stream().forEach(n -> {
            mergeNamespaceToNamespacesList(subNamespaces, n);
            n.setParent(targetNamespace);
        });

        List<IfDevUnit> units = targetNamespace.getUnits();
        namespace.getUnits().stream().forEach(u ->
        {
            IfDevName name = u.getName();
            Preconditions.checkState(units.stream().noneMatch(u2 -> u2.getName().equals(name)),
                    "unit name collision '%s'", name);
            u.setNamespace(targetNamespace);
        });
        units.addAll(namespace.getUnits());

        List<IfDevType> types = targetNamespace.getTypes();
        namespace.getTypes().stream().forEach(t ->
        {
            IfDevName name = t.getName();
            Preconditions.checkState(types.stream().noneMatch(t2 -> t2.getName().equals(name)),
                    "type name collision '%s'", name);
            t.setNamespace(targetNamespace);
        });
        types.addAll(namespace.getTypes());

        List<IfDevComponent> components = targetNamespace.getComponents();
        namespace.getComponents().stream().forEach(c ->
        {
            IfDevName name = c.getName();
            Preconditions.checkState(components.stream().noneMatch(c2 -> c2.getName().equals(name)),
                    "component name collision '%s'", name);
            c.setNamespace(targetNamespace);
        });
        components.addAll(namespace.getComponents());
    }

    @NotNull
    public static IfDevNamespace newRootIfDevNamespaceForFqn(@NotNull IfDevFqn namespaceFqn)
    {
        IfDevNamespace namespace = SimpleIfDevNamespace.newInstance(namespaceFqn.getLast(),
                Optional.<IfDevNamespace>empty());
        List<IfDevName> parts = namespaceFqn.getParts();
        for (int i = parts.size() - 2; i >= 0; i--)
        {
            IfDevNamespace parentNamespace = SimpleIfDevNamespace.newInstance(parts.get(i),
                    Optional.<IfDevNamespace>empty());
            namespace.setParent(parentNamespace);
            parentNamespace.getSubNamespaces().add(namespace);
            namespace = parentNamespace;
        }
        return namespace;
    }

    @NotNull
    public static IfDevNamespace newNamespaceForFqn(@NotNull IfDevFqn fqn)
    {
        IfDevNamespace currentNamespace = null;
        for (IfDevName name : fqn.getParts())
        {
            currentNamespace = SimpleIfDevNamespace.newInstance(name, Optional.ofNullable(currentNamespace));
            if (currentNamespace.getParent().isPresent())
            {
                currentNamespace.getParent().get().getSubNamespaces().add(currentNamespace);
            }
        }
        return currentNamespace;
    }
}
