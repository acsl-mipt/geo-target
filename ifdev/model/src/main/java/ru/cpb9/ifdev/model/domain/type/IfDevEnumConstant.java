package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevOptionalInfoAware;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevEnumConstant extends IfDevOptionalInfoAware
{
    @NotNull
    IfDevName getName();
    @NotNull
    String getValue();
}
