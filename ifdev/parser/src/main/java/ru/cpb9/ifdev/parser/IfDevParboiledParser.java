package ru.cpb9.ifdev.parser;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevAliasType;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevFqn;
import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevName;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevNamespace;
import ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
@BuildParseTree
public class IfDevParboiledParser extends BaseParser<IfDevElement>
{
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
        return Sequence(Namespace(), namespaceVar.set((IfDevNamespace) pop()), ZeroOrMore(EW(),
                FirstOf(
                        Sequence(Component(), namespaceVar.get().getComponents().add((IfDevComponent) pop())),
                        Sequence(UnitDecl(), namespaceVar.get().getUnits().add((IfDevUnit) pop())),
                        Sequence(TypeDecl(), namespaceVar.get().getTypes().add((IfDevType) pop())),
                        Sequence(Alias(), namespaceVar.get().getTypes().add((IfDevType) pop()))),
                EOI, push(namespaceVar.get()));
    }

    Rule Alias(@NotNull Var<IfDevNamespace> namespaceVar)
    {
        Var<Optional<String>> infoVar = new Var<>(Optional.empty());
        return Sequence("alias", EW(), ElementNameAsName(), EW(), TypeApplicationAsProxyType(), OptEwInfoString(infoVar),
                push(ImmutableIfDevAliasType.newInstance((IfDevName) pop(1), namespaceVar.get(),
                        (IfDevMaybeProxy<IfDevType>) pop(), infoVar.get())));
    }

    Rule Namespace()
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

    Rule Component()
    {
        return Sequence("component", EW(), ElementNameAsName(),
                Optional(OptEW(), ':', OptEW(), Subcomponent(), ZeroOrMore(OptEW(), ',', OptEW(), Subcomponent())),
                OptEwInfoString(infoVar), OptEW(), '{', Optional(OptEW(), ComponentBaseType()), ZeroOrMore(OptEW(),
                        FirstOf(Command(), Message())), OptEW(), '}');
    }

    Rule ComponentBaseType()
    {
        return Sequence(TestNot(Sequence(FirstOf("command", "message"), EW())), TypeDeclBody());
    }

    Rule Command()
    {
        return Sequence("command", EW(), ElementNameAsName(), OptEW(), ':', OptEW(), NonNegativeNumber(), OptEwInfoString(
                infoVar),
                OptEW(), '(', Optional(OptEW(), CommandArgs()), OptEW(), ')');
    }

    Rule CommandArgs()
    {
        return Sequence(CommandArg(), ZeroOrMore(OptEW(), ',', OptEW(), CommandArg()), Optional(OptEW(), ','));
    }

    Rule CommandArg()
    {
        return Sequence(TypeUnitApplication(), EW(), ElementNameAsName(), OptEwInfoString(infoVar));
    }

    Rule TypeUnitApplication()
    {
        return Sequence(TypeApplicationAsProxyType(), Optional(OptEW(), Unit()));
    }

    Rule TypeApplicationAsProxyType()
    {
        return FirstOf(PrimitiveTypeApplication(),
                ArrayTypeApplication(),
                Sequence(ElementIdAsFqn(), push(SimpleIfDevMaybeProxy.proxy((IfDevFqn) pop()))));
    }

    Rule Unit()
    {
        return Sequence('/', ElementIdAsFqn(), '/');
    }

