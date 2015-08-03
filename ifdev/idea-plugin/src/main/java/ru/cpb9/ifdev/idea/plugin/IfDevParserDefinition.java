package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.parser.IfDevLanguage;
import ru.cpb9.ifdev.parser.IfDevLexerAdapter;
import ru.cpb9.ifdev.parser.psi.IfDevFile;
import ru.cpb9.ifdev.parser.IfDevParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import ru.cpb9.ifdev.parser.psi.IfDevTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevParserDefinition implements ParserDefinition
{
    private static final IFileElementType FILE = new IFileElementType(
            Language.findInstance(IfDevLanguage.class));
    private static final TokenSet WHITESPACES = TokenSet.create(TokenType.WHITE_SPACE, IfDevTypes.COMMENT, IfDevTypes.MULTILINE_COMMENT);
    private static final TokenSet COMMENTS = TokenSet.create(IfDevTypes.COMMENT, IfDevTypes.MULTILINE_COMMENT);

    @NotNull
    @Override
    public Lexer createLexer(Project project)
    {
        return new IfDevLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project)
    {
        return new IfDevParser();
    }

    @Override
    public IFileElementType getFileNodeType()
    {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens()
    {
        return WHITESPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens()
    {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements()
    {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node)
    {
        return IfDevTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider)
    {
        return new IfDevFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
    {
        return SpaceRequirements.MAY;
    }
}
