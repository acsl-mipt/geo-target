package ru.cpb9.ifdev.java.generator;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.generation.Generator;
import ru.cpb9.generator.java.ast.JavaClass;
import ru.cpb9.generator.java.ast.JavaClassMethod;
import ru.cpb9.generator.java.ast.JavaType;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import ru.cpb9.ifdev.model.domain.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class JavaIfDevSourcesGenerator implements Generator<JavaIfDevSourcesGeneratorConfiguration>
{
    @NotNull
    private final JavaIfDevSourcesGeneratorConfiguration config;

    public JavaIfDevSourcesGenerator(@NotNull JavaIfDevSourcesGeneratorConfiguration config)
    {
        this.config = config;
    }

    @Override
    public void generate()
    {
        generateNamespaces(config.getRegistry().getRootNamespaces());
    }

    private void generateNamespaces(@NotNull List<IfDevNamespace> namespaces)
    {
        namespaces.stream().forEach(this::generateNamespace);
    }

    private void generateNamespace(@NotNull IfDevNamespace namespace)
    {
        generateNamespaces(namespace.getSubNamespaces());
        namespace.getComponents().stream().forEach(this::generateComponent);
        namespace.getUnits().stream().forEach(this::generateUnit);
        namespace.getTypes().stream().forEach(this::generateType);
    }

    private void generateType(@NotNull IfDevType type)
    {
        type.accept(new IfDevTypeVisitor<Optional<JavaClass>, RuntimeException>()
        {
            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevPrimitiveType primitiveType) throws RuntimeException
            {
                return Optional.empty();
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevSubType subType) throws RuntimeException
            {
                return null;
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevEnumType enumType) throws RuntimeException
            {
                return null;
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
            {
                return Optional.empty();
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevStructType structType) throws RuntimeException
            {

                JavaClass javaClass = new JavaClass(structType.getNamespace().getFqn().toString(),
                        structType.getName().asString());
                final List<JavaClassMethod> methods = new ArrayList<>();
                structType.getFields().stream().forEach((f) ->
                {
                    JavaType javaType = getJavaTypeForIfDevType(f.getType().getObject());
                    methods.add(new JavaClassMethod());
                });
                return Optional.of(javaClass);
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
            {
                return null;
            }
        });
    }

    @NotNull
    private JavaType getJavaTypeForIfDevType(@NotNull IfDevType type)
    {
        return type.accept(new IfDevTypeVisitor<JavaType, RuntimeException>()
        {
            @Override
            public JavaType visit(@NotNull IfDevPrimitiveType primitiveType) throws RuntimeException
            {
                switch (primitiveType.getKind())
                {
                case INT:
                    switch ((byte) primitiveType.getBitLength())
                    {
                        case 8:
                            return JavaType.Primitive.BYTE;
                        case 16:
                            return JavaType.Primitive.SHORT;
                        case 32:
                            return JavaType.Primitive.INT;
                        case 64:
                            return JavaType.Primitive.LONG;
                        default:
                            throw new AssertionError();
                    }
                case UINT:
                    switch ((byte) primitiveType.getBitLength())
                    {
                        case 8:
                            return JavaType.Primitive.SHORT;
                        case 16:
                            return JavaType.Primitive.INT;
                        case 32:
                            return JavaType.Primitive.LONG;
                        case 64:
                            return JavaType.Std.BIG_INTEGER;
                        default:
                            throw new AssertionError();
                    }
                case FLOAT:
                    switch ((byte) primitiveType.getBitLength())
                    {
                    case 32:
                        return JavaType.Primitive.FLOAT;
                    case 64:
                        return JavaType.Primitive.DOUBLE;
                    default:
                        throw new AssertionError();
                    }
                case BOOL:
                    return JavaType.Primitive.BOOLEAN;
                }
                throw new AssertionError();
            }

            @Override
            public JavaType visit(@NotNull IfDevSubType subType) throws RuntimeException
            {
                return null;
            }

            @Override
            public JavaType visit(@NotNull IfDevEnumType enumType) throws RuntimeException
            {
                return null;
            }

            @Override
            public JavaType visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
            {
                return null;
            }

            @Override
            public JavaType visit(@NotNull IfDevStructType structType) throws RuntimeException
            {
                return null;
            }

            @Override
            public JavaType visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
            {
                return getJavaTypeForIfDevType(typeAlias.getType().getObject());
            }
        });
    }

    private void generateUnit(@NotNull IfDevUnit unit)
    {

    }

    private void generateComponent(@NotNull IfDevComponent component)
    {

    }

    @NotNull
    @Override
    public Optional<JavaIfDevSourcesGeneratorConfiguration> getConfiguration()
    {
        return Optional.of(config);
    }
}
