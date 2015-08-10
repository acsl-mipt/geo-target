package ru.cpb9.ifdev.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import ru.cpb9.ifdev.model.domain.IfDevElement;

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
        return Sequence(Namespace(), ZeroOrMore(FirstOf(Component(), UnitDecl(), TypeDecl(), Alias())), EOI);
    }

    Rule Alias()
    {
        return Sequence("alias", EW(), ElementId(), TypeApplication(), Optional(InfoString()));
    }

    Rule Namespace()
    {
        return Sequence("namespace", ElementId());
    }

    Rule ElementName()
    {
        return Sequence(Optional('^'), FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_'), ZeroOrMore(
                FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_')));
    }

    Rule Component()
    {
        return Sequence("component", ElementName(), Optional(':', Subcomponent(), ZeroOrMore(',', Subcomponent())), Optional(InfoString()), '{', Optional(ComponentBaseType()), ZeroOrMore(
                FirstOf(Command(), Message())), '}');
    }

    Rule ComponentBaseType()
    {
        return TypeDeclBody();
    }

    Rule Command()
    {
        return Sequence("command", ElementName(), ':', NonNegativeNumber(), Optional(InfoString()), '(',
                Optional(CommandArgs()), ')');
    }

    Rule CommandArgs()
    {
        return Sequence(CommandArg(), ZeroOrMore(',', CommandArg()), Optional(','));
    }

    Rule CommandArg()
    {
        return Sequence(TypeUnitApplication(), ElementName(), Optional(InfoString()));
    }

    Rule TypeUnitApplication()
    {
        return Sequence(TypeApplication(), Optional(Unit()));
    }

    Rule TypeApplication()
    {
        return FirstOf(ElementId(), PrimitiveTypeApplication(), ArrayTypeApplication());
    }

    Rule Unit()
    {
        return Sequence('/', ElementId(), '/');
    }

    Rule PrimitiveTypeApplication()
    {
        return Sequence(PrimitiveTypeKind(), ':', NonNegativeNumber());
    }

    Rule ArrayTypeApplication()
    {
        return Sequence('[', TypeApplication(), ',', LengthFrom(), Optional("..", LengthTo()), ']');
    }

    Rule StructTypeDecl()
    {
        return Sequence("struct", EW(), OptInfoString(), OptEW(), '(', OptEW(), CommandArg(), ZeroOrMore(',', OptEW(),
                CommandArg()), Optional(',', OptEW()), ')');
    }

    Rule ElementId()
    {
        return Sequence(ElementName(), Optional('.', ElementName()));
    }

    Rule Message()
    {
        return Sequence("message", EW(), ElementName(), ':', OptEW(), NonNegativeNumber(), OptInfoString(),
                FirstOf(StatusMessage(), EventMessage(), DynamicStatusMessage()));
    }

    Rule StatusMessage()
    {
        return Sequence("status", EW(), MessageParameters());
    }

    Rule MessageParameters()
    {
        return FirstOf(DeepAllParameters(), AllParameters(),
                Sequence('(', OptEW(), Parameter(), ZeroOrMore(',', OptEW(), Parameter()), Optional(',', OptEW()), ')'));
    }

    Rule DeepAllParameters()
    {
        return Sequence("*.*", OptEW());
    }

    Rule AllParameters()
    {
        return Sequence('*', OptEW());
    }

    Rule EventMessage()
    {
        return Sequence("event", EW(), MessageParameters());
    }

    Rule DynamicStatusMessage()
    {
        return Sequence("dynamic", EW(), "status", MessageParameters());
    }

    Rule Parameter()
    {
        return FirstOf(AllParameters(), Sequence(ParameterElement(), Optional(InfoString())));
    }

    Rule ParameterElement()
    {
        return Sequence(ElementId(),
                Optional(OneOrMore('[', OptEW(), NonNegativeNumber(), OptEW(),
                                Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'),
                        ZeroOrMore('.', ElementId(),
                                Optional('[', OptEW(), NonNegativeNumber(), OptEW(), Optional("..", OptEW(), NonNegativeNumber(), OptEW()), ']'))));
    }

    Rule InfoString()
    {
        return Sequence("info", EW(), StringValue());
    }

    Rule OptInfoString()
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
        return ElementName();
    }

    Rule UnitDecl()
    {
        return Sequence("unit", EW(), ElementName(), Optional("display", EW(), StringValue()),
                Optional("placement", EW(), FirstOf("before", "after")), OptInfoString());
    }

    Rule TypeDecl()
    {
        return Sequence("type", EW(), ElementName(), OptInfoString(), TypeDeclBody());
    }

    Rule PrimitiveTypeKind()
    {
        return FirstOf("unit", "int", "float", "bool");
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
        return FirstOf(Sequence(TypeApplication(), OptInfoString()), EnumTypeDecl(), StructTypeDecl());
    }

    Rule EnumTypeDecl()
    {
        return Sequence("enum", EW(), ElementId(), OptInfoString(), '(', OptEW(), EnumTypeValues(), OptEW(), ')');
    }

    Rule EnumTypeValues()
    {
        return Sequence(EnumTypeValue(), OptEW(), Optional(',', OptEW(), EnumTypeValue()), Optional(OptEW(), ','));
    }

    Rule EnumTypeValue()
    {
        return Sequence(ElementName(), OptEW(), '=', OptEW(), Literal(), OptInfoString());
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
