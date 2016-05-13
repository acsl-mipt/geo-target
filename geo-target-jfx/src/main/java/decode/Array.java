package decode;

import ru.mipt.acsl.decode.model.types.ArraySize;
import ru.mipt.acsl.decode.model.types.ArraySize$;

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
        this.size = ArraySize$.MODULE$.apply(from, to);
    }
}
