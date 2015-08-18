package ru.cpb9.ifdev.java.generator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;

import java.io.File;

/**
 * @author Artem Shein
 */
public class JavaIfDevSourcesGeneratorConfiguration
{
    @NotNull
    private File outputDir;
    @NotNull
    private IfDevRegistry registry;

    public JavaIfDevSourcesGeneratorConfiguration(@NotNull File outputDir,
                                                  @NotNull IfDevRegistry registry)
    {
        this.outputDir = outputDir;
        this.registry = registry;
    }

    @NotNull
    @Argument(metaVar = "OUTPUT_DIR", required = true)
    File getOutputDir()
    {
        return outputDir;
    }

    @NotNull
    IfDevRegistry getRegistry()
    {
        return registry;
    }
}
