package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.decode.model.domain.Message;
import ru.mipt.acsl.decode.model.domain.Registry;
import ru.mipt.acsl.*;
import ru.mipt.acsl.geotarget.ModelRegistry;

/**
 * @author Artem Shein
 */
public final class KnownTmMessages
{
    @NotNull
    private static final Registry MODEL_REGISTRY = ModelRegistry.registry();
    @NotNull
    public static final Message MOTION_ALL = MODEL_REGISTRY.messageOrFail(MotionComponent.AllMessage.FQN);
    @NotNull
    public static final Message DEVICE_ALL = MODEL_REGISTRY.messageOrFail(DeviceComponent.AllMessage.FQN);
    @NotNull
    public static final Message DOG_POINT_ALL = MODEL_REGISTRY.messageOrFail(DogPointComponent.AllMessage.FQN);
    @NotNull
    public static final Message MODE_ALL = MODEL_REGISTRY.messageOrFail(ModeComponent.AllMessage.FQN);
    @NotNull
    public static final Message ROUTE_ALL = MODEL_REGISTRY.messageOrFail(RouteComponent.AllMessage.FQN);

    private KnownTmMessages()
    {

    }
}
