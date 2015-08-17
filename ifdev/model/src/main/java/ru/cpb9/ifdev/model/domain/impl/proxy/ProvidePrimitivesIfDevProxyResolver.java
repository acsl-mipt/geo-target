package ru.cpb9.ifdev.model.domain.impl.proxy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.*;
import ru.cpb9.ifdev.model.domain.proxy.IfDevProxyResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import ru.cpb9.ifdev.model.domain.type.IfDevPrimitiveType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.*;

/**
 * @author Artem Shein
 */
public class ProvidePrimitivesIfDevProxyResolver implements IfDevProxyResolver
{
    @NotNull
    private final Map<IfDevName, IfDevPrimitiveType> primitiveTypeByNameMap = new HashMap<>();
    @NotNull
    @Override
    public <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry,
                                                                          @NotNull URI uri,
                                                                          @NotNull Class<T> cls)
    {
        List<String> parts = IfDevUtils.getUriParts(uri);
        if (parts.size() == 2 && parts.get(0).equals(IfDevConstants.SYSTEM_NAMESPACE_NAME.asString()))
        {
            Optional<IfDevNamespace> namespaceOptional = IfDevUtils.getNamespaceByFqn(registry,
                    ImmutableIfDevFqn.newInstance(Lists.newArrayList(IfDevConstants.SYSTEM_NAMESPACE_NAME)));
            Preconditions.checkState(namespaceOptional.isPresent(), "system namespace not found");
            String typeName = parts.get(1);
            if (typeName.contains(":"))
            {
                int index = typeName.indexOf(":");
                String typeKind = typeName.substring(0, index);
                long bitLength = Long.parseLong(typeName.substring(index + 1));
                Optional<IfDevType.TypeKind> typeKindOptional = IfDevType.TypeKind.forName(typeKind);
                if (typeKindOptional.isPresent())
                {
                    IfDevName name = ImmutableIfDevName.newInstanceFromMangledName(typeName);
                    IfDevNamespace namespace = namespaceOptional.get();
                    IfDevPrimitiveType primitiveType = primitiveTypeByNameMap.computeIfAbsent(name,
                            (nameKey) -> SimpleIfDevPrimitiveType
                                    .newInstance(Optional.of(nameKey), namespace, typeKindOptional.get(),
                                            bitLength,
                                            Optional.<String>empty()));
                    if (!namespace.getTypes().stream().filter(t -> t.getName().equals(primitiveType.getName()))
                            .findAny().isPresent())
                    {
                        List<IfDevType> types = new ArrayList<>();
                        types.add(primitiveType);
                        namespace.setTypes(types);
                    }
                    return SimpleIfDevResolvingResult.newInstance(Optional.of(primitiveType)
                            .filter(cls::isInstance).map(cls::cast));
                }
            }
        }
        return SimpleIfDevResolvingResult.immutableEmpty();
    }
}
