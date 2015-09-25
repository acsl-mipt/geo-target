package ru.cpb9.geotarget;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.acsl.decode.model.domain.DecodeReferenceable;
import ru.mipt.acsl.decode.model.domain.DecodeRegistry;
import ru.mipt.acsl.decode.model.domain.impl.proxy.SimpleDecodeDomainModelResolver;
import ru.mipt.acsl.decode.model.domain.proxy.DecodeResolvingResult;
import ru.mipt.acsl.decode.model.provider.DecodeSqlProvider;
import ru.mipt.acsl.decode.model.provider.DecodeSqlProviderConfiguration;
import ru.mipt.acsl.decode.parser.DecodeSourceProvider;
import ru.mipt.acsl.decode.parser.DecodeSourceProviderConfiguration;

import java.net.URL;
import java.util.function.Supplier;

/**
 * @author Artem Shein
 */
public abstract class ModelRegistry
{
    private static final URL RESOURCE = Resources.getResource("ru/cpb9/decode/local.sqlite");
    private static final Logger LOG = LoggerFactory.getLogger(ModelRegistry.class);
    private static volatile DecodeRegistry registry;

    private static Supplier<DecodeRegistry> newSqlProvider()
    {
        DecodeSqlProvider provider = new DecodeSqlProvider();
        DecodeSqlProviderConfiguration config = new DecodeSqlProviderConfiguration();
        config.setConnectionUrl("jdbc:sqlite::resource:" + RESOURCE);
        return () -> provider.provide(config);
    }

    private static Supplier<DecodeRegistry> newResourceProvider()
    {
        DecodeSourceProvider provider = new DecodeSourceProvider();
        DecodeSourceProviderConfiguration config = new DecodeSourceProviderConfiguration();
        config.setResourcePath("ru/cpb9/decode");
        return () -> provider.provide(config);
    }

    @NotNull
    public static DecodeRegistry getRegistry()
    {
        if (registry == null)
        {
            synchronized (ModelRegistry.class)
            {
                if (registry == null)
                {
                    DecodeRegistry newRegistry = Preconditions.checkNotNull(newResourceProvider().get());
                    DecodeResolvingResult<DecodeReferenceable> resolvingResult =
                            SimpleDecodeDomainModelResolver.newInstance().resolve(newRegistry);
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
