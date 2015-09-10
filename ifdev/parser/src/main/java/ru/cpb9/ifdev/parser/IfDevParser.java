package ru.cpb9.ifdev.parser;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;
import ru.cpb9.ifdev.model.domain.IfDevElement;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.parsing.Parser;
import ru.cpb9.parsing.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class IfDevParser implements Parser<IfDevElement>
{
    @NotNull
    public IfDevElement parse(@NotNull InputStream is)
    {
        IfDevParboiledParser parser = Parboiled.createParser(IfDevParboiledParser.class);
        try
        {
            ParsingResult<IfDevElement> result = new TracingParseRunner<IfDevElement>(parser.File()).run(
                    IOUtils.toString(is, Charsets.UTF_8));
            if (!result.matched || result.hasErrors())
            {
                throw new ParsingException(result.parseErrors.stream().map(ParboiledErrorToStringWrapper::new).collect(
                        Collectors.toList()).toString());
            }
            return Preconditions.checkNotNull(result.resultValue);
        }
        catch (IOException e)
        {
            throw new ParsingException(e);
        }
    }
}
