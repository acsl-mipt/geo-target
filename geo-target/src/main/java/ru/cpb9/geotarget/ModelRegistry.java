package ru.cpb9.geotarget;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevDomainModelResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import ru.cpb9.ifdev.model.provider.IfDevSqlProvider;
import ru.cpb9.ifdev.model.provider.IfDevSqlProviderConfiguration;
import ru.cpb9.ifdev.parser.IfDevSourceProvider;
import ru.cpb9.ifdev.parser.IfDevSourceProviderConfiguration;

import java.net.URL;
import java.util.function.Supplier;

/**
 * @author Artem Shein
 */
public abstract class ModelRegistry
{
    private static final Logger LOG = LoggerFactory.getLogger(ModelRegistry.class);
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
        IfDevSourceProvider provider = new IfDevSourceProvider();
        IfDevSourceProviderConfiguration config = new IfDevSourceProviderConfiguration();
        config.setResourcePath("ru/cpb9/ifdev");
        return () -> provider.provide(config);
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
                    IfDevRegistry newRegistry = Preconditions.checkNotNull(newResourceProvider().get());
                    IfDevResolvingResult<IfDevReferenceable> resolvingResult =
                            SimpleIfDevDomainModelResolver.newInstance().resolve(newRegistry);
                    if (resolvingResult.hasError())
                    {
                        resolvingResult.getMessages().stream().forEach(m -> LOG.error("{}", m.getText()));
                    }
                    registry = newRegistry;
                }
            }
        }
        return registry;
    }
}
