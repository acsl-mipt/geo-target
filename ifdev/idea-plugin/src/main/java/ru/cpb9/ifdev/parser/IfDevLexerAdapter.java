package ru.cpb9.ifdev.parser;

import com.intellij.lexer.FlexAdapter;

/**
 * @author Artem Shein
 */
public class IfDevLexerAdapter extends FlexAdapter
{
    public IfDevLexerAdapter()
    {
        super(new _IfDevLexer());
    }
}
