package ru.cpb9.ifdev.idea.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.*;
import ru.cpb9.ifdev.model.domain.type.*;
import ru.cpb9.ifdev.parser.psi.*;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.modeling.ModelingMessage;
import ru.cpb9.ifdev.modeling.TransformationMessage;
import ru.cpb9.ifdev.modeling.TransformationResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.ifdev.parser.psi.IfDevUnit;

import java.util.*;
import java.util.stream.Collectors;

import static ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy.object;
import static ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy.proxy;

/**
 * @author Artem Shein
 */
public class IfDevFileProcessor
{
    @NotNull
    private final IfDevRegistry registry;
    @NotNull
    private final TransformationResult<IfDevRegistry> result;

    public static void notifyUser(@NotNull ModelingMessage msg)
    {
        Notifications.Bus.notify(new Notification(IfDevGenerateSqliteForIfDevSourcesAction.GROUP_DISPLAY_ID,
                msg.getLevel().name(), msg.getText(), getNotificationTypeForLevel(msg.getLevel())));
    }

    public IfDevFileProcessor(@NotNull IfDevRegistry registry, @NotNull TransformationResult<IfDevRegistry> result)
    {
        this.registry = registry;
        this.result = result;
    }

    public void process(@NotNull IfDevFile file)
    {
        List<PsiElement> elements = Lists.newArrayList(file.getNode().getPsi().getChildren());
        Iterator<PsiElement> it = elements.iterator();
        if (!it.hasNext())
        {
            return;
        }
        PsiElement namespaceDecl = it.next();
        if (!(namespaceDecl instanceof IfDevNamespaceDecl))
        {
            error("Expected namespace declaration, found '%s'", namespaceDecl);
            return;
        }
        String namespaceString = ((IfDevNamespaceDecl) namespaceDecl).getElementId().getText();
        IfDevNamespace namespace = IfDevRegistryUtils.getOrCreateNamespaceByFqn(registry, namespaceString);
        while (it.hasNext())
        {
            PsiElement element = it.next();

            if (element instanceof IfDevUnitDecl)
            {
                namespace.getUnits().add(
                        ImmutableIfDevUnit.newInstance(
                                ImmutableIfDevName.newInstanceFromSourceName(
                                        ((IfDevUnitDecl) element).getElementNameRule().getText()),
                                namespace,
                                getText(((IfDevUnitDecl) element).getStringValue()),
                                getText(((IfDevUnitDecl) element).getInfoString())));
            }
            else if (element instanceof IfDevTypeDecl)
            {
                namespace.getTypes().add(newType((IfDevTypeDecl) element, namespace));
            }
            else if (element instanceof IfDevComponentDecl)
            {
                processComponentDefinition((IfDevComponentDecl) element, namespace);
            }
            else if (element instanceof IfDevAliasDecl)
            {
                namespace.getTypes()
                        .add(ImmutableIfDevAliasType.newInstance(ImmutableIfDevName.newInstanceFromSourceName(
                                        ((IfDevAliasDecl) element).getElementId().getText()),
                                namespace,
                                makeProxyForTypeApplication(((IfDevAliasDecl) element).getTypeApplication(), namespace),
                                getText(((IfDevAliasDecl) element).getInfoString())));
            }
            else if (element instanceof PsiWhiteSpace)
            {
                // skip
            }
            else
            {
                error("Unexpected '%s'", element);
            }
        }
    }

    @NotNull
    private static NotificationType getNotificationTypeForLevel(@NotNull TransformationMessage.Level level)
    {
        switch (level)
        {
            case ERROR:
                return NotificationType.ERROR;
            case WARN:
                return NotificationType.WARNING;
            case NOTICE:
                return NotificationType.INFORMATION;
        }
        throw new AssertionError();
    }

