package ru.cpb9.ifdev.model.domain.message;

import ru.cpb9.ifdev.model.domain.IfDevNameAware;
import ru.cpb9.ifdev.model.domain.IfDevOptionalInfoAware;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface IfDevMessage extends IfDevOptionalInfoAware, IfDevNameAware
{
    <T, E extends Throwable> T accept(IfDevMessageVisitor<T, E> visitor) throws E;

    @NotNull
    List<IfDevMessageParameter> getParameters();

    int getId();
}
