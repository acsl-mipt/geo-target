package ru.cpb9.ifdev.modeling;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface ResolvingResult
{
    boolean hasError();

    @NotNull
    List<ResolvingMessage> getMessages();
}