    private void processComponentDefinition(@NotNull IfDevComponentDecl componentDecl,
                                            @NotNull IfDevNamespace namespace)
    {
        Optional<IfDevTypeDeclBody> typeDeclBodyOptional = Optional.ofNullable(
                componentDecl.getComponentBaseTypeDecl()).map(
                IfDevComponentBaseTypeDecl::getTypeDeclBody);
        final String name = componentDecl.getElementNameRule().getText();
        Optional<IfDevMaybeProxy<IfDevType>> baseType = Optional.empty();
        if (typeDeclBodyOptional.isPresent())
        {
            baseType = Optional.of(findExistingOrCreateType(Optional.<IfDevName>empty(),
                    typeDeclBodyOptional.get(),
                    namespace));
        }

        List<IfDevCommand> commands = componentDecl.getCommandDeclList().stream().map(commandDecl -> {
            Optional<IfDevCommandArgs> commandArgs = Optional.ofNullable(commandDecl.getCommandArgs());
            List<IfDevCommandArgument> commandArguments = new ArrayList<>();
            if (commandArgs.isPresent())
            {
                commandArguments = commandArgs.get().getCommandArgList().stream().map(arg -> {
                    IfDevTypeUnitApplication typeUnit = arg.getTypeUnitApplication();
                    IfDevUnit unit = typeUnit.getUnit();
                    return ImmutableIfDevCommandArgument.newInstance(ImmutableIfDevName.newInstanceFromSourceName(
                                    arg.getElementNameRule().getText()),
                            makeProxyForTypeApplication(typeUnit.getTypeApplication(), namespace),
                            Optional.ofNullable(unit).map(u -> getProxyFor(namespace.getFqn(),
                                    ImmutableIfDevName.newInstanceFromSourceName(u.getElementId().getText()))),
                            getText(arg.getInfoString()));
                }).collect(Collectors.toList());
            }
            return ImmutableIfDevCommand.newInstance(ImmutableIfDevName.newInstanceFromSourceName(
                    commandDecl.getElementNameRule().getText()),
                    Integer.parseInt(commandDecl.getNonNegativeNumber().getText()),
                    getText(commandDecl.getInfoString()), commandArguments);
        }).collect(Collectors.toList());

        IfDevComponent component = SimpleIfDevComponent.newInstance(
                ImmutableIfDevName.newInstanceFromSourceName(name),
                namespace, baseType, getText(componentDecl.getInfoString()),
                componentDecl.getSubcomponentDeclList().stream()
                        .map(subcomponentDecl -> IfDevFileProcessor.<IfDevComponent>getProxyFor(namespace.getFqn(),
                                ImmutableIfDevName
                                        .newInstanceFromSourceName(subcomponentDecl.getElementNameRule().getText())))
                        .collect(Collectors.toSet()), commands, new ArrayList<>());
        component.getMessages().addAll(componentDecl.getMessageDeclList().stream().map(messageDecl -> {
            IfDevStatusMessage statusMessage = messageDecl.getStatusMessage();
            IfDevEventMessage eventMessage = messageDecl.getEventMessage();
            IfDevDynamicStatusMessage dynamicStatusMessage = messageDecl.getDynamicStatusMessage();
            String messageSourceName = messageDecl.getElementNameRule().getText();
            Optional<String> infoOptional = getText(messageDecl.getInfoString());
            IfDevName messageName = ImmutableIfDevName.newInstanceFromSourceName(messageSourceName);
            int id = Integer.parseInt(messageDecl.getNonNegativeNumber().getText());
            if (statusMessage != null)
            {
                return ImmutableIfDevStatusMessage.newInstance(component, messageName, id, infoOptional,
                        getMessageParameters(statusMessage.getMessageParametersDecl()));
            }
            if (eventMessage != null)
            {
                return ImmutableIfDevEventMessage.newInstance(component, messageName, id, infoOptional,
                        getMessageParameters(eventMessage.getMessageParametersDecl()));
            }
            if (dynamicStatusMessage != null)
            {
                return ImmutableIfDevDynamicStatusMessage.newInstance(component, messageName, id, infoOptional,
                        getMessageParameters(dynamicStatusMessage.getMessageParametersDecl()));
            }
            throw new AssertionError();
        }).collect(Collectors.toList()));

        namespace.getComponents().add(component);
    }

    @NotNull
    private IfDevMaybeProxy<IfDevType> makeProxyForTypeApplication(
            @NotNull IfDevTypeApplication typeApplication,
            @NotNull IfDevNamespace namespace)
    {
        IfDevArrayTypeApplication arrayType = typeApplication.getArrayTypeApplication();
        IfDevPrimitiveTypeApplication primitiveType = typeApplication.getPrimitiveTypeApplication();
        IfDevElementId elementId = typeApplication.getElementId();
        if (elementId != null)
        {
            return getProxyFor(namespace.getFqn(), ImmutableIfDevName.newInstanceFromSourceName(elementId.getText()));
        }
        if (primitiveType != null)
        {
            return getProxyFor(primitiveType);
        }
        if (arrayType != null)
        {
            return getProxyFor(arrayType, namespace);
        }
        throw new AssertionError();
    }

