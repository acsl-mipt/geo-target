package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.modeling.ModelingMessage;
import ru.cpb9.ifdev.modeling.TransformationMessage;
import ru.cpb9.ifdev.modeling.TransformationResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
class IfDevTransformationResult implements TransformationResult<IfDevRegistry>
{
    @NotNull
    Optional<IfDevRegistry> result = Optional.empty();
    @NotNull
    List<TransformationMessage> messages = new ArrayList<>();

    public IfDevTransformationResult(
            @NotNull IfDevRegistry registry)
    {
        result = Optional.of(registry);
    }

    @NotNull
    @Override
    public Optional<IfDevRegistry> getResult()
    {
        return result;
    }

    @NotNull
    @Override
    public List<TransformationMessage> getMessages()
    {
        return messages;
    }

    @Override
    public boolean hasError()
    {
        return messages.stream().filter(msg -> msg.getLevel().equals(ModelingMessage.Level.ERROR)).findAny()
                .isPresent();
    }
}
