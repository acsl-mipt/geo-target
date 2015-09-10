package ru.cpb9.ifdev.model.domain;

import com.google.common.base.Preconditions;
import ru.cpb9.common.Either;
import ru.cpb9.ifdev.model.domain.impl.IfDevParameterWalker;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public interface IfDevComponent extends IfDevOptionalInfoAware, IfDevReferenceable, IfDevNamespaceAware
{
    @NotNull
    Optional<IfDevMaybeProxy<IfDevType>> getBaseType();

    @NotNull
    Set<IfDevMaybeProxy<IfDevComponent>> getSubComponents();

    @NotNull
    List<IfDevCommand> getCommands();

    @NotNull
    List<IfDevMessage> getMessages();

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }

    @NotNull
    default SortedSet<IfDevComponent> getAllSubComponentsOrdered()
    {
        SortedSet<IfDevComponent> components = new TreeSet<>();
        components.addAll(getSubComponents().stream().map(IfDevMaybeProxy::getObject).collect(Collectors.toSet()));
        components.addAll(
                components.stream().flatMap(c -> c.getAllSubComponentsOrdered().stream()).collect(Collectors.toSet()));
        return components;
    }

    @NotNull
    default IfDevType getTypeForParameter(@NotNull IfDevMessageParameter parameter)
    {
        Optional<IfDevMaybeProxy<IfDevType>> baseType = getBaseType();
        Preconditions.checkState(baseType.isPresent());
        IfDevType result = baseType.get().getObject();
        IfDevParameterWalker walker = new IfDevParameterWalker(parameter);
        while (walker.hasNext())
        {
            Either<String, Integer> token = walker.next();
            result = Preconditions.checkNotNull(result)
                    .accept(new TokenWalker(token));
        }
        return Preconditions.checkNotNull(result);
    }

    class TokenWalker implements IfDevTypeVisitor<IfDevType, RuntimeException>
    {
        @NotNull
        private final Either<String, Integer> token;

        public TokenWalker(@NotNull Either<String, Integer> token)
        {
            this.token = token;
        }

        @Override
        public IfDevType visit(@NotNull IfDevPrimitiveType primitiveType) throws RuntimeException
        {
            return null;
        }

        @Override
        public IfDevType visit(@NotNull IfDevNativeType nativeType) throws RuntimeException
        {
            return null;
        }

        @Override
        public IfDevType visit(@NotNull IfDevSubType subType) throws RuntimeException
        {
            return subType.getBaseType().getObject().accept(this);
        }

        @Override
        public IfDevType visit(@NotNull IfDevEnumType enumType) throws RuntimeException
        {
            return null;
        }

        @Override
        public IfDevType visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
        {
            Preconditions.checkState(token.isRight());
            return arrayType.getBaseType().getObject();
        }

        @Override
        public IfDevType visit(@NotNull IfDevStructType structType) throws RuntimeException
        {
            Preconditions.checkState(token.isLeft());
            String name = token.getLeft();
            return structType.getFields().stream().filter(f -> f.getName().asString().equals(name))
                    .findAny().orElseThrow(() -> new AssertionError(String.format("Field '%s' not found in struct '%s'", name, structType))).getType().getObject();
        }

        @Override
        public IfDevType visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
        {
            return typeAlias.getType().getObject().accept(this);
        }
    }
}
