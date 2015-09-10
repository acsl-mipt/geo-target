package ru.cpb9.ifdev.model.domain.impl.proxy;

import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.*;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevResolvingResult;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Artem Shein
 */
public final class SimpleIfDevDomainModelResolver implements IfDevDomainModelResolver
{
    public static IfDevDomainModelResolver newInstance()
    {
        return new SimpleIfDevDomainModelResolver();
    }

    private SimpleIfDevDomainModelResolver()
    {

    }

    @Override
    public IfDevResolvingResult<IfDevReferenceable> resolve(@NotNull IfDevRegistry registry)
    {
        return registry.getRootNamespaces().stream().flatMap(namespace -> resolve(namespace, registry))
                .reduce(SimpleIfDevResolvingResult.newInstance(Optional.<IfDevReferenceable>empty()),
                        SimpleIfDevResolvingResult::merge);
    }

    @NotNull
    private static Stream<IfDevResolvingResult<IfDevReferenceable>> resolve(
            @NotNull IfDevNamespace namespace,
            @NotNull IfDevRegistry registry)
    {
        List<IfDevResolvingResult<IfDevReferenceable>> resultList = new ArrayList<>();
        resultList.addAll(namespace.getTypes().stream().map(type -> resolve(type, registry))
                .collect(Collectors.toList()));
        resultList.addAll(namespace.getSubNamespaces().stream().flatMap(
                subNamespace -> resolve(subNamespace, registry))
                .collect(Collectors.toList()));
        resultList.addAll(namespace.getComponents().stream().flatMap(component -> resolve(component, registry)).collect(
                Collectors.toList()));
        return resultList.stream();
    }

    @NotNull
    private static Stream<IfDevResolvingResult<IfDevReferenceable>> resolve(
            @NotNull IfDevComponent component,
            @NotNull IfDevRegistry registry)
    {
        List<IfDevResolvingResult<IfDevReferenceable>> resultList = new ArrayList<>();
        if (component.getBaseType().isPresent())
        {
            resultList.add(resolveWithTypeCheck(component.getBaseType().get(), registry, IfDevType.class));
            if (component.getBaseType().get().isResolved())
            {
                resultList.add(resolve(component.getBaseType().get().getObject(), registry));
            }
        }
        for (IfDevCommand command : component.getCommands())
        {
            for (IfDevCommandArgument argument : command.getArguments())
            {
                resultList.add(resolveWithTypeCheck(argument.getType(), registry, IfDevType.class));
                if (argument.getUnit().isPresent())
                {
                    resultList.add(resolveWithTypeCheck(argument.getUnit().get(), registry, IfDevUnit.class));
                }
            }
        }
        for (IfDevMaybeProxy<IfDevComponent> subComponent : component.getSubComponents())
        {
            resultList.add(resolveWithTypeCheck(subComponent, registry, IfDevComponent.class));
            if (subComponent.isResolved())
            {
                resultList.addAll(resolve(subComponent.getObject(), registry).collect(Collectors.toList()));
            }
        }
        return resultList.stream();
    }

    @SuppressWarnings("unchecked")
    private static <T extends IfDevReferenceable> IfDevResolvingResult<IfDevReferenceable>
    resolveWithTypeCheck(
            @NotNull IfDevMaybeProxy<T> maybeProxy, @NotNull IfDevRegistry registry,
            @NotNull Class<T> cls)
    {
        // it's Java...
        return (IfDevResolvingResult<IfDevReferenceable>)maybeProxy.resolve(registry, cls);
    }

    @NotNull
    private static IfDevResolvingResult<IfDevReferenceable> resolve(
            @NotNull IfDevType type,
            @NotNull IfDevRegistry registry)
    {
        List<IfDevResolvingResult<IfDevReferenceable>> resolvingResultList = new ArrayList<>();
        type.accept(new IfDevTypeResolveVisitor(registry, resolvingResultList));
        return resolvingResultList.stream().reduce(SimpleIfDevResolvingResult.newInstance(
                Optional.<IfDevReferenceable>empty()),
                SimpleIfDevResolvingResult::merge);
    }

    private static class IfDevTypeResolveVisitor implements IfDevTypeVisitor<Void,RuntimeException>
    {
        @NotNull
        private final IfDevRegistry registry;
        @NotNull
        private final List<IfDevResolvingResult<IfDevReferenceable>> resolvingResultList;

        public IfDevTypeResolveVisitor(@NotNull IfDevRegistry registry,
                                       @NotNull List<IfDevResolvingResult<IfDevReferenceable>> resolvingResultList)
        {
            this.registry = registry;
            this.resolvingResultList = resolvingResultList;
        }

        @Override
        public Void visit(@NotNull IfDevPrimitiveType baseType) throws RuntimeException
        {
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevNativeType nativeType) throws RuntimeException
        {
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevSubType subType) throws RuntimeException
        {
            resolvingResultList.add(resolveWithTypeCheck(subType.getBaseType(), registry, IfDevType.class));
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevEnumType enumType) throws RuntimeException
        {
            resolvingResultList.add(resolveWithTypeCheck(enumType.getBaseType(), registry, IfDevType.class));
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
        {
            resolvingResultList.add(resolveWithTypeCheck(arrayType.getBaseType(), registry, IfDevType.class));
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevStructType structType) throws RuntimeException
        {
            for (IfDevStructField field : structType.getFields())
            {
                resolvingResultList.add(resolveWithTypeCheck(field.getType(), registry, IfDevType.class));
                if (!field.getType().isProxy())
                {
                    resolvingResultList.add(resolve(field.getType().getObject(), registry));
                }
                if (field.getUnit().isPresent())
                {
                    resolvingResultList.add(resolveWithTypeCheck(field.getUnit().get(), registry, IfDevUnit.class));
                }
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
        {
            resolvingResultList.add(resolveWithTypeCheck(typeAlias.getType(), registry, IfDevType.class));
            return null;
        }
    }
}
