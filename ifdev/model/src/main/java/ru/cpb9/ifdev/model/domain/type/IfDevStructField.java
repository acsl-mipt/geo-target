package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.IfDevOptionalInfoAware;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.IfDevNameAware;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevStructField extends IfDevNameAware, IfDevOptionalInfoAware
{
    @NotNull
    IfDevMaybeProxy<IfDevType> getType();

    @NotNull
    Optional<IfDevMaybeProxy<IfDevUnit>> getUnit();
}
