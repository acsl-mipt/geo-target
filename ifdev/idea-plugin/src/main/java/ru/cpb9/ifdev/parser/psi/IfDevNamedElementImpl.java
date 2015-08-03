package ru.cpb9.ifdev.parser.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public abstract class IfDevNamedElementImpl extends ASTWrapperPsiElement implements IfDevNamedElement
{
    public IfDevNamedElementImpl(@NotNull ASTNode node)
    {
        super(node);
    }
}
