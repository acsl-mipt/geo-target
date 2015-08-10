package ru.cpb9.parsing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author Artem Shein
 */
public interface Parser<T>
{
    /**
     * @throws ParsingException if parsing fails
     * @throws IOException if reading fails
     * @param is input stream to parse
     * @return parsing result
     */
    @NotNull
    T parse(@NotNull InputStream is) throws IOException;

    @NotNull
    default T parse(@NotNull File inputFile) throws IOException
    {
        try (InputStream is = FileUtils.openInputStream(inputFile))
        {
            return parse(is);
        }
    }

    @NotNull
    default T parse(@NotNull String str) throws IOException
    {
        try (StringReader reader = new StringReader(str))
        {
            try (InputStream is = new ReaderInputStream(reader))
            {
                return parse(is);
            }
        }
    }
}
