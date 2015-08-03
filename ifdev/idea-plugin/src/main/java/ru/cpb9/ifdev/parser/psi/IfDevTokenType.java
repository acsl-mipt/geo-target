package ru.cpb9.ifdev.parser.psi;

import ru.cpb9.ifdev.parser.IfDevLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevTokenType extends IElementType
{
    public IfDevTokenType(@NotNull String debugName)
    {
        super(debugName, IfDevLanguage.INSTANCE);
    }

    @Override
    public String toString()
    {
        return "IfDevTokenType." + super.toString();
    }
}