    @NotNull
    private IfDevMaybeProxy<IfDevType> getProxyFor(@NotNull IfDevArrayTypeApplication arrayType,
                                                   @NotNull IfDevNamespace namespace)
    {
        return getProxyFor(getNamespaceFqnFor(arrayType, namespace),
                ImmutableIfDevName.newInstanceFromSourceName(arrayType.getText()));
    }

    @NotNull
    private IfDevMaybeProxy<IfDevType> getProxyFor(@NotNull IfDevTypeApplication typeApplication,
                                                   @NotNull IfDevNamespace namespace)
    {
        IfDevArrayTypeApplication arrayTypeApplication = typeApplication.getArrayTypeApplication();
        IfDevPrimitiveTypeApplication primitiveTypeApplication = typeApplication.getPrimitiveTypeApplication();
        IfDevElementId elementId = typeApplication.getElementId();
        if (arrayTypeApplication != null)
        {
            return getProxyFor(arrayTypeApplication, namespace);
        }
        if (primitiveTypeApplication != null)
        {
            return getProxyFor(primitiveTypeApplication);
        }
        if (elementId != null)
        {
            return getProxyFor(namespace.getFqn(), ImmutableIfDevName.newInstanceFromSourceName(elementId.getText()));
        }
        throw new AssertionError();
    }

    private static <T extends IfDevReferenceable> IfDevMaybeProxy<T> getProxyFor(@NotNull IfDevFqn namespaceFqn,
                                                                                 @NotNull IfDevName name)
    {
        return proxy(IfDevUriUtils.getUriForNamespaceAndName(namespaceFqn, name));
    }

    @NotNull
    private IfDevFqn getNamespaceFqnFor(@NotNull IfDevArrayTypeApplication arrayType, @NotNull IfDevNamespace namespace)
    {
        return getNamespaceFqnFor(arrayType.getTypeApplication(), namespace);
    }

    private IfDevFqn getNamespaceFqnFor(@NotNull IfDevTypeApplication typeApplication,
                                        @NotNull IfDevNamespace namespace)
    {
        IfDevArrayTypeApplication arrayType = typeApplication.getArrayTypeApplication();
        IfDevPrimitiveTypeApplication primitiveType = typeApplication.getPrimitiveTypeApplication();
        IfDevElementId elementId = typeApplication.getElementId();
        if (arrayType != null)
        {
            return getNamespaceFqnFor(arrayType, namespace);
        }
        if (primitiveType != null)
        {
            return ImmutableIfDevFqn.newInstance(Lists.newArrayList(IfDevConstants.SYSTEM_NAMESPACE_NAME));
        }
        if (elementId != null)
        {
            String name = elementId.getText();
            if (name.contains("."))
            {
                return ImmutableIfDevFqn.newInstanceFromSource(name.substring(0, name.lastIndexOf('.') - 1));
            }
            return namespace.getFqn();
        }
        throw new AssertionError();
    }

    @NotNull
    private IfDevMaybeProxy<IfDevType> getProxyFor(@NotNull IfDevPrimitiveTypeApplication primitiveType)
    {
        return getProxyFor(ImmutableIfDevFqn.newInstance(Lists.newArrayList(IfDevConstants.SYSTEM_NAMESPACE_NAME)),
                ImmutableIfDevName.newInstanceFromSourceName(primitiveType.getText()));
    }

    @NotNull
    private static List<IfDevMessageParameter> getMessageParameters(
            @NotNull IfDevMessageParametersDecl messageParametersDecl)
    {
        if (messageParametersDecl.getDeepAllParameters() != null)
        {
            return ImmutableList.of(ImmutableIfDevDeepAllParameters.INSTANCE);
        }
        if (messageParametersDecl.getAllParameters() != null)
        {
            return ImmutableList.of(ImmutableIfDevAllParameters.INSTANCE);
        }
        return messageParametersDecl.getParameterDeclList().stream()
                .map(param -> Optional.ofNullable(param.getParameterElement())
                        .map(e -> ImmutableIfDevMessageParameter.newInstance(e.getText())).orElse(ImmutableIfDevAllParameters.INSTANCE))
                        .collect(Collectors.toList());
    }

