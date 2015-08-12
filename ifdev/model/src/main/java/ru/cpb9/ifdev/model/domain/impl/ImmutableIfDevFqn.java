package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import ru.cpb9.ifdev.model.domain.IfDevFqn;
import ru.cpb9.ifdev.model.domain.IfDevName;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevFqn implements IfDevFqn
{
    @NotNull
    private final List<IfDevName> parts;

    public static IfDevFqn newInstance(@NotNull List<IfDevName> parts)
    {
        return new ImmutableIfDevFqn(parts);
    }

    @NotNull
    public static IfDevFqn newInstanceFromSource(@NotNull String sourceText)
    {
        return new ImmutableIfDevFqn(Stream.of(sourceText.split(Pattern.quote(".")))
                .map(ImmutableIfDevName::newInstanceFromSourceName).collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public List<IfDevName> getParts()
    {
        return parts;
    }

    @NotNull
    @Override
    public String asString()
    {
        return String.join(".", parts.stream().map(IfDevName::asString).collect(Collectors.<String>toList()));
    }

    @NotNull
    @Override
    public IfDevFqn dropLast()
    {
        return newInstance();
    }

    private ImmutableIfDevFqn(@NotNull List<IfDevName> parts)
    {
        Preconditions.checkArgument(!parts.isEmpty(), "FQN must not be empty");
        this.parts = ImmutableList.copyOf(parts);
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevFqn{parts=%s}",
                String.join(".", parts.stream().map(IfDevName::asString).collect(Collectors.toList())));
    }
}
