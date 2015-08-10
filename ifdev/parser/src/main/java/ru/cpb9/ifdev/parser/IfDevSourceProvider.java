package ru.cpb9.ifdev.parser;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevElement;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.IfDevResource;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevRegistry;
import ru.cpb9.parsing.ParsingException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class IfDevSourceProvider
{
    @NotNull
    IfDevRegistry provide(@NotNull IfDevSourceProviderConfiguration config) throws IOException
    {
        String resourcePath = config.getResourcePath();
        final IfDevParser parser = new IfDevParser();
        return SimpleIfDevRegistry.newInstance(Resources.readLines(Resources.getResource(resourcePath), Charsets.UTF_8)
                .stream().filter((name) -> name.endsWith(".ifdev")).map(
                (name) -> {
                    try
                    {
                        IfDevElement element = parser.parse(Resources
                                .toString(Resources.getResource(resourcePath + "/" + name), Charsets.UTF_8));
                        Preconditions.checkState(element instanceof IfDevResource, "must be an instance of IfDevResource");
                        return (IfDevResource)element;
                    }
                    catch (IOException e)
                    {
                        throw new ParsingException(e);
                    }
                }).collect(Collectors.toList()));
    }
}
