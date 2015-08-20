package ifdev;

import ru.cpb9.ifdev.model.domain.impl.ImmutableArraySize;
import ru.cpb9.ifdev.model.domain.type.ArraySize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public abstract class Array<T>
{
    private final ArraySize size;
    private final List<T> list = new ArrayList<>();

    protected Array(long from, long to)
    {
        this.size = new ImmutableArraySize(from, to);
    }
}
