package ru.cpb9.geotarget;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.java.generator.JavaIfDevSourcesGenerator;
import ru.cpb9.ifdev.java.generator.JavaIfDevSourcesGeneratorConfiguration;

import java.io.File;

/**
 * @author Artem Shein
 */
public final class ModelSourceGenerator
{
    public static void main(String[] args)
    {
        JavaIfDevSourcesGeneratorConfiguration config = new JavaIfDevSourcesGeneratorConfiguration(new File("."), ModelRegistry.getRegistry());
        generate(config);
    }

    public static void generate(@NotNull JavaIfDevSourcesGeneratorConfiguration config)
    {
        JavaIfDevSourcesGenerator generator = new JavaIfDevSourcesGenerator(config);
        generator.generate();
    }

    private ModelSourceGenerator()
    {

    }
}
