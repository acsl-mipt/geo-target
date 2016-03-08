package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.decode.model.domain.component.messages.StatusMessage;
import ru.mipt.acsl.decode.model.domain.registry.Registry;
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
    public static final StatusMessage MOTION_ALL = MODEL_REGISTRY.statusMessageOrFail(MotionComponent.AllMessage.FQN);
    @NotNull
    public static final StatusMessage DEVICE_ALL = MODEL_REGISTRY.statusMessageOrFail(DeviceComponent.AllMessage.FQN);
    @NotNull
    public static final StatusMessage DOG_POINT_ALL = MODEL_REGISTRY.statusMessageOrFail(DogPointComponent.AllMessage.FQN);
    @NotNull
    public static final StatusMessage MODE_ALL = MODEL_REGISTRY.statusMessageOrFail(ModeComponent.AllMessage.FQN);
    @NotNull
    public static final StatusMessage ROUTE_ALL = MODEL_REGISTRY.statusMessageOrFail(RouteComponent.AllMessage.FQN);

    private KnownTmMessages()
    {

    }
}
