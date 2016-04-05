package ru.cpb9.device.modeling.flying;

/**
 * @author Artem Shein
 */
public enum SimplifiedRouteKind
{
    MANUAL(0), AUTO(1);

    private final int code;

    SimplifiedRouteKind(int code)
    {
        this.code = code;
    }
}
