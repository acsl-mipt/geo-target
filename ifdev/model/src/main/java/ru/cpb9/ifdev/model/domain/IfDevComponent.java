package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.impl.IfDevParameterWalker;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public interface IfDevComponent extends IfDevOptionalInfoAware, IfDevReferenceable, IfDevNamespaceAware
{
    @NotNull
    Optional<IfDevMaybeProxy<IfDevType>> getBaseType();

    @NotNull
    Set<IfDevMaybeProxy<IfDevComponent>> getSubComponents();

    @NotNull
    List<IfDevCommand> getCommands();

    @NotNull
    List<IfDevMessage> getMessages();

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }

    @NotNull
    default SortedSet<IfDevComponent> getAllSubComponentsOrdered()
    {
        SortedSet<IfDevComponent> components = new TreeSet<>();
        components.addAll(getSubComponents().stream().map(IfDevMaybeProxy::getObject).collect(Collectors.toSet()));
        components.addAll(
                components.stream().flatMap(c -> c.getAllSubComponentsOrdered().stream()).collect(Collectors.toSet()));
        return components;
    }

    @NotNull
    default IfDevType getTypeForParameter(@NotNull IfDevMessageParameter parameter)
    {
        new IfDevParameterWalker(parameter);
        throw new AssertionError("not implemented");
    }
}
