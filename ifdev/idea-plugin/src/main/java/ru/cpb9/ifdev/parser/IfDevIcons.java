package ru.cpb9.ifdev.parser;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author Artem Shein
 */
public final class IfDevIcons
{
    public static final Icon FILE = IconLoader.getIcon("/ru/cpb9/ifdev/idea/plugin/icons/ifdev_file_icon.png");

    // Deny instantiation
    private IfDevIcons()
    {
        throw new AssertionError();
    }
}
