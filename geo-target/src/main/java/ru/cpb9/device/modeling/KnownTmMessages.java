package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.ModelRegistry;
import ru.mipt.acsl.decode.model.domain.DecodeMessage;
import ru.mipt.acsl.decode.model.domain.DecodeRegistry;
import ru.mipt.acsl.*;

/**
 * @author Artem Shein
 */
public final class KnownTmMessages
{
    @NotNull
    private static final DecodeRegistry MODEL_REGISTRY = ModelRegistry.getRegistry();
    @NotNull
    public static final DecodeMessage MOTION_ALL = MODEL_REGISTRY.getMessageOrThrow(MotionComponent.AllMessage.FQN);
    @NotNull
    public static final DecodeMessage DEVICE_ALL = MODEL_REGISTRY.getMessageOrThrow(DeviceComponent.AllMessage.FQN);
    @NotNull
    public static final DecodeMessage DOG_POINT_ALL = MODEL_REGISTRY.getMessageOrThrow(DogPointComponent.AllMessage.FQN);
    @NotNull
    public static final DecodeMessage MODE_ALL = MODEL_REGISTRY.getMessageOrThrow(ModeComponent.AllMessage.FQN);
    @NotNull
    public static final DecodeMessage ROUTE_ALL = MODEL_REGISTRY.getMessageOrThrow(RouteComponent.AllMessage.FQN);

    private KnownTmMessages()
    {

    }
}
