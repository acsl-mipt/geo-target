package ru.cpb9.ifdev.java.generator;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.code.generation.Generator;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class JavaIfDevSourcesGenerator implements Generator<JavaIfDevSourcesGeneratorConfiguration>
{
    @NotNull
    private final JavaIfDevSourcesGeneratorConfiguration config;

    public JavaIfDevSourcesGenerator(@NotNull JavaIfDevSourcesGeneratorConfiguration config)
    {
        this.config = config;
    }

    @Override
    public void generate()
    {

    }

    @NotNull
    @Override
    public Optional<JavaIfDevSourcesGeneratorConfiguration> getConfiguration()
    {
        return Optional.of(config);
    }
}
