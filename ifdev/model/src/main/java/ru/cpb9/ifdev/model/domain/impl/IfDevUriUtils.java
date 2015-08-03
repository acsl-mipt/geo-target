package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevFqn;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class IfDevUriUtils
{
    @NotNull
    public static URI getUriForNamespaceAndName(@NotNull IfDevFqn namespaceFqn, @NotNull IfDevName name)
    {
        List<IfDevName> namespaceNameParts = new ArrayList<>(namespaceFqn.getParts());
        namespaceNameParts.add(name);
        try
        {
            return URI.create("/" + URLEncoder.encode(String.join("/", namespaceNameParts.stream().map(IfDevName::asString).collect(
                    Collectors.toList())), Charsets.UTF_8.name()));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }

    public static List<String> getUriParts(@NotNull URI uri)
    {
        try
        {
            return Lists.newArrayList(
                    URLDecoder.decode(uri.getPath(), Charsets.UTF_8.name()).substring(1).split(Pattern.quote("/")));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }
}
