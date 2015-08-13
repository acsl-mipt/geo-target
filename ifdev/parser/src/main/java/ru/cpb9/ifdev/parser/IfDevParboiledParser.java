package ru.cpb9.ifdev.parser;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.*;
import ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevEnumConstant;
import ru.cpb9.ifdev.model.domain.type.IfDevStructField;
import ru.cpb9.ifdev.model.domain.type.IfDevType;

import java.util.*;

import static ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy.proxyDefaultNamespace;
import static ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy.proxyForSystem;

/**
 * @author Artem Shein
 */
@BuildParseTree
public class IfDevParboiledParser extends BaseParser<IfDevElement>
{
    private static final Logger LOG = LoggerFactory.getLogger(IfDevParboiledParser.class);

    Rule W()
    {
        return OneOrMore(AnyOf(" \t"));
    }

    Rule OptW()
    {
        return Optional(W());
    }

    Rule EW()
    {
        return OneOrMore(AnyOf(" \t\r\n"));
    }

    Rule OptEW()
    {
        return Optional(EW());
    }

    Rule File()
    {
        Var<IfDevNamespace> namespaceVar = new Var<>();
        return Sequence(NamespaceAsNamespace(), namespaceVar.set((IfDevNamespace) pop()), ZeroOrMore(EW(),
                        FirstOf(
                                Sequence(ComponentAsComponent(namespaceVar),
                                        namespaceVar.get().getComponents().add((IfDevComponent) pop())),
                                Sequence(UnitDeclAsUnit(namespaceVar),
                                        namespaceVar.get().getUnits().add((IfDevUnit) pop())),
                                Sequence(TypeDeclAsType(namespaceVar),
                                        namespaceVar.get().getTypes().add((IfDevType) pop())),
                                Sequence(AliasAsAlias(namespaceVar),
                                        namespaceVar.get().getTypes().add((IfDevType) pop())))),
                EOI, push(namespaceVar.get()));
    }

    Rule AliasAsAlias(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence("alias", EW(), ElementNameAsName(), EW(), TypeApplicationAsProxyType(namespaceVar),
                OptEwInfoString(infoVar),
                push(ImmutableIfDevAliasType.newInstance((IfDevName) pop(1), namespaceVar.get(),
                        (IfDevMaybeProxy<IfDevType>) pop(), infoVar.get())));
    }

    Rule NamespaceAsNamespace()
    {
        return Sequence("namespace", EW(), ElementIdAsFqn(),
                push(SimpleIfDevNamespace.newRootNamespaceFor((IfDevFqn) pop())));
    }

