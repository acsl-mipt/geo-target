package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevNamespaceAware
{
    @NotNull
    IfDevNamespace getNamespace();
}
