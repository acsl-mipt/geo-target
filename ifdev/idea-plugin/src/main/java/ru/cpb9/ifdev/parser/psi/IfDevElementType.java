package ru.cpb9.ifdev.parser.psi;

import ru.cpb9.ifdev.parser.IfDevLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevElementType extends IElementType
{
    public IfDevElementType(@NotNull String debugName)
    {
        super(debugName, IfDevLanguage.INSTANCE);
    }
}
