package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.IfDevNamespaceAware;
import ru.cpb9.ifdev.model.domain.IfDevOptionalNameAndOptionalInfoAware;
import ru.cpb9.ifdev.model.domain.IfDevReferenceableVisitor;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevType extends IfDevReferenceable, IfDevOptionalNameAndOptionalInfoAware, IfDevNamespaceAware
{
    enum TypeKind
    {
        INT("int"), UINT("uint"), FLOAT("float"), BOOL("bool");

        @NotNull
        private static final Map<String, TypeKind> typeKindByName = new HashMap<String, TypeKind>(){{
            put(INT.getName(), INT);
            put(UINT.getName(), UINT);
            put(FLOAT.getName(), FLOAT);
            put(BOOL.getName(), BOOL);
        }};
        @NotNull
        private final String name;

        @NotNull
        public static Optional<TypeKind> forName(@NotNull String name)
        {
            return Optional.ofNullable(typeKindByName.get(name));
        }

        TypeKind(@NotNull String name)
        {
            this.name = name;
        }

        @NotNull
        public String getName()
        {
            return name;
        }
    }

    <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E;

    @Nullable
    default <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
