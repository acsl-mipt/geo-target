package ru.cpb9.ifdev.parser;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cpb9.ifdev.model.domain.IfDevElement;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.IfDevUtils;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevRegistry;
import ru.cpb9.parsing.ParsingException;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class IfDevSourceProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(IfDevSourceProvider.class);

    @NotNull
    public IfDevRegistry provide(@NotNull IfDevSourceProviderConfiguration config)
    {
        String resourcePath = config.getResourcePath();
        final IfDevParser parser = new IfDevParser();
        try
        {
            IfDevRegistry registry = SimpleIfDevRegistry.newInstance();
            registry
                    .getRootNamespaces().addAll(
                    IfDevUtils.mergeRootNamespaces(
                            Resources.readLines(Resources.getResource(resourcePath), Charsets.UTF_8)
                                    .stream().filter((name) -> name.endsWith(".ifdev")).map(
                                    (name) -> {
                                        try
                                        {
                                            LOG.debug("Parsing resource '{}'", resourcePath + "/" + name);
                                            IfDevElement element = parser.parse(Resources
                                                    .toString(Resources.getResource(resourcePath + "/" + name),
                                                            Charsets.UTF_8));
                                            Preconditions.checkState(element instanceof IfDevNamespace,
                                                    "must be an instance of IfDevNamespace");
                                            return (IfDevNamespace) element;
                                        }
                                        catch (IOException e)
                                        {
                                            throw new ParsingException(e);
                                        }
                                    }).collect(Collectors.toList())));
            return registry;
        }
        catch (IOException e)
        {
            throw new ParsingException(e);
        }
    }
}
