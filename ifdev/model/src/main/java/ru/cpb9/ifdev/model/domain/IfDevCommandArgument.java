package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevCommandArgument extends IfDevNameAware, IfDevOptionalInfoAware
{
    @NotNull
    Optional<IfDevMaybeProxy<IfDevUnit>> getUnit();

    @NotNull
    IfDevMaybeProxy<IfDevType> getType();
}
