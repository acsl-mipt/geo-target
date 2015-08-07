package ru.cpb9.geotarget;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.provider.IfDevSqlProvider;
import ru.cpb9.ifdev.model.provider.IfDevSqlProviderConfiguration;

import java.net.URL;

/**
 * @author Artem Shein
 */
public abstract class ModelRegistry
{
    private static final URL RESOURCE = Resources.getResource("ru/cpb9/ifdev/local.sqlite");
    private static volatile IfDevRegistry registry;

    @NotNull
    public static IfDevRegistry getRegistry()
    {
        if (registry == null)
        {
            synchronized (ModelRegistry.class)
            {
                if (registry == null)
                {
                    IfDevSqlProvider provider = new IfDevSqlProvider();
                    IfDevSqlProviderConfiguration config = new IfDevSqlProviderConfiguration();
                    config.setConnectionUrl("jdbc:sqlite::resource:" + RESOURCE);
                    registry = Preconditions.checkNotNull(provider.provide(config));
                }
            }
        }
        return registry;
    }
}
