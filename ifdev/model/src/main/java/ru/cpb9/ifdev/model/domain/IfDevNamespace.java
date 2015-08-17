package ru.cpb9.ifdev.model.domain;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevFqn;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevNamespace extends IfDevReferenceable, IfDevNameAware
{
    @NotNull
    String asString();

    @NotNull
    List<IfDevUnit> getUnits();

    @NotNull
    List<IfDevType> getTypes();

    void setTypes(@NotNull List<IfDevType> types);

    @NotNull
    List<IfDevNamespace> getSubNamespaces();

    @NotNull
    Optional<IfDevNamespace> getParent();

    @NotNull
    List<IfDevComponent> getComponents();

    default <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }

    @NotNull
    default IfDevFqn getFqn()
    {
        List<IfDevName> parts = new ArrayList<>();
        IfDevNamespace currentNamespace = this;
        while (currentNamespace.getParent().isPresent())
        {
            parts.add(currentNamespace.getName());
            currentNamespace = currentNamespace.getParent().get();
        }
        parts.add(currentNamespace.getName());
        return ImmutableIfDevFqn.newInstance(Lists.reverse(parts));
    }

    void setParent(@Nullable IfDevNamespace parent);

    @NotNull
    default IfDevElement getRootNamespace()
    {
        return getParent().map(IfDevNamespace::getRootNamespace).orElse(this);
    }
}
