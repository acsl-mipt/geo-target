package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.ModelRegistry;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.mipt.acsl.*;

/**
 * @author Artem Shein
 */
public final class KnownTmMessages
{
    @NotNull
    private static final IfDevRegistry MODEL_REGISTRY = ModelRegistry.getRegistry();
    @NotNull
    public static final IfDevMessage MOTION_ALL = MODEL_REGISTRY.getMessageOrThrow(MotionComponent.AllMessage.FQN);
    @NotNull
    public static final IfDevMessage DEVICE_ALL = MODEL_REGISTRY.getMessageOrThrow(DeviceComponent.AllMessage.FQN);
    @NotNull
    public static final IfDevMessage DOG_POINT_ALL = MODEL_REGISTRY.getMessageOrThrow(DogPointComponent.AllMessage.FQN);
    @NotNull
    public static final IfDevMessage MODE_ALL = MODEL_REGISTRY.getMessageOrThrow(ModeComponent.AllMessage.FQN);
    @NotNull
    public static final IfDevMessage ROUTE_ALL = MODEL_REGISTRY.getMessageOrThrow(RouteComponent.AllMessage.FQN);

    private KnownTmMessages()
    {

    }
}
