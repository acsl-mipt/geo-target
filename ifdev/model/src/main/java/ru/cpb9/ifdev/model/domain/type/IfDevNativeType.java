package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevBerType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Artem Shein
 */
public interface IfDevNativeType extends IfDevType
{
    Set<String> MANGLED_TYPE_NAMES = new HashSet<String>(){{ add(ImmutableIfDevBerType.MANGLED_NAME.asString()); }};
}