    @NotNull
    private IfDevMaybeProxy<IfDevType> findExistingOrCreateType(@NotNull Optional<IfDevName> name, @NotNull IfDevTypeDeclBody typeDeclBody,
                                           @NotNull IfDevNamespace namespace)
    {
        IfDevTypeApplication typeApplication = typeDeclBody.getTypeApplication();
        IfDevEnumTypeDecl enumType = typeDeclBody.getEnumTypeDecl();
        IfDevStructTypeDecl structType = typeDeclBody.getStructTypeDecl();
        if (typeApplication != null)
        {
            return getProxyFor(typeApplication, namespace);
        }
        if (enumType != null)
        {
            return object(newEnumType(name, enumType, namespace));
        }
        if (structType != null)
        {
            return object(newStructType(name, structType, namespace));
        }
        throw new AssertionError();
    }

    @NotNull
    private IfDevType newType(@NotNull Optional<IfDevName> name, @NotNull IfDevTypeDeclBody typeDeclBody,
                              @NotNull IfDevNamespace namespace)
    {
        IfDevTypeApplication typeApplication = typeDeclBody.getTypeApplication();
        IfDevEnumTypeDecl enumType = typeDeclBody.getEnumTypeDecl();
        IfDevStructTypeDecl structType = typeDeclBody.getStructTypeDecl();

        if (typeApplication != null)
        {
            return newSubTypeForTypeApplication(name, getText(typeDeclBody.getInfoString()), typeApplication, namespace);
        }
        if (enumType != null)
        {
            return newEnumType(name, enumType, namespace);
        }
        if (structType != null)
        {
            return newStructType(name, structType, namespace);
        }
        throw new AssertionError();
    }

    @NotNull
    private IfDevType newType(@NotNull IfDevTypeDecl typeDecl, @NotNull IfDevNamespace namespace)
    {
        return newType(
                Optional.of(ImmutableIfDevName.newInstanceFromSourceName(typeDecl.getElementNameRule().getText())),
                typeDecl.getTypeDeclBody(), namespace);
    }

    @NotNull
    private IfDevStructType newStructType(
            @NotNull Optional<IfDevName> name,
            @NotNull IfDevStructTypeDecl structTypeDecl,
            @NotNull IfDevNamespace namespace)
    {
        List<IfDevStructField> fields = new ArrayList<>();
        for (IfDevCommandArg fieldElement : structTypeDecl.getCommandArgList())
        {
            IfDevTypeUnitApplication typeUnitApplication = fieldElement.getTypeUnitApplication();
            IfDevUnit unit = typeUnitApplication.getUnit();
            fields.add(ImmutableIfDevStructField.newInstance(ImmutableIfDevName.newInstanceFromSourceName(
                            fieldElement.getElementNameRule().getText()),
                    makeProxyForTypeApplication(typeUnitApplication.getTypeApplication(), namespace),
                    Optional.ofNullable(unit).map(u -> getProxyFor(namespace.getFqn(),
                            ImmutableIfDevName.newInstanceFromSourceName(u.getElementId().getText()))),
                    getText(fieldElement.getInfoString())));
        }
        return ImmutableIfDevStructType.newInstance(name, namespace, getText(structTypeDecl.getInfoString()), fields);
    }

    @NotNull
    private IfDevSubType newSubTypeForTypeApplication(@NotNull Optional<IfDevName> name,
                                                      @NotNull Optional<String> info,
                                                      @NotNull IfDevTypeApplication newTypeDeclBody,
                                                      @NotNull IfDevNamespace namespace)
    {
        IfDevArrayTypeApplication arrayTypeApplication = newTypeDeclBody.getArrayTypeApplication();
        IfDevPrimitiveTypeApplication primitiveTypeApplication = newTypeDeclBody.getPrimitiveTypeApplication();
        IfDevElementId elementId = newTypeDeclBody.getElementId();
        if (arrayTypeApplication != null)
        {
//            long minLength = Long.parseLong(arrayTypeApplication.getLengthFrom().getNonNegativeNumber().getText());
//            IfDevLengthTo lengthTo = arrayTypeApplication.getLengthTo();
//            long maxLength = lengthTo == null ? minLength : Long.parseLong(lengthTo.getNonNegativeNumber().getText());
//            ArraySize size = new ImmutableIfDevArrayType.ImmutableArraySize(minLength, maxLength);
            return ImmutableIfDevSubType.newInstance(name, namespace, getProxyFor(arrayTypeApplication, namespace), info);
        }
        if (primitiveTypeApplication != null)
        {
            return ImmutableIfDevSubType.newInstance(name, namespace,
                    makeProxyForPrimitiveType(primitiveTypeApplication), info);
        }
        if (elementId != null)
        {
            return ImmutableIfDevSubType.newInstance(name, namespace,
                    getProxyFor(namespace.getFqn(),
                            ImmutableIfDevName.newInstanceFromSourceName(
                                    Preconditions.checkNotNull(newTypeDeclBody.getElementId()).getText())), info);
        }
        throw new AssertionError();
    }

