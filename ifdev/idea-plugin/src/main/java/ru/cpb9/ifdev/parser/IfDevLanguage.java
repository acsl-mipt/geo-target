package ru.cpb9.ifdev.parser;

import com.intellij.lang.Language;

/**
 * @author Artem Shein
 */
public class IfDevLanguage extends Language
{
    public static final IfDevLanguage INSTANCE = new IfDevLanguage();

    private IfDevLanguage()
    {
        super("IfDev");
    }
}
