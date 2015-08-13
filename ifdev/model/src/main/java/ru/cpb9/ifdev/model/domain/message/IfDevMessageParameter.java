package ru.cpb9.ifdev.model.domain.message;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevElement;

/**
 * @author Artem Shein
 */
public interface IfDevMessageParameter extends IfDevElement
{
    @NotNull
    String getValue();
}
