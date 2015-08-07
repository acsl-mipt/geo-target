package ru.cpb9.ifdev.java.generator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;

import java.io.File;

/**
 * @author Artem Shein
 */
public interface JavaIfDevSourcesGeneratorConfiguration
{
    @NotNull
    @Argument(metaVar = "OUTPUT_DIR", required = true)
    File getOutputDir();
}
