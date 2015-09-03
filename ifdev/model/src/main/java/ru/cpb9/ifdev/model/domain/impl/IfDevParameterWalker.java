package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.common.Either;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;

import java.util.Iterator;

/**
 * @author Artem Shein
 */
public class IfDevParameterWalker implements Iterator<Either<String, Integer>>
{
    @NotNull
    private final IfDevMessageParameter parameter;
    private int currentIndex;

    public IfDevParameterWalker(@NotNull IfDevMessageParameter parameter)
    {
        this.parameter = parameter;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext()
    {
        return currentIndex < parameter.getValue().length();
    }

    @Override
    public Either<String, Integer> next()
    {
        String value = parameter.getValue();
        int bracketIndex = value.indexOf('[', currentIndex);
        int endBracketIndex = value.indexOf(']', currentIndex);
        int dotIndex = value.indexOf('.', currentIndex);
        Either<String, Integer> result;
        if (bracketIndex != -1)
        {
            if (dotIndex != -1)
            {

            }
            else
            {
                result = Either.right(Integer.parseInt(value.));
            }
        }
        else if (dotIndex != -1)
        {
            result = Either.left(value.substring(currentIndex, dotIndex));
            currentIndex = dotIndex + 1;
        }
        else if (endBracketIndex != -1)
        {
            
        }
        else
        {
            result = Either.left(value.substring(currentIndex));
            currentIndex = value.length();
        }
        return result;
    }
}
