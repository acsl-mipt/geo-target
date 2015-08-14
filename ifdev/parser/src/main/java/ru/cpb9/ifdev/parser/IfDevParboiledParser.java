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
        Var<String> infoVar = new Var<>();
        return Sequence("alias", EW(), ElementNameAsName(), EW(), TypeApplicationAsProxyType(namespaceVar),
                OptEwInfoString(infoVar),
                push(ImmutableIfDevAliasType.newInstance((IfDevName) pop(1), namespaceVar.get(),
                        (IfDevMaybeProxy<IfDevType>) pop(), Optional.ofNullable(infoVar.get()))));
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
        Var<IfDevMaybeProxy<IfDevType>> typeVar = new Var<>();
        Var<String> infoVar = new Var<>();
        Var<Set<IfDevMaybeProxy<IfDevComponent>>> subComponentsVar = new Var<>(new HashSet<>());
        Var<List<IfDevCommand>> commandsVar = new Var<>(new ArrayList<>());
        Var<List<IfDevMessage>> messagesVar = new Var<>(new ArrayList<>());
        Var<IfDevName> baseTypeNameVar = new Var<>();
        Var<IfDevComponent> componentVar = new Var<>();
        return Sequence("component", EW(), ElementNameAsName(),
                test(peek(), "12"),
                Optional(OptEW(), ':', OptEW(), Subcomponent(namespaceVar, subComponentsVar),
                        ZeroOrMore(OptEW(), ',', OptEW(), Subcomponent(namespaceVar, subComponentsVar))),
                OptEwInfoString(infoVar), OptEW(), '{',
                test(peek(), "11"),
                Optional(OptEW(), test(getContext().getValueStack().size(), "20"), ComponentBaseTypeAsType(namespaceVar,
                        baseTypeNameVar),
                        test(getContext().getValueStack().size(), "21"),
                        typeVar.set(SimpleIfDevMaybeProxy.object((IfDevType)pop()))),
                test(peek(), "10"),
                componentVar.set(SimpleIfDevComponent.newInstance((IfDevName) pop(), namespaceVar.get(), Optional.ofNullable(typeVar.get()),
                        Optional.ofNullable(infoVar.get()), subComponentsVar.get(), commandsVar.get(), messagesVar.get())),
                ZeroOrMore(OptEW(),
                        FirstOf(CommandAsCommand(namespaceVar), MessageAsMessage(componentVar))), OptEW(), '}',
                push(componentVar.get()));
    }

    Rule ComponentBaseTypeAsType(@NotNull Var<IfDevNamespace> namespaceVar,
                                 @NotNull Var<IfDevName> nameVar)
    {
        return Sequence(TestNot(Sequence(FirstOf("command", "message"), EW())),
                TypeDeclBodyAsType(namespaceVar, nameVar));
    }

    Rule CommandAsCommand(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<List<IfDevCommandArgument>> argsVar = new Var<>(new ArrayList<>());
        Var<String> infoVar = new Var<>();
        return Sequence("command", EW(), ElementNameAsName(), test(peek(), "1"), OptEW(), ':', OptEW(),
                NonNegativeNumberAsInteger(), test(peek(), "2"), OptEwInfoString(infoVar), test(peek(), "3"),
                OptEW(), '(', Optional(OptEW(), CommandArgs(namespaceVar, argsVar)), test(peek(), "4"), OptEW(), ')',
                test(peek(), "5"), test(peek(1), "5.2"),
                push(ImmutableIfDevCommand.newInstance((IfDevName) pop(1),
                        ((ImmutableIfDevElementWrapper<Integer>) pop()).getValue(), Optional.ofNullable(infoVar.get()), argsVar.get())));
    }

    Rule CommandArgs(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<List<IfDevCommandArgument>> argsVar)
    {
        return Sequence(CommandArgAsCommandArg(namespaceVar), argsVar.get().add((IfDevCommandArgument) pop()),
                ZeroOrMore(OptEW(), ',', OptEW(), CommandArgAsCommandArg(namespaceVar), argsVar.get().add(
                        (IfDevCommandArgument) pop())), Optional(OptEW(), ','));
    }

    Rule CommandArgAsCommandArg(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<IfDevMaybeProxy<IfDevUnit>> unitVar = new Var<>();
        Var<String> infoVar = new Var<>();
        return Sequence(TypeUnitApplicationAsProxyType(namespaceVar, unitVar), EW(), ElementNameAsName(), OptEwInfoString(
                        infoVar),
                push(ImmutableIfDevCommandArgument
                        .newInstance((IfDevName) pop(), (IfDevMaybeProxy<IfDevType>) pop(),
                                Optional.ofNullable(unitVar.get()),
                                Optional.ofNullable(infoVar.get()))));
    }

    Rule StructFieldAsStructField(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<IfDevMaybeProxy<IfDevUnit>> unitVar = new Var<>();
        Var<String> infoVar = new Var<>();
        return Sequence(test(getContext().getValueStack().size(), "30"), TypeUnitApplicationAsProxyType(namespaceVar,
                        unitVar), EW(), ElementNameAsName(), OptEwInfoString(
                        infoVar),
                push(ImmutableIfDevStructField
                        .newInstance((IfDevName) pop(), (IfDevMaybeProxy<IfDevType>) pop(),
                                Optional.ofNullable(unitVar.get()),
                                Optional.ofNullable(infoVar.get()))), test(getContext().getValueStack().size(), "31"));
    }

    Rule TypeUnitApplicationAsProxyType(@NotNull Var<IfDevNamespace> namespaceVar,
                                        @NotNull Var<IfDevMaybeProxy<IfDevUnit>> unitVar)
    {
        return Sequence(test(getContext().getValueStack().size(), "50"), TypeApplicationAsProxyType(namespaceVar), Optional(
                OptEW(), UnitAsFqn(), unitVar.set(
                proxyDefaultNamespace((IfDevFqn) pop(), namespaceVar.get()))), test(getContext().getValueStack().size(),
                "51"));
    }

    Rule TypeApplicationAsProxyType(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        return Sequence(
                test(getContext().getValueStack().size(), "100"),
                FirstOf(PrimitiveTypeApplicationAsProxyType(),
                        // FIXME: bug with Var in parboiled
                        Sequence(test(getContext().getValueStack().size(), "60"), push(namespaceVar.get()),
                                ArrayTypeApplicationAsProxyType(), test(
                                        getContext().getValueStack().size(), "61")),
                        Sequence(test(getContext().getValueStack().size(), "70"), ElementIdAsFqn(), push(
                                proxyDefaultNamespace((IfDevFqn) pop(),
                                        namespaceVar.get())), test(getContext().getValueStack().size(), "71"))),
                test(getContext().getValueStack().size(), "101"));
    }

    Rule UnitAsFqn()
    {
        return Sequence('/', ElementIdAsFqn(), '/');
    }

    Rule PrimitiveTypeApplicationAsProxyType()
    {
        return Sequence(Sequence(PrimitiveTypeKind(), ':', NonNegativeNumber()),
                push(proxyForSystem(ImmutableIfDevName.newInstanceFromSourceName(match()))));
    }

    // FIXME: uses IfDevNamespace ref from stack, should use Var, parboiled bug
    Rule ArrayTypeApplicationAsProxyType()
    {
        Var<IfDevNamespace> namespaceVar = new Var<>();
        return Sequence(
                test(getContext().getValueStack().size(), "80"),
                Sequence(namespaceVar.set((IfDevNamespace) pop()), '[', OptEW(),
                        TypeApplicationAsProxyType(namespaceVar), drop(), OptEW(), ',', OptEW(), LengthFrom(),
                        Optional(OptEW(), "..", OptEW(), LengthTo()), OptEW(), ']'),
                push(proxyForSystem(ImmutableIfDevName.newInstanceFromSourceName(match()))),
                test(getContext().getValueStack().size(), "81"));
    }

    Rule StructTypeDeclAsStructType(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<IfDevName> nameVar)
    {
        Var<String> infoVar = new Var<>();
        Var<List<IfDevStructField>> fieldsVar = new Var<>(new ArrayList<>());
        return Sequence("struct", OptEwInfoString(infoVar), OptEW(), '(', OptEW(),
                StructFieldAsStructField(namespaceVar), fieldsVar.get().add((IfDevStructField) pop()),
                ZeroOrMore(OptEW(), ',', OptEW(), StructFieldAsStructField(namespaceVar),
                        fieldsVar.get().add((IfDevStructField) pop())),
                Optional(OptEW(), ','), OptEW(), ')',
                push(ImmutableIfDevStructType
                        .newInstance(Optional.ofNullable(nameVar.get()), namespaceVar.get(), Optional.ofNullable(infoVar.get()), fieldsVar.get())));
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
        Var<String> infoVar = new Var<>();
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
                                @NotNull Var<Integer> idVar, @NotNull Var<String> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("status", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevStatusMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        Optional.ofNullable(infoVar.get()), parametersVar.get())));
    }

    Rule MessageParameters(@NotNull Var<List<IfDevMessageParameter>> parametersVar)
    {
        return FirstOf(
                Sequence(DeepAllParameters(), parametersVar.get().add(ImmutableIfDevDeepAllParameters.INSTANCE)),
                Sequence(AllParameters(), parametersVar.get().add(ImmutableIfDevAllParameters.INSTANCE)),
                Sequence('(', OptEW(), ParameterAsParameter(), parametersVar.get().add((IfDevMessageParameter) pop()),
                        ZeroOrMore(OptEW(), ',', OptEW(), ParameterAsParameter(), parametersVar.get().add(
                                (IfDevMessageParameter) pop())),
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
                               @NotNull Var<Integer> idVar, @NotNull Var<String> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("event", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevEventMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        Optional.ofNullable(infoVar.get()), parametersVar.get())));
    }

    Rule DynamicStatusMessageAsMessage(@NotNull Var<IfDevComponent> componentVar, @NotNull Var<IfDevName> nameVar,
                                       @NotNull Var<Integer> idVar, @NotNull Var<String> infoVar)
    {
        Var<List<IfDevMessageParameter>> parametersVar = new Var<>(new ArrayList<>());
        return Sequence("dynamic", EW(), "status", EW(), MessageParameters(parametersVar),
                push(ImmutableIfDevDynamicStatusMessage.newInstance(componentVar.get(), nameVar.get(), idVar.get(),
                        Optional.ofNullable(infoVar.get()), parametersVar.get())));
    }

    Rule ParameterAsParameter()
    {
        Var<String> infoVar = new Var<>();
        return FirstOf(Sequence(AllParameters(), push(ImmutableIfDevAllParameters.INSTANCE)),
                Sequence(ParameterElement(), push(ImmutableIfDevElementWrapper.newInstance(match())),
                        OptEwInfoString(infoVar), push(ImmutableIfDevMessageParameter.newInstance(
                                ((ImmutableIfDevElementWrapper<String>) pop()).getValue()))));
    }

    Rule ParameterElement()
    {
        return Sequence(ElementIdAsFqn(), drop(),
                Optional(OneOrMore('[', OptEW(), NonNegativeNumber(), OptEW(),
                                Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'),
                        ZeroOrMore('.', ElementIdAsFqn(), drop(),
                                Optional('[', OptEW(), NonNegativeNumber(), OptEW(),
                                        Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'))));
    }

    Rule InfoStringAsString()
    {
        return Sequence("info", EW(), StringValueAsString());
    }

    Rule OptEwInfoString(@NotNull Var<String> infoVar)
    {
        return Optional(EW(), InfoStringAsString(),
                infoVar.set(((ImmutableIfDevElementWrapper<String>) pop()).getValue()));
    }

    Rule StringValueAsString()
    {
        return FirstOf(Sequence('"', ZeroOrMore(FirstOf(NoneOf("\"\\"), Sequence('\\', ANY))),
                        push(ImmutableIfDevElementWrapper.newInstance(match())),
                        FirstOf('"', Sequence(drop(), NOTHING))),
                Sequence('\'', ZeroOrMore(FirstOf(NoneOf("\'\\"), Sequence('\\', ANY))),
                        push(ImmutableIfDevElementWrapper.newInstance(match())),
                        FirstOf('\'', Sequence(drop(), NOTHING))));
    }

    Rule Subcomponent(@NotNull Var<IfDevNamespace> namespaceVar,
                      Var<Set<IfDevMaybeProxy<IfDevComponent>>> subComponentsVar)
    {
        return Sequence(ElementIdAsFqn(), subComponentsVar.get().add(proxyDefaultNamespace(
                (IfDevFqn) pop(), namespaceVar.get())));
    }

    Rule UnitDeclAsUnit(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<String> infoVar = new Var<>();
        Var<String> displayVar = new Var<>();
        return Sequence("unit", EW(), ElementNameAsName(), Optional(EW(), "display", EW(), StringValueAsString(),
                        displayVar.set(((ImmutableIfDevElementWrapper<String>) pop()).getValue())),
                Optional(EW(), "placement", EW(), FirstOf("before", "after")), OptEwInfoString(infoVar),
                push(ImmutableIfDevUnit.newInstance((IfDevName) pop(), namespaceVar.get(),
                        displayVar.get(), infoVar.get())));
    }

    boolean test(Object value, String s)
    {
        LOG.debug("{}: {}", s, value);
        return true;
    }

    Rule TypeDeclAsType(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<IfDevName> nameVar = new Var<>();
        return Sequence("type", EW(), ElementNameAsName(), nameVar.set((IfDevName) pop()), EW(),
                TypeDeclBodyAsType(namespaceVar, nameVar));
    }

    Rule PrimitiveTypeKind()
    {
        return FirstOf("uint", "int", "float", "bool");
    }

    Rule LengthFrom()
    {
        return NonNegativeNumber();
    }

    Rule LengthTo()
    {
        return NonNegativeNumber();
    }

    Rule TypeDeclBodyAsType(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<IfDevName> nameVar)
    {
        Var<String> infoVar = new Var<>();
        return Sequence(test(getContext().getValueStack().size(), "90"), FirstOf(
                        EnumTypeDeclAsType(namespaceVar, nameVar),
                        StructTypeDeclAsStructType(namespaceVar, nameVar),
                        Sequence(test(getContext().getValueStack().size(), "110"), TypeApplicationAsProxyType(namespaceVar), OptEwInfoString(infoVar),
                                test(getContext().getValueStack().size(), "111"),
                                push(ImmutableIfDevSubType
                                        .newInstance(Optional.ofNullable(nameVar.get()), namespaceVar.get(),
                                                (IfDevMaybeProxy<IfDevType>) pop(), Optional.ofNullable(
                                                infoVar.get()))))),
                test(getContext().getValueStack().size(), "91"));
    }

    Rule EnumTypeDeclAsType(@NotNull Var<IfDevNamespace> namespaceVar, @NotNull Var<IfDevName> nameVar)
    {
        Var<String> infoVar = new Var<>();
        Var<Set<IfDevEnumConstant>> enumConstantsVar = new Var<>(new HashSet<>());
        return Sequence(test(getContext().getValueStack().size(), "40"), "enum", EW(), ElementIdAsFqn(), OptEwInfoString(infoVar), OptEW(), '(', OptEW(),
                EnumTypeValues(enumConstantsVar), OptEW(), ')',
                push(ImmutableIfDevEnumType.newInstance(Optional.ofNullable(nameVar.get()), namespaceVar.get(),
                        proxyDefaultNamespace((IfDevFqn) pop(), namespaceVar.get()),
                        Optional.ofNullable(infoVar.get()), enumConstantsVar.get())), test(getContext().getValueStack().size(), "41"));
    }

    Rule EnumTypeValues(@NotNull Var<Set<IfDevEnumConstant>> enumConstantsVar)
    {
        return Sequence(EnumTypeValue(enumConstantsVar),
                ZeroOrMore(OptEW(), ',', OptEW(), EnumTypeValue(enumConstantsVar)), Optional(OptEW(), ','));
    }

    Rule EnumTypeValue(@NotNull Var<Set<IfDevEnumConstant>> enumConstantsVar)
    {
        Var<String> infoVar = new Var<>();
        return Sequence(ElementNameAsName(), OptEW(), '=', OptEW(), LiteralAsString(), OptEwInfoString(infoVar),
                enumConstantsVar.get().add(ImmutableIfDevEnumConstant.newInstanceWrapper((IfDevName) pop(1),
                        (ImmutableIfDevElementWrapper<String>) pop(), Optional.ofNullable(infoVar.get()))));
    }

    Rule LiteralAsString()
    {
        return Sequence(FirstOf(FloatLiteral(), NonNegativeNumber(), "true", "false"),
                push(ImmutableIfDevElementWrapper.newInstance(match())));
    }

    Rule FloatLiteral()
    {
        return Sequence(Optional(FirstOf('+', '-')),
                FirstOf(Sequence(NonNegativeNumber(), '.', Optional(NonNegativeNumber())),
                        Sequence('.', NonNegativeNumber())),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), NonNegativeNumber()));
    }

    Rule NonNegativeNumberAsInteger()
    {
        return Sequence(NonNegativeNumber(), push(ImmutableIfDevElementWrapper.newInstance(Integer.parseInt(match()))));
    }

    Rule NonNegativeNumber()
    {
        return OneOrMore(CharRange('0', '9'));
    }
}
