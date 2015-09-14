package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.ModelRegistry;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

/**
 * @author Artem Shein
 */
public final class KnownTmMessages
{
    @NotNull
    private static final IfDevRegistry MODEL_REGISTRY = ModelRegistry.getRegistry();
    @NotNull
    public static final IfDevMessage MOTION_ALL = MODEL_REGISTRY.getMessage(TmMessageFqn.MOTION_ALL.toString()).orElseThrow(
            AssertionError::new);
    @NotNull
    public static final IfDevMessage DEVICE_ALL = MODEL_REGISTRY.getMessage(TmMessageFqn.DEVICE_ALL.toString()).orElseThrow(AssertionError::new);
    @NotNull
    public static final IfDevMessage DOG_POINT_ALL = MODEL_REGISTRY.getMessage(TmMessageFqn.DOG_POINT_ALL.toString()).orElseThrow(AssertionError::new);
    public static final IfDevMessage MODE_ALL = MODEL_REGISTRY.getMessage(TmMessageFqn.MODE_ALL.toString()).orElseThrow(AssertionError::new);
    public static final IfDevMessage ROUTE_ALL =
            MODEL_REGISTRY.getMessage(TmMessageFqn.ROUTE_ALL.toString()).orElseThrow(AssertionError::new);

    private KnownTmMessages()
    {

    }
}
