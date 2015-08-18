package ru.cpb9.ifdev.java.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.generation.GenerationException;
import ru.cpb9.generation.Generator;
import ru.cpb9.generator.java.ast.*;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import ru.cpb9.ifdev.model.domain.type.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
                return Optional.empty();
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevEnumType enumType) throws RuntimeException
            {
                return Optional.empty();
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
                    //JavaType javaType = getJavaTypeForIfDevType(f.getType().getObject());
                    //methods.add(new JavaClassMethod());
                });
                return Optional.of(javaClass);
            }

            @Override
            @NotNull
            public Optional<JavaClass> visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
            {
                return Optional.empty();
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
        JavaClass javaClass = new JavaClass(fqnToJavaPackage(unit.getNamespace().getFqn().asString()), unitNameToClassName(
                unit.getName().asString()));
        javaClass.getFields().add(new JavaField(JavaVisibility.PUBLIC, true, true, new JavaTypeApplication(Optional.class, new JavaTypeApplication(String.class)), "DISPLAY", unit.getDisplay().isPresent()? new JavaClassMethodCallExpr(new JavaTypeApplication(Optional.class), "of", new JavaStringExpr(unit.getDisplay().get())) : new JavaClassMethodCallExpr(Optional.class, "empty")));
        generateJavaClass(javaClass);
    }

    @NotNull
    private String fqnToJavaPackage(@NotNull String fqn)
    {
        return fqn.toLowerCase();
    }

    @NotNull
    private String unitNameToClassName(@NotNull String unitName)
    {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, unitName);
    }

    private void generateJavaClass(@NotNull JavaClass javaClass)
    {
        File dir = createPathIfNotExistsForPackage(javaClass.getPackage());
        File javaFile = new File(dir, javaClass.getName() + ".java");
        try (OutputStream os = new FileOutputStream(javaFile))
        {
            try (OutputStreamWriter writer = new OutputStreamWriter(os))
            {
                javaClass.generate(writer);
            }
        }
        catch (Exception e)
        {
            throw new GenerationException(e);
        }
    }

    @NotNull
    private File createPathIfNotExistsForPackage(@NotNull String _package)
    {
        File dir = new File(config.getOutputDir(), _package.replaceAll(Pattern.quote("."), "/"));
        if (!dir.exists())
        {
            Preconditions.checkState(dir.mkdirs());
        }
        return dir;
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
