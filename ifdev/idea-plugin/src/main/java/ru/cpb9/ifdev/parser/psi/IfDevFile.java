package ru.cpb9.ifdev.parser.psi;

import ru.cpb9.ifdev.parser.IfDevFileType;
import ru.cpb9.ifdev.parser.IfDevLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevFile extends PsiFileBase
{
    public IfDevFile(@NotNull FileViewProvider viewProvider)
    {
        super(viewProvider, IfDevLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType()
    {
        return IfDevFileType.INSTANCE;
    }

    @Override
    public String toString()
    {
        return "Device interface definition file";
    }

}
