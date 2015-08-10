package ru.cpb9.ifdev.parser;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import ru.cpb9.ifdev.model.domain.IfDevElement;
import ru.cpb9.parsing.Parser;
import ru.cpb9.parsing.ParsingException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Artem Shein
 */
public class IfDevParser implements Parser<IfDevElement>
{
    @NotNull
    public IfDevElement parse(@NotNull InputStream is) throws IOException
    {
        IfDevParboiledParser parser = Parboiled.createParser(IfDevParboiledParser.class);
        ParsingResult<IfDevElement> result = new ReportingParseRunner<IfDevElement>(parser.File()).run(
                IOUtils.toString(is, Charsets.UTF_8));
        if (!result.matched)
        {
            throw new ParsingException("syntax error");
        }
        if (result.hasErrors())
        {
            throw new ParsingException(result.parseErrors.toString());
        }
        return Preconditions.checkNotNull(result.resultValue);
    }
}
