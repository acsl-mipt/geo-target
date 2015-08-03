package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.parser.IfDevFileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevFileTypeFactory extends FileTypeFactory
{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer)
    {
        consumer.consume(IfDevFileType.INSTANCE, "ifdev");
    }
}
