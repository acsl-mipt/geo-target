package ru.cpb9.ifdev.parser;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Artem Shein
 */
public final class IfDevFileType extends LanguageFileType
{
    public static final IfDevFileType INSTANCE = new IfDevFileType();

    /**
     * Creates a language file type.
     */
    private IfDevFileType()
    {
        super(IfDevLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName()
    {
        return "IfDev file";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "IfDev device interface description";
    }

    @NotNull
    @Override
    public String getDefaultExtension()
    {
        return "edu.phystech.acsl.ifdev";
    }

    @Nullable
    @Override
    public Icon getIcon()
    {
        return IfDevIcons.FILE;
    }
}
