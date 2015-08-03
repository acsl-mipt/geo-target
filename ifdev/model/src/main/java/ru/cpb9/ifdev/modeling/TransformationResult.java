package ru.cpb9.ifdev.modeling;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface TransformationResult<T>
{
    @NotNull
    Optional<T> getResult();
    @NotNull
    List<TransformationMessage> getMessages();
    boolean hasError();
}
