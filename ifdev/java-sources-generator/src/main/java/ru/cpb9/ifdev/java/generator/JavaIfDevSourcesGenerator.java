package ru.cpb9.ifdev.java.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.generation.GenerationException;
import ru.cpb9.generation.Generator;
import ru.cpb9.generator.java.ast.*;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevAllParameters;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevDeepAllParameters;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Optional<AbstractJavaBaseClass> javaClassOptional =
                type.accept(new IfDevTypeVisitor<Optional<AbstractJavaBaseClass>, RuntimeException>()
                {
                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevPrimitiveType primitiveType) throws RuntimeException
                    {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevNativeType nativeType) throws
                            RuntimeException
                    {
                        return Optional.empty();
                    }

                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevSubType subType) throws RuntimeException
                    {
                        IfDevType baseType = subType.getBaseType().getObject();
                        JavaClass javaClass = JavaClass.newBuilder(subType.getNamespace().getFqn().asString(),
                                classNameFromTypeName(subType.getName().asString()))
                                .extendsClass(getJavaTypeForIfDevType(baseType, false))
                                .build();
                        return Optional.of(javaClass);
                    }

                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevEnumType enumType) throws RuntimeException
                    {
                        JavaEnum javaEnum = JavaEnum.newBuilder(enumType.getNamespace().getFqn().asString(),
                                classNameFromEnumName(enumType.getName().asString())).build();
                        return Optional.of(javaEnum);
                    }

                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
                    {
                        JavaClass javaClass = JavaClass.newBuilder(arrayType.getNamespace().getFqn().asString(),
                                classNameFromArrayType(arrayType))
                                .genericArgument("T")
                                .extendsClass("ifdev.Array", new JavaTypeApplication("T"))
                                .constuctor(Collections.emptyList(),
                                        new JavaSuperCallStatement(
                                                new JavaLongExpr(arrayType.getSize().getMinLength()),
                                                new JavaLongExpr(arrayType.getSize().getMaxLength())))
                                .build();
                        return Optional.of(javaClass);
                    }

                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevStructType structType) throws RuntimeException
                    {
                        JavaClass javaClass = JavaClass.newBuilder(structType.getNamespace().getFqn().asString(),
                                classNameFromTypeName(structType.getName().asString()))
                                .constuctor(
                                        structType.getFields().stream().map(f ->
                                                new JavaMethodArgument(
                                                        getJavaTypeForIfDevType(f.getType().getObject(), false),
                                                        f.getName().asString())).collect(Collectors.toList()),
                                        structType.getFields().stream().map(f ->
                                                new JavaAssignStatement(
                                                        new JavaVarExpr("this." + f.getName().asString()),
                                                        new JavaVarExpr(f.getName().asString())))
                                                .collect(Collectors.toList()))
                                .build();
                        final List<JavaField> fields = javaClass.getFields();
                        final List<JavaClassMethod> methods = javaClass.getMethods();
                        structType.getFields().stream().forEach((f) ->
                        {
                            JavaType javaType = getJavaTypeForIfDevType(f.getType().getObject(), false);
                            String fieldName = f.getName().asString();
                            JavaField field = new JavaField(JavaVisibility.PRIVATE, false, false, javaType,
                                    fieldName);
                            methods.add(new JavaClassMethod(JavaVisibility.PUBLIC, false, javaType, "get" +
                                    StringUtils.capitalize(fieldName), Collections.emptyList(),
                                    Lists.newArrayList(new JavaReturnStatement(new JavaVarExpr(fieldName)))));
                            fields.add(field);
                        });
                        return Optional.of(javaClass);
                    }

                    @Override
                    @NotNull
                    public Optional<AbstractJavaBaseClass> visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
                    {
                        return Optional.empty();
                    }
                });
        if (javaClassOptional.isPresent())
        {
            generateJavaClass(javaClassOptional.get());
        }
    }

    @NotNull
    private String classNameFromEnumName(@NotNull String enumName)
    {
        return classNameFromTypeName(enumName);
    }

    @NotNull
    private String classNameFromTypeName(@NotNull String typeName)
    {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, typeName);
    }

    @NotNull
    private JavaType getJavaTypeForIfDevType(@NotNull IfDevType type, boolean genericUse)
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
                                return genericUse ? JavaType.Std.BYTE : JavaType.Primitive.BYTE;
                            case 16:
                                return genericUse ? JavaType.Std.SHORT : JavaType.Primitive.SHORT;
                            case 32:
                                return genericUse ? JavaType.Std.INTEGER : JavaType.Primitive.INT;
                            case 64:
                                return genericUse ? JavaType.Std.LONG : JavaType.Primitive.LONG;
                            default:
                                throw new AssertionError();
                        }
                    case UINT:
                        switch ((byte) primitiveType.getBitLength())
                        {
                            case 8:
                                return genericUse ? JavaType.Std.SHORT : JavaType.Primitive.SHORT;
                            case 16:
                                return genericUse ? JavaType.Std.INTEGER : JavaType.Primitive.INT;
                            case 32:
                                return genericUse ? JavaType.Std.LONG : JavaType.Primitive.LONG;
                            case 64:
                                return JavaType.Std.BIG_INTEGER;
                            default:
                                throw new AssertionError();
                        }
                    case FLOAT:
                        switch ((byte) primitiveType.getBitLength())
                        {
                            case 32:
                                return genericUse ? JavaType.Std.FLOAT : JavaType.Primitive.FLOAT;
                            case 64:
                                return genericUse ? JavaType.Std.DOUBLE : JavaType.Primitive.DOUBLE;
                            default:
                                throw new AssertionError();
                        }
                    case BOOL:
                        return genericUse ? JavaType.Std.BOOLEAN : JavaType.Primitive.BOOLEAN;
                }
                throw new AssertionError();
            }

            @Override
            public JavaType visit(@NotNull IfDevNativeType nativeType) throws RuntimeException
            {
                return new JavaTypeApplication("ifdev.Ber");
            }

            @Override
            public JavaType visit(@NotNull IfDevSubType subType) throws RuntimeException
            {
                return new JavaTypeApplication(subType.getNamespace().getFqn().asString() + "." + classNameFromTypeName(
                        subType.getName().asString()));
            }

            @Override
            public JavaType visit(@NotNull IfDevEnumType enumType) throws RuntimeException
            {
                return new JavaTypeApplication(enumType.getNamespace().getFqn().asString() + "." +
                        classNameFromTypeName(enumType.getName().asString()));
            }

            @Override
            public JavaType visit(@NotNull IfDevArrayType arrayType) throws RuntimeException
            {
                return new JavaTypeApplication(
                        arrayType.getNamespace().getFqn().asString() + "." + classNameFromArrayType(
                                arrayType), getJavaTypeForIfDevType(arrayType.getBaseType().getObject(), true));
            }

            @Override
            public JavaType visit(@NotNull IfDevStructType structType) throws RuntimeException
            {
                return new JavaTypeApplication(structType.getNamespace().getFqn().asString() + "." +
                        classNameFromTypeName(structType.getName().asString()));
            }

            @Override
            public JavaType visit(@NotNull IfDevAliasType typeAlias) throws RuntimeException
            {
                return getJavaTypeForIfDevType(typeAlias.getType().getObject(), genericUse);
            }
        });
    }

    @NotNull
    private String classNameFromArrayType(@NotNull IfDevArrayType arrayType)
    {
        return "Array" + (arrayType.isFixedSize()
                ? arrayType.getSize().getMinLength()
                : (arrayType.getSize().getMaxLength() == 0 ? "" : arrayType.getSize().getMinLength() + "_" + arrayType.getSize().getMaxLength()));
    }

    private void generateUnit(@NotNull IfDevUnit unit)
    {
        JavaClass javaClass =
                JavaClass.newBuilder(fqnToJavaPackage(unit.getNamespace().getFqn().asString()), unitNameToClassName(
                        unit.getName().asString())).build();
        javaClass.getFields().add(new JavaField(JavaVisibility.PUBLIC, true, true,
                new JavaTypeApplication(Optional.class, new JavaTypeApplication(String.class)), "DISPLAY",
                unit.getDisplay().isPresent() ?
                        new JavaClassMethodCallExpr(new JavaTypeApplication(Optional.class), "of",
                                new JavaStringExpr(unit.getDisplay().get())) :
                        new JavaClassMethodCallExpr(Optional.class, "empty")));
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
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, unitName) + "Unit";
    }

    private void generateJavaClass(@NotNull AbstractJavaBaseClass javaClass)
    {
        File dir = createPathIfNotExistsForPackage(javaClass.getPackage());
        File javaFile = new File(dir, javaClass.getName() + ".java");
        try (OutputStream os = new FileOutputStream(javaFile))
        {
            try (OutputStreamWriter writer = new OutputStreamWriter(os))
            {
                JavaGeneratorState state = new JavaGeneratorState(writer);
                writer.append("package ").append(javaClass.getPackage()).append(";");
                state.eol();
                Map<String, String> imports = new HashMap<>();
                new ImportsExporter(imports, javaClass.getPackage()).export(javaClass);
                if (!imports.isEmpty())
                {
                    for (Map.Entry<String, String> _import : imports.entrySet())
                    {
                        writer.append("import ").append(_import.getValue()).append('.').append(_import.getKey()).append(';');
                        state.eol();
                    }
                }
                state.eol();
                javaClass.generate(state, writer);
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
        JavaClass.Builder componentClassBuilder = JavaClass.newBuilder(component.getNamespace().getFqn().asString(), component.getName().asString());
        component.getMessages().stream().forEach(m ->
        {
            JavaClass.Builder messageClassBuilder = JavaClass.newBuilder("", classNameFromMessageName(m.getName().asString()));
            for (IfDevMessageParameter param : m.getParameters())
            {
                messageClassBuilder.privateField(getJavaTypeForIfDevType(component.getTypeForParameter(param), false), getFieldNameForParameter(param));
            }
            componentClassBuilder.innerClass(messageClassBuilder.build());
        });
        generateJavaClass(componentClassBuilder.build());
    }

    @NotNull
    private static String getFieldNameForParameter(@NotNull IfDevMessageParameter parameter)
    {
        return parameter.getValue().toLowerCase().replaceAll("[\\.\\[\\]]", "_");
    }

    private Stream<IfDevMessageParameter> getParametersStreamForParameter(@NotNull IfDevMessageParameter parameter, @NotNull IfDevComponent component)
    {
        /*TODO: implement * and *.* parameters
        if (parameter.equals(ImmutableIfDevDeepAllParameters.INSTANCE))
        {

        }
        else if (parameter.equals(ImmutableIfDevAllParameters.INSTANCE))
        {
            Optional<IfDevMaybeProxy<IfDevType>> baseTypeOptional = component.getBaseType();
            if (baseTypeOptional.isPresent())
            {
                baseTypeOptional.get().getObject().
            }
        }*/
        return Stream.of(parameter);
    }

    @NotNull
    private String classNameFromMessageName(@NotNull String name)
    {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    }

    @NotNull
    @Override
    public Optional<JavaIfDevSourcesGeneratorConfiguration> getConfiguration()
    {
        return Optional.of(config);
    }
}
