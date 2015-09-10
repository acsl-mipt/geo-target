package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;
import ru.cpb9.common.Either;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.type.ArraySize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Artem Shein
 */
public class IfDevParameterWalker implements Iterator<Either<String, Integer>>
{
    @NotNull
    private final IfDevMessageParameter parameter;
    private final List<Either<String, Integer>> tokens;
    private int currentIndex;

    public IfDevParameterWalker(@NotNull IfDevMessageParameter parameter)
    {
        this.parameter = parameter;
        this.currentIndex = 0;
        ParsingResult<List<Either<String, Integer>>> result = new TracingParseRunner<List<Either<String, Integer>>>(Parboiled.createParser(ParameterParser.class).Parameter()).run(
                parameter.getValue());
        Preconditions.checkState(result.matched && !result.hasErrors());
        tokens = result.resultValue;
    }

    @Override
    public boolean hasNext()
    {
        return currentIndex < tokens.size();
    }

    @Override
    public Either<String, Integer> next()
    {
        return tokens.get(currentIndex++);
    }

    @BuildParseTree
    static class ParameterParser extends BaseParser<List<Either<String, Integer>>>
    {
        Rule Parameter()
        {
            Var<List<Either<String, Integer>>> tokens = new Var<>(new ArrayList<>());
            return Sequence(ElementName(tokens), ZeroOrMore(FirstOf(Sequence('.', ElementName(tokens)), ArrayLimits(
                    tokens))), EOI, push(tokens.get()));
        }

        Rule ElementName(@NotNull Var<List<Either<String, Integer>>> tokens)
        {
            return Sequence(Sequence(Optional('^'), FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_'), ZeroOrMore(
                    FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_'))),
                    tokens.get().add(Either.left(ImmutableIfDevName.newInstanceFromSourceName(match()).asString())));
        }

        Rule ArrayLimits(@NotNull Var<List<Either<String, Integer>>> tokens)
        {
            Var<Integer> lengthVar = new Var<>();
            return Sequence('[', OneOrMore(CharRange('0', '9')), lengthVar.set(Integer.parseInt(match())), ']',
                    tokens.get().add(Either.right(lengthVar.get())));
        }

    }
}
