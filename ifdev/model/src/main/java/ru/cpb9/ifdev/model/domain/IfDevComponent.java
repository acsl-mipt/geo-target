package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
}
