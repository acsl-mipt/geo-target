package ru.cpb9.geotarget;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.provider.IfDevSqlProvider;
import ru.cpb9.ifdev.model.provider.IfDevSqlProviderConfiguration;
import ru.cpb9.ifdev.parser.IfDevParser;

import java.net.URL;
import java.util.function.Supplier;

/**
 * @author Artem Shein
 */
public abstract class ModelRegistry
{
    private static final URL RESOURCE = Resources.getResource("ru/cpb9/ifdev/local.sqlite");
    private static volatile IfDevRegistry registry;

    private static Supplier<IfDevRegistry> newSqlProvider()
    {
        IfDevSqlProvider provider = new IfDevSqlProvider();
        IfDevSqlProviderConfiguration config = new IfDevSqlProviderConfiguration();
        config.setConnectionUrl("jdbc:sqlite::resource:" + RESOURCE);
        return () -> provider.provide(config);
    }

    private static Supplier<IfDevRegistry> newResourceProvider()
    {
        IfDevParser parser = new IfDevParser();
        throw new NotImplementedException("provider not implemented");
    }

    @NotNull
    public static IfDevRegistry getRegistry()
    {
        if (registry == null)
        {
            synchronized (ModelRegistry.class)
            {
                if (registry == null)
                {
                    registry = Preconditions.checkNotNull(newSqlProvider().get());
                }
            }
        }
        return registry;
    }
}
