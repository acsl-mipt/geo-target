package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.Nullable;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevNamespace implements IfDevNamespace
{
    @NotNull
    private final IfDevName name;
    @NotNull
    private List<IfDevType> types = new ArrayList<>();
    @NotNull
    private final List<IfDevUnit> units = new ArrayList<>();
    @NotNull
    private final List<IfDevNamespace> subNamespaces = new ArrayList<>();
    @NotNull
    private Optional<IfDevNamespace> parent;
    @NotNull
    private List<IfDevComponent> components = new ArrayList<>();

    public static IfDevNamespace newInstance(@NotNull IfDevName name, @NotNull Optional<IfDevNamespace> parent)
    {
        return new SimpleIfDevNamespace(name, parent);
    }

    private SimpleIfDevNamespace(@NotNull IfDevName name, @NotNull Optional<IfDevNamespace> parent)
    {
        this.name = name;
        this.parent = parent;
    }

    @NotNull
    @Override
    public String asString()
    {
        return name.asString();
    }

    @NotNull
    @Override
    public List<IfDevUnit> getUnits()
    {
        return units;
    }

    @NotNull
    @Override
    public List<IfDevType> getTypes()
    {
        return types;
    }

    @Override
    public void setTypes(@NotNull List<IfDevType> types)
    {
        this.types = types;
    }

    @NotNull
    @Override
    public List<IfDevNamespace> getSubNamespaces()
    {
        return subNamespaces;
    }

    @NotNull
    @Override
    public Optional<IfDevNamespace> getParent()
    {
        return parent;
    }

    @NotNull
    @Override
    public List<IfDevComponent> getComponents()
    {
        return components;
    }

    @Override
    public void setParent(@Nullable IfDevNamespace parent)
    {
        this.parent = Optional.ofNullable(parent);
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    @Override
    @NotNull
    public IfDevName getName()
    {
        return name;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("Namespace{name=%s, %d subnamespaces, %d units, %d types, %d components}", name.asString(),
                subNamespaces.size(), units.size(), types.size(), components.size());
    }

    @NotNull
    public static IfDevNamespace newRootNamespaceFor(@NotNull IfDevFqn namespaceFqn)
    {
        IfDevNamespace namespace = SimpleIfDevNamespace.newInstance(namespaceFqn.getLast(),
                Optional.<IfDevNamespace>empty());
        List<IfDevName> parts = namespaceFqn.getParts();
        for (int i = parts.size() - 2; i >= 0; i--)
        {
            IfDevNamespace parentNamespace = SimpleIfDevNamespace.newInstance(parts.get(i),
                    Optional.<IfDevNamespace>empty());
            namespace.setParent(parentNamespace);
            namespace = parentNamespace;
        }
        return namespace;
    }
}
