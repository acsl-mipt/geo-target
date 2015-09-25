package ru.cpb9.geotarget;

import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.decode.java.generator.JavaDecodeSourcesGenerator;
import ru.mipt.acsl.decode.java.generator.JavaDecodeSourcesGeneratorConfiguration;

import java.io.File;

/**
 * @author Artem Shein
 */
public final class ModelSourceGenerator
{
    public static void main(String[] args)
    {
        JavaDecodeSourcesGeneratorConfiguration config = new JavaDecodeSourcesGeneratorConfiguration(new File("."), ModelRegistry.getRegistry());
        generate(config);
    }

    public static void generate(@NotNull JavaDecodeSourcesGeneratorConfiguration config)
    {
        JavaDecodeSourcesGenerator generator = new JavaDecodeSourcesGenerator(config);
        generator.generate();
    }

    private ModelSourceGenerator()
    {

    }
}