    @NotNull
    private static IfDevMaybeProxy<IfDevType> makeProxyForPrimitiveType(
            @NotNull IfDevPrimitiveTypeApplication primitiveTypeApplication)
    {
        throw new AssertionError();
    }

    @NotNull
    private static IfDevNamespace resolveNamespaceForTypeApplication(@NotNull IfDevTypeApplication typeApplication,
                                                                     @NotNull IfDevNamespace namespace)
    {
        IfDevArrayTypeApplication arrayTypeApplication = typeApplication.getArrayTypeApplication();
        IfDevPrimitiveTypeApplication primitiveTypeApplication = typeApplication.getPrimitiveTypeApplication();
        IfDevElementId elementId = typeApplication.getElementId();
        if (arrayTypeApplication != null)
        {
            return resolveNamespaceForTypeApplication(arrayTypeApplication.getTypeApplication(), namespace);
        }
        if (primitiveTypeApplication != null)
        {
            // TODO: Resolve namespace for primitive type
            return namespace;
        }
        if (elementId != null)
        {
            // TODO: Resolve namespace for base type
            return namespace;
        }
        throw new AssertionError();
    }

    @NotNull
    private static IfDevType newEnumType(@NotNull Optional<IfDevName> name, @NotNull IfDevEnumTypeDecl enumTypeDecl,
                                         @NotNull IfDevNamespace namespace)
    {
        Set<IfDevEnumConstant> values = enumTypeDecl.getEnumTypeValues().getEnumTypeValueList().stream()
                .map(child -> ImmutableIfDevEnumConstant.newInstance(
                        ImmutableIfDevName.newInstanceFromSourceName(child.getElementNameRule().getText()),
                        child.getLiteral().getText(), getText(child.getInfoString()))).collect(Collectors.toSet());
        return ImmutableIfDevEnumType.newInstance(name, namespace, getProxyFor(namespace.getFqn(),
                ImmutableIfDevName.newInstanceFromSourceName(enumTypeDecl.getElementId().getText())),
                getText(enumTypeDecl.getInfoString()), values);
    }

    @NotNull
    private Optional<IfDevType> newPrimitiveType(@NotNull Optional<IfDevName> name,
                                                 @NotNull IfDevNamespace namespace,
                                                 @NotNull IfDevPrimitiveTypeApplication primitiveTypeApplication)
    {
        String typeKindString = primitiveTypeApplication.getPrimitiveTypeKind().getText();
        IfDevType.TypeKind typeKind;
        switch (typeKindString)
        {
            case "uint":
                typeKind = IfDevType.TypeKind.UINT;
                break;
            case "int":
                typeKind = IfDevType.TypeKind.INT;
                break;
            case "float":
                typeKind = IfDevType.TypeKind.FLOAT;
                break;
            case "bool":
                typeKind = IfDevType.TypeKind.BOOL;
                break;
            default:
                error("Unsupported type kind '%s'", typeKindString);
                return Optional.empty();
        }
        return Optional.of(ImmutableIfDevPrimitiveType.newInstance(name, namespace,
                typeKind, Long.parseLong(primitiveTypeApplication.getNonNegativeNumber().getText()),
                Optional.<String>empty()));
    }

    @NotNull
    private static Optional<String> getText(@Nullable IfDevInfoString infoString)
    {
        return getText(Optional.ofNullable(infoString)
                .map(IfDevInfoString::getStringValue));
    }

    @NotNull
    private static Optional<String> getText(@Nullable IfDevStringValue stringValue)
    {
        return getText(Optional.ofNullable(stringValue));
    }

    @NotNull
    private static Optional<String> getText(@NotNull Optional<IfDevStringValue> stringValue)
    {
        return stringValue.map(str -> Optional.ofNullable(str.getString()).orElse(str.getStringUnaryQuotes())).map(
                PsiElement::getText).map(text -> text.substring(1, text.length() - 1));
    }

    private void error(@NotNull String msg, Object... params)
    {
        result.getMessages().add(new IfDevTransformationMessage(ModelingMessage.Level.ERROR, msg, params));
    }

}