    Rule PrimitiveTypeApplication()
    {
        return Sequence(Sequence(PrimitiveTypeKind(), ':', NonNegativeNumber()),
                push(SimpleIfDevMaybeProxy
                        .proxyForSystem(ImmutableIfDevName.newInstanceFromSourceName(match())));
    }

    Rule ArrayTypeApplication()
    {
        return Sequence('[', OptEW(), TypeApplicationAsProxyType(), OptEW(), ',', OptEW(), LengthFrom(),
                Optional(OptEW(), "..", OptEW(), LengthTo()), OptEW(), ']',
                push(SimpleIfDevMaybeProxy.));
    }

    Rule StructTypeDecl()
    {
        return Sequence("struct", OptEwInfoString(infoVar), OptEW(), '(', OptEW(), CommandArg(),
                ZeroOrMore(OptEW(), ',', OptEW(), CommandArg()),
                Optional(OptEW(), ','), OptEW(), ')');
    }

    Rule ElementIdAsFqn()
    {
        Var<List<IfDevName>> names = new Var<>();
        return Sequence(ElementNameAsName(), names.set(Lists.newArrayList((IfDevName) pop())),
                ZeroOrMore('.', ElementNameAsName(), names.get().add((IfDevName) pop())),
                push(ImmutableIfDevFqn.newInstance(names.get())));
    }

    Rule Message()
    {
        return Sequence("message", EW(), ElementNameAsName(), OptEW(), ':', OptEW(), NonNegativeNumber(), OptEwInfoString(
                infoVar),
                OptEW(), FirstOf(StatusMessage(), EventMessage(), DynamicStatusMessage()));
    }

    Rule StatusMessage()
    {
        return Sequence("status", EW(), MessageParameters());
    }

    Rule MessageParameters()
    {
        return FirstOf(DeepAllParameters(), AllParameters(),
                Sequence('(', OptEW(), Parameter(), ZeroOrMore(OptEW(), ',', OptEW(), Parameter()), Optional(OptEW(), ','), OptEW(), ')'));
    }

    Rule DeepAllParameters()
    {
        return String("*.*");
    }

    Rule AllParameters()
    {
        return Ch('*');
    }

    Rule EventMessage()
    {
        return Sequence("event", EW(), MessageParameters());
    }

    Rule DynamicStatusMessage()
    {
        return Sequence("dynamic", EW(), "status", EW(), MessageParameters());
    }

    Rule Parameter()
    {
        return FirstOf(AllParameters(), Sequence(ParameterElement(), OptEwInfoString(infoVar)));
    }

    Rule ParameterElement()
    {
        return Sequence(ElementIdAsFqn(),
                Optional(OneOrMore('[', OptEW(), NonNegativeNumber(), OptEW(),
                                Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'),
                        ZeroOrMore('.', ElementIdAsFqn(),
                                Optional('[', OptEW(), NonNegativeNumber(), OptEW(), Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'))));
    }

    Rule InfoString()
    {
        return Sequence("info", EW(), StringValue());
    }

    Rule OptEwInfoString(Var<Optional<String>> infoVar)
    {
        return Optional(EW(), InfoString());
    }

    Rule StringValue()
    {
        return FirstOf(Sequence('"', ZeroOrMore(FirstOf(NoneOf("\"\\"), Sequence('\\', ANY))), '"'),
                Sequence('\'', ZeroOrMore(FirstOf(NoneOf("\'\\"), Sequence('\\', ANY))), '\''));
    }

    Rule Subcomponent()
    {
        return ElementNameAsName();
    }

    Rule UnitDecl()
    {
        return Sequence("unit", EW(), ElementNameAsName(), Optional(EW(), "display", EW(), StringValue()),
                Optional(EW(), "placement", EW(), FirstOf("before", "after")), OptEwInfoString(infoVar));
    }

    Rule TypeDecl()
    {
        return Sequence("type", EW(), ElementNameAsName(), OptEwInfoString(infoVar), EW(), TypeDeclBody());
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

    Rule TypeDeclBody()
    {
        return FirstOf(EnumTypeDecl(), StructTypeDecl(), Sequence(TypeApplicationAsProxyType(), OptEwInfoString(infoVar)));
    }

    Rule EnumTypeDecl()
    {
        return Sequence("enum", EW(), ElementIdAsFqn(), OptEwInfoString(infoVar), OptEW(), '(', OptEW(), EnumTypeValues(), OptEW(), ')');
    }

    Rule EnumTypeValues()
    {
        return Sequence(EnumTypeValue(), ZeroOrMore(OptEW(), ',', OptEW(), EnumTypeValue()), Optional(OptEW(), ','));
    }

    Rule EnumTypeValue()
    {
        return Sequence(ElementNameAsName(), OptEW(), '=', OptEW(), Literal(), OptEwInfoString(infoVar));
    }

    Rule Literal()
    {
        return FirstOf(FloatLiteral(), NonNegativeNumber(), "true", "false");
    }

    Rule FloatLiteral()
    {
        return Sequence(Optional(FirstOf('+', '-')),
                FirstOf(Sequence(NonNegativeNumber(), '.', Optional(NonNegativeNumber())),
                        Sequence('.', NonNegativeNumber())),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), NonNegativeNumber()));
    }

    Rule NonNegativeNumber()
    {
        return OneOrMore(CharRange('0', '9'));
    }
}
