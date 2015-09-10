package ru.cpb9.ifdev.model.domain.impl.proxy;

import com.google.common.base.Preconditions;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.*;
import ru.cpb9.ifdev.model.domain.proxy.IfDevProxyResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import ru.cpb9.ifdev.model.domain.type.IfDevArrayType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Stream.concat;
import static ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy.proxy;

/**
 * @author Artem Shein
 */
public class FindExistingIfDevProxyResolver implements IfDevProxyResolver
{
    public static final String INVALID_MANGLED_ARRAY_NAME = "invalid mangled array name";

    @NotNull
    @Override
    public <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry,
                                                                          @NotNull URI uri,
                                                                          @NotNull Class<T> cls)
    {
        Iterator<String> iter = IfDevUtils.getUriParts(uri).iterator();
        IfDevResolvingResult<IfDevReferenceable> current = SimpleIfDevResolvingResult.immutableEmpty();
        while (iter.hasNext())
        {
            String part = iter.next();
            Optional<IfDevReferenceable> resolvedObject = current.getResolvedObject();
            if (resolvedObject.isPresent())
            {
                current = Preconditions.checkNotNull(resolvedObject.get().<IfDevResolvingResult<IfDevReferenceable>, RuntimeException>accept(
                        new ResolveIfDevReferenceableVisitor(registry,
                        ImmutableIfDevName.newInstanceFromMangledName(part))));
            }
            else
            {
                current = SimpleIfDevResolvingResult.newInstance(registry.getRootNamespaces().stream()
                        .filter(n -> n.getName().asString().equals(part)).findAny()
                        .map(v -> (IfDevReferenceable) v));
            }
            if (!current.getResolvedObject().isPresent())
            {
                return SimpleIfDevResolvingResult.newInstance(Optional.empty());
            }
        }
        return SimpleIfDevResolvingResult
                .newInstance(current.getResolvedObject().map(o -> cls.isInstance(o) ? cls.cast(o) : null));
    }

    private static class ResolveIfDevReferenceableVisitor
            implements IfDevReferenceableVisitor<IfDevResolvingResult<IfDevReferenceable>, RuntimeException>
    {
        @NotNull
        private final IfDevRegistry registry;
        @NotNull
        private final IfDevName part;

        public ResolveIfDevReferenceableVisitor(@NotNull IfDevRegistry registry, @NotNull IfDevName part)
        {
            this.registry = registry;
            this.part = part;
        }

        @Override
        @NotNull
        public IfDevResolvingResult<IfDevReferenceable> visit(@NotNull IfDevNamespace namespace)
        {
            Optional<IfDevReferenceable> resolvedObject = concat(
                    concat(
                            concat(namespace.getSubNamespaces().stream(), namespace.getUnits().stream()),
                            namespace.getTypes().stream()),
                    namespace.getComponents().stream())
                    .filter(n -> n.getName().equals(part)).findAny();
            if (resolvedObject.isPresent())
            {
                return SimpleIfDevResolvingResult.newInstance(resolvedObject);
            }
            String partString = part.asString();
            if (partString.startsWith("["))
            {
                Preconditions.checkState(partString.endsWith("]"), INVALID_MANGLED_ARRAY_NAME);
                String innerPart = partString.substring(1, partString.length() - 1);
                int index = innerPart.lastIndexOf(",");
                long minLength = 0, maxLength = 0;
                if (index != -1)
                {
                    String sizePart = innerPart.substring(index + 1);
                    if (sizePart.contains(".."))
                    {
                        String[] parts = sizePart.split(Pattern.quote(".."));
                        minLength = Long.parseLong(parts[0]);
                        maxLength = "*".equals(parts[1]) ? 0 : Long.parseLong(parts[1]);
                    }
                    else
                    {
                        minLength = maxLength = Long.parseLong(sizePart);
                    }
                }
                final long finalMinLength = minLength;
                final long finalMaxLength = maxLength;
                IfDevArrayType newArrayType = namespace.getTypes().stream().filter(t -> t.getName().equals(part))
                        .filter(IfDevArrayType.class::isInstance).map(IfDevArrayType.class::cast).findAny()
                        .orElseGet(() -> SimpleIfDevArrayType.newInstance(
                                Optional.of(part),
                                namespace,
                                proxy(namespace.getFqn(),
                                        ImmutableIfDevName.newInstanceFromSourceName(innerPart.substring(0, index == -1? innerPart.length() : index))),
                                Optional.<String>empty(),
                                ImmutableArraySize.newInstance(finalMinLength, finalMaxLength)));
                IfDevResolvingResult<IfDevType> resolvedBaseType = newArrayType.getBaseType()
                        .resolve(registry, IfDevType.class);
                if (resolvedBaseType.getResolvedObject().isPresent())
                {
                    List<IfDevType> types = new ArrayList<>(namespace.getTypes());
                    types.add(newArrayType);
                    namespace.setTypes(types);
                    return SimpleIfDevResolvingResult.newInstance(Optional.of(newArrayType));
                }
            }
            return SimpleIfDevResolvingResult.immutableEmpty();
        }

        @Override
        @NotNull
        public IfDevResolvingResult<IfDevReferenceable> visit(@NotNull IfDevType type)
        {
            return SimpleIfDevResolvingResult.immutableEmpty();
        }

        @Override
        @NotNull
        public IfDevResolvingResult<IfDevReferenceable> visit(@NotNull IfDevComponent component) throws RuntimeException
        {
            return SimpleIfDevResolvingResult.immutableEmpty();
        }

        @Override
        @NotNull
        public IfDevResolvingResult<IfDevReferenceable> visit(@NotNull IfDevUnit unit) throws RuntimeException
        {
            return SimpleIfDevResolvingResult.immutableEmpty();
        }
    }
}
