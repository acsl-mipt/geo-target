package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevStructField;
import ru.cpb9.ifdev.model.domain.type.IfDevStructType;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevStructType extends AbstractIfDevType implements IfDevStructType
{
    @NotNull
    private final List<IfDevStructField> fields;

    public static IfDevStructType newInstance(@NotNull Optional<IfDevName> name,
                                              @NotNull IfDevNamespace namespace, @NotNull Optional<String> info,
                                              @NotNull List<IfDevStructField> fields)
    {
        return new SimpleIfDevStructType(name, namespace, info, fields);
    }

    private SimpleIfDevStructType(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                  @NotNull Optional<String> info,
                                  @NotNull List<IfDevStructField> fields)
    {
        super(name, namespace, info);
        this.fields = ImmutableList.copyOf(fields);
    }

    @NotNull
    @Override
    public List<IfDevStructField> getFields()
    {
        return fields;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("SimpleIfDevStructType{name=%s, namespace=%s, info=%s, fields=%s}",
                name, namespace.getFqn(), info, fields);
    }
}