    Rule ElementNameAsName()
    {
        return Sequence(
                Sequence(Optional('^'), FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_'), ZeroOrMore(
                        FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_'))),
                push(ImmutableIfDevName.newInstanceFromSourceName(match())));
    }

    Rule ComponentAsComponent(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<IfDevMaybeProxy<IfDevType>>> typeVar = new Var<>(Optional.empty());
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        Var<Set<IfDevMaybeProxy<IfDevComponent>>> subComponentsVar = new Var<>(new HashSet<>());
        Var<List<IfDevCommand>> commandsVar = new Var<>(new ArrayList<>());
        Var<List<IfDevMessage>> messagesVar = new Var<>(new ArrayList<>());
        Var<Optional<IfDevName>> baseTypeNameVar = new Var<>(Optional.empty());
        Var<IfDevComponent> componentVar = new Var<>();
        return Sequence("component", EW(), ElementNameAsName(),
                Optional(OptEW(), ':', OptEW(),
                        SubcomponentAsFqn(namespaceVar, subComponentsVar),
                        ZeroOrMore(OptEW(), ',', OptEW(), SubcomponentAsFqn(namespaceVar, subComponentsVar))),
                OptEwInfoString(infoVar), OptEW(), '{', Optional(OptEW(),
                        ComponentBaseTypeAsType(namespaceVar, baseTypeNameVar)),
                componentVar.set(SimpleIfDevComponent.newInstance((IfDevName) pop(), namespaceVar.get(), typeVar.get(),
                        infoVar.get(), subComponentsVar.get(), commandsVar.get(), messagesVar.get())),
                ZeroOrMore(OptEW(),
                        FirstOf(CommandAsCommand(namespaceVar), MessageAsMessage(componentVar))), OptEW(), '}',
                push(componentVar.get()));
    }

    Rule ComponentBaseTypeAsType(@NotNull Var<IfDevNamespace> namespaceVar,
                                 @NotNull Var<Optional<IfDevName>> nameVar)
    {
        return Sequence(TestNot(Sequence(FirstOf("command", "message"), EW())),
                TypeDeclBodyAsType(namespaceVar, nameVar));
    }

    Rule CommandAsCommand(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<List<IfDevCommandArgument>> argsVar = new Var<>(new ArrayList<>());
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence("command", EW(), ElementNameAsName(), OptEW(), ':', OptEW(), NonNegativeNumberAsInteger(), OptEwInfoString(
                infoVar),
                OptEW(), '(', Optional(OptEW(), CommandArgs(namespaceVar, argsVar)), OptEW(), ')',
                push(ImmutableIfDevCommand.newInstance((IfDevName) pop(1),
                        ((ImmutableIfDevElementWrapper<Integer>) pop()).getValue(), infoVar.get(), argsVar.get())));
    }

    Rule CommandArgs(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<List<IfDevCommandArgument>> argsVar)
    {
        return Sequence(CommandArgAsCommandArg(namespaceVar), argsVar.get().add((IfDevCommandArgument) pop()),
                ZeroOrMore(OptEW(), ',', OptEW(), CommandArgAsCommandArg(namespaceVar)),
                argsVar.get().add((IfDevCommandArgument) pop()), Optional(OptEW(), ','));
    }

    Rule CommandArgAsCommandArg(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<IfDevMaybeProxy<IfDevUnit>>> unitVar = new Var<>(Optional.empty());
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence(TypeUnitApplicationAsProxyType(namespaceVar, unitVar), EW(), ElementNameAsName(), OptEwInfoString(infoVar),
                push(ImmutableIfDevCommandArgument
                        .newInstance((IfDevName) pop(), (IfDevMaybeProxy<IfDevType>) pop(), unitVar.get(),
                                infoVar.get())));
    }

    Rule StructFieldAsStructField(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<IfDevMaybeProxy<IfDevUnit>>> unitVar = new Var<>(Optional.empty());
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence(TypeUnitApplicationAsProxyType(namespaceVar, unitVar), EW(), ElementNameAsName(), OptEwInfoString(infoVar),
                push(ImmutableIfDevStructField.newInstance((IfDevName) pop(), (IfDevMaybeProxy<IfDevType>) pop(), unitVar.get(), infoVar.get())));
    }

    Rule TypeUnitApplicationAsProxyType(@NotNull Var<IfDevNamespace> namespaceVar,
                                        @NotNull Var<Optional<IfDevMaybeProxy<IfDevUnit>>> unitVar)
    {
        return Sequence(TypeApplicationAsProxyType(namespaceVar), Optional(OptEW(), UnitAsFqn(), unitVar.set(
                Optional.of(proxyDefaultNamespace((IfDevFqn) pop(), namespaceVar.get())))));
    }

    Rule TypeApplicationAsProxyType(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        return FirstOf(PrimitiveTypeApplicationAsProxyType(),
                ArrayTypeApplicationAsProxyType(namespaceVar),
                Sequence(ElementIdAsFqn(), push(proxyDefaultNamespace((IfDevFqn) pop(),
                        namespaceVar.get()))));
    }

    Rule UnitAsFqn()
    {
        return Sequence('/', ElementIdAsFqn(), '/');
    }

    Rule PrimitiveTypeApplicationAsProxyType()
    {
        return Sequence(Sequence(PrimitiveTypeKind(), ':', NonNegativeNumberAsInteger(), drop()),
                push(proxyForSystem(ImmutableIfDevName.newInstanceFromSourceName(match()))));
    }

    Rule ArrayTypeApplicationAsProxyType(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        return Sequence(Sequence('[', OptEW(), TypeApplicationAsProxyType(namespaceVar), drop(), OptEW(), ',', OptEW(), LengthFrom(),
                Optional(OptEW(), "..", OptEW(), LengthTo()), OptEW(), ']'),
                push(proxyForSystem(ImmutableIfDevName.newInstanceFromSourceName(match()))));
    }

    Rule StructTypeDeclAsStructType(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<Optional<IfDevName>> nameVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        Var<List<IfDevStructField>> fieldsVar = new Var<>(new ArrayList<>());
        return Sequence("struct", OptEwInfoString(infoVar), OptEW(), '(', OptEW(),
                StructFieldAsStructField(namespaceVar), fieldsVar.get().add((IfDevStructField) pop()),
                ZeroOrMore(OptEW(), ',', OptEW(), StructFieldAsStructField(namespaceVar), fieldsVar.get().add((IfDevStructField) pop())),
                Optional(OptEW(), ','), OptEW(), ')',
                push(ImmutableIfDevStructType
                        .newInstance(nameVar.get(), namespaceVar.get(), infoVar.get(), fieldsVar.get())));
    }

    Rule ElementIdAsFqn()
    {
        Var<List<IfDevName>> names = new Var<>();
        return Sequence(ElementNameAsName(), names.set(Lists.newArrayList((IfDevName) pop())),
                ZeroOrMore('.', ElementNameAsName(), names.get().add((IfDevName) pop())),
                push(ImmutableIfDevFqn.newInstance(names.get())));
    }

    Rule MessageAsMessage(@NotNull Var<IfDevComponent> componentVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        Var<IfDevName> nameVar = new Var<>();
        Var<Integer> idVar = new Var<>();
        return Sequence("message", EW(), ElementNameAsName(), nameVar.set((IfDevName) pop()), OptEW(), ':', OptEW(),
                NonNegativeNumberAsInteger(), idVar.set(((ImmutableIfDevElementWrapper<Integer>) pop()).getValue()),
                OptEwInfoString(infoVar), OptEW(),
                FirstOf(StatusMessageAsMessage(componentVar, nameVar, idVar, infoVar),
                        EventMessageAsMessage(componentVar, nameVar, idVar, infoVar),
                        DynamicStatusMessageAsMessage(componentVar, nameVar, idVar, infoVar)));
    }

    Rule StatusMessageAsMessage(@NotNull Var<IfDevComponent> componentVar, @NotNull Var<IfDevName> nameVar,
                                @NotNull Var<Integer> idVar, @NotNull Var<Optional<String>> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("status", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevStatusMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        infoVar.get(), parametersVar.get())));
    }

    Rule MessageParameters(@NotNull Var<List<IfDevMessageParameter>> parametersVar)
    {
        return FirstOf(DeepAllParameters(), AllParameters(),
                Sequence('(', OptEW(), ParameterAsParameter(),
                        ZeroOrMore(OptEW(), ',', OptEW(), ParameterAsParameter()),
                        Optional(OptEW(), ','), OptEW(), ')'));
    }

    Rule DeepAllParameters()
    {
        return String("*.*");
    }

    Rule AllParameters()
    {
        return Ch('*');
    }

    Rule EventMessageAsMessage(@NotNull Var<IfDevComponent> componentVar, @NotNull Var<IfDevName> nameVar,
                               @NotNull Var<Integer> idVar, @NotNull Var<Optional<String>> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("event", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevEventMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        infoVar.get(), parametersVar.get())));
    }

    Rule DynamicStatusMessageAsMessage(@NotNull Var<IfDevComponent> componentVar, @NotNull Var<IfDevName> nameVar,
                                       @NotNull Var<Integer> idVar, @NotNull Var<Optional<String>> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("dynamic", EW(), "status", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevDynamicStatusMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        infoVar.get(), parametersVar.get())));
    }

    Rule ParameterAsParameter()
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return FirstOf(Sequence(AllParameters(), push(ImmutableIfDevAllParameters.INSTANCE)),
                Sequence(ParameterElement(), push(ImmutableIfDevElementWrapper.newInstance(match())),
                        OptEwInfoString(infoVar), push(ImmutableIfDevMessageParameter.newInstance(
                                ((ImmutableIfDevElementWrapper<String>) pop()).getValue()))));
    }

    Rule ParameterElement()
    {
        return Sequence(ElementIdAsFqn(), drop(),
                Optional(OneOrMore('[', OptEW(), NonNegativeNumberAsInteger(), OptEW(),
                                Optional("..", OptEW(), NonNegativeNumberAsInteger(), OptEW()), ']'),
                        ZeroOrMore('.', ElementIdAsFqn(), drop(),
                                Optional('[', OptEW(), NonNegativeNumberAsInteger(), OptEW(),
                                        Optional("..", OptEW(), NonNegativeNumberAsInteger(), OptEW()), ']'))));
    }

    Rule InfoStringAsString()
    {
        return Sequence("info", EW(), StringValueAsString());
    }

    Rule OptEwInfoString(@NotNull Var<Optional<String>> infoVar)
    {
        return Optional(EW(), InfoStringAsString(), infoVar.set(Optional.of(
                ((ImmutableIfDevElementWrapper<String>) pop()).getValue())));
    }

    Rule StringValueAsString()
    {
        return FirstOf(Sequence('"', ZeroOrMore(FirstOf(NoneOf("\"\\"), Sequence('\\', ANY))),
                        push(ImmutableIfDevElementWrapper.newInstance(match())), FirstOf('"', Sequence(drop(), NOTHING))),
                Sequence('\'', ZeroOrMore(FirstOf(NoneOf("\'\\"), Sequence('\\', ANY))),
                        push(ImmutableIfDevElementWrapper.newInstance(match())),
                        FirstOf('\'', Sequence(drop(), NOTHING))));
    }

    Rule SubcomponentAsFqn(@NotNull Var<IfDevNamespace> namespaceVar, Var<Set<IfDevMaybeProxy<IfDevComponent>>> subComponentsVar)
    {
        return Sequence(ElementIdAsFqn(), subComponentsVar.get().add(proxyDefaultNamespace(
                (IfDevFqn) pop(), namespaceVar.get())));
    }

    Rule UnitDeclAsUnit(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        Var<Optional<String>> displayVar = new Var<>(Optional.empty());
        return Sequence("unit", EW(), ElementNameAsName(), Optional(EW(), "display", EW(), StringValueAsString(),
                        displayVar.set(Optional.of(((ImmutableIfDevElementWrapper<String>) pop()).getValue()))),
                Optional(EW(), "placement", EW(), FirstOf("before", "after")), OptEwInfoString(infoVar),
                push(ImmutableIfDevUnit.newInstance((IfDevName) pop(), namespaceVar.get(),
                        displayVar.get(), infoVar.get())));
    }

    boolean test(IfDevElement peek)
    {
        LOG.debug("{}", peek);
        return true;
    }

    Rule TypeDeclAsType(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<IfDevName>> nameVar = new Var<>();
        return Sequence("type", EW(), ElementNameAsName(), nameVar.set(Optional.of((IfDevName) pop())),
                TypeDeclBodyAsType(namespaceVar, nameVar));
    }

    Rule PrimitiveTypeKind()
    {
        return FirstOf("uint", "int", "float", "bool");
    }

    Rule LengthFrom()
    {
        return NonNegativeNumberAsInteger();
    }

    Rule LengthTo()
    {
        return NonNegativeNumberAsInteger();
    }

    Rule TypeDeclBodyAsType(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<Optional<IfDevName>> nameVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return FirstOf(EnumTypeDecl(namespaceVar, nameVar), StructTypeDeclAsStructType(namespaceVar, nameVar),
                Sequence(TypeApplicationAsProxyType(namespaceVar), OptEwInfoString(infoVar),
                        push(ImmutableIfDevSubType.newInstance(nameVar.get(), namespaceVar.get(),
                                (IfDevMaybeProxy<IfDevType>) pop(), infoVar.get()))));
    }

    Rule EnumTypeDecl(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<Optional<IfDevName>> nameVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        Var<Set<IfDevEnumConstant>> enumConstantsVar = new Var<>(new HashSet<>());
        return Sequence("enum", EW(), ElementIdAsFqn(), OptEwInfoString(infoVar), OptEW(), '(', OptEW(),
                EnumTypeValues(enumConstantsVar), OptEW(), ')',
                push(ImmutableIfDevEnumType.newInstance(nameVar.get(), namespaceVar.get(),
                        proxyDefaultNamespace((IfDevFqn) pop(), namespaceVar.get()),
                        infoVar.get(), enumConstantsVar.get())));
    }

    Rule EnumTypeValues(@NotNull Var<Set<IfDevEnumConstant>> enumConstantsVar)
    {
        return Sequence(EnumTypeValue(), ZeroOrMore(OptEW(), ',', OptEW(), EnumTypeValue(),
                enumConstantsVar.get().add((IfDevEnumConstant) pop())), Optional(OptEW(), ','));
    }

    Rule EnumTypeValue()
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence(ElementNameAsName(), OptEW(), '=', OptEW(), Literal(), OptEwInfoString(infoVar),
                push(ImmutableIfDevEnumConstant.newInstanceWrapper((IfDevName) pop(1),
                        (ImmutableIfDevElementWrapper<String>) pop(),
                        infoVar.get())));
    }

    Rule Literal()
    {
        return Sequence(FirstOf(FloatLiteral(), NonNegativeNumberAsInteger(), "true", "false"),
                push(ImmutableIfDevElementWrapper.newInstance(match())));
    }

    Rule FloatLiteral()
    {
        return Sequence(Optional(FirstOf('+', '-')),
                FirstOf(Sequence(NonNegativeNumberAsInteger(), '.', Optional(NonNegativeNumberAsInteger())),
                        Sequence('.', NonNegativeNumberAsInteger())),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), NonNegativeNumberAsInteger()));
    }

    Rule NonNegativeNumberAsInteger()
    {
        return Sequence(OneOrMore(CharRange('0', '9')), push(ImmutableIfDevElementWrapper.newInstance(
                Integer.parseInt(match()))));
    }
}
