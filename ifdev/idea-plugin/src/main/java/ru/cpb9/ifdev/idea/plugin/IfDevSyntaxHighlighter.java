package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.parser.IfDevLexerAdapter;
import ru.cpb9.ifdev.parser.psi.IfDevTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class IfDevSyntaxHighlighter extends SyntaxHighlighterBase
{
    private static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("IFDEV_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD);
    private static final TextAttributesKey ELEMENT_NAME = TextAttributesKey.createTextAttributesKey("IFDEV_ELEMENT_NAME",
            DefaultLanguageHighlighterColors.IDENTIFIER);
    private static final TextAttributesKey BRACE = TextAttributesKey.createTextAttributesKey("IFDEV_BRACE",
            DefaultLanguageHighlighterColors.BRACES);
    private static final TextAttributesKey SLASH = TextAttributesKey.createTextAttributesKey("IFDEV_SLASH",
            DefaultLanguageHighlighterColors.MARKUP_TAG);
    private static final TextAttributesKey BRACKET = TextAttributesKey.createTextAttributesKey("IFDEV_BRACKET",
            DefaultLanguageHighlighterColors.BRACKETS);
    private static final TextAttributesKey DOT = TextAttributesKey.createTextAttributesKey("IFDEV_DOT",
            DefaultLanguageHighlighterColors.DOT);
    private static final TextAttributesKey STAR = TextAttributesKey.createTextAttributesKey("IFDEV_STAR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN);
    private static final TextAttributesKey PAREN = TextAttributesKey.createTextAttributesKey("IFDEV_PAREN",
            DefaultLanguageHighlighterColors.PARENTHESES);
    private static final TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("IFDEV_COMMA",
            DefaultLanguageHighlighterColors.COMMA);
    private static final TextAttributesKey LINE_COMMENT = TextAttributesKey.createTextAttributesKey("IFDEV_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("IFDEV_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    private static final TextAttributesKey ELEMENT_ID = TextAttributesKey.createTextAttributesKey("IFDEV_ELEMENT_ID",
            DefaultLanguageHighlighterColors.IDENTIFIER);
    private static final TextAttributesKey NON_NEGATIVE_NUMBER = TextAttributesKey.createTextAttributesKey("IFDEV_NON_NEGATIVE_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER);
    private static final TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("IFDEV_BAD_CHARACTER",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
    private static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("IFDEV_STRING",
            DefaultLanguageHighlighterColors.STRING);

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private static final TextAttributesKey[] KEYWORD_KEYS = {KEYWORD};
    private static final TextAttributesKey[] BAD_CHARACTER_KEYS = {BAD_CHARACTER};
    private static final TextAttributesKey[] ELEMENT_NAME_KEYS = {ELEMENT_NAME};
    private static final TextAttributesKey[] ELEMENT_ID_KEYS = {ELEMENT_ID};
    private static final TextAttributesKey[] BRACES_KEYS = {BRACE};
    private static final TextAttributesKey[] PARENS_KEYS = {PAREN};
    private static final TextAttributesKey[] COMMA_KEYS = {COMMA};
    private static final TextAttributesKey[] LINE_COMMENT_KEYS ={LINE_COMMENT};
    private static final TextAttributesKey[] BLOCK_COMMENT_KEYS ={BLOCK_COMMENT};
    private static final TextAttributesKey[] BRACKETS_KEYS = {BRACKET};
    private static final TextAttributesKey[] SLASH_KEYS = {SLASH};
    private static final TextAttributesKey[] DOT_KEYS = {DOT};
    private static final Set<IElementType> KEYWORD_TOKENS = new HashSet<IElementType>(){{
        Collections.addAll(this, IfDevTypes.NAMESPACE, IfDevTypes.COMPONENT, IfDevTypes.COMMAND, IfDevTypes.DYNAMIC,
                IfDevTypes.EVENT, IfDevTypes.MESSAGE, IfDevTypes.STATUS, IfDevTypes.PARAMETER, IfDevTypes.INFO,
                IfDevTypes.ARRAY, IfDevTypes.BASE_TYPE, IfDevTypes.BOOL, IfDevTypes.ENUM, IfDevTypes.BER,
                IfDevTypes.FLOAT, IfDevTypes.INT, IfDevTypes.UINT, IfDevTypes.STRUCT, IfDevTypes.UNIT_TOKEN,
                IfDevTypes.TYPE_KEYWORD, IfDevTypes.SUBCOMPONENT, IfDevTypes.DISPLAY, IfDevTypes.PLACEMENT,
                IfDevTypes.BEFORE, IfDevTypes.AFTER, IfDevTypes.COLON, IfDevTypes.ALIAS, IfDevTypes.WITH);
    }};
    private static final TextAttributesKey[] NON_NEGATIVE_NUMBER_KEYS = {NON_NEGATIVE_NUMBER};
    private static final TextAttributesKey[] STAR_KEYS = {STAR};
    private static final TextAttributesKey[] STRING_KEYS = {STRING};

    @NotNull
    @Override
    public Lexer getHighlightingLexer()
    {
        return new IfDevLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
    {
        if (KEYWORD_TOKENS.contains(tokenType))
        {
            return KEYWORD_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.ELEMENT_NAME_TOKEN) || tokenType.equals(IfDevTypes.ESCAPED_NAME))
        {
            return ELEMENT_NAME_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.ELEMENT_ID))
        {
            return ELEMENT_ID_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.NON_NEGATIVE_NUMBER))
        {
            return NON_NEGATIVE_NUMBER_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.LEFT_BRACE) || tokenType.equals(IfDevTypes.RIGHT_BRACE))
        {
            return BRACES_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.LEFT_PAREN) || tokenType.equals(IfDevTypes.RIGHT_PAREN))
        {
            return PARENS_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.LEFT_BRACKET) || tokenType.equals(IfDevTypes.RIGHT_BRACKET))
        {
            return BRACKETS_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.COMMA))
        {
            return COMMA_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.SLASH))
        {
            return SLASH_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.DOT))
        {
            return DOT_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.STAR))
        {
            return STAR_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.COMMENT))
        {
            return LINE_COMMENT_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.MULTILINE_COMMENT))
        {
            return BLOCK_COMMENT_KEYS;
        }
        else if (tokenType.equals(IfDevTypes.STRING_VALUE) || tokenType.equals(IfDevTypes.STRING)
                || tokenType.equals(IfDevTypes.STRING_UNARY_QUOTES))
        {
            return STRING_KEYS;
        }
        else if (tokenType.equals(TokenType.BAD_CHARACTER))
        {
            return BAD_CHARACTER_KEYS;
        }
        else
        {
            return EMPTY_KEYS;
        }
    }
}
