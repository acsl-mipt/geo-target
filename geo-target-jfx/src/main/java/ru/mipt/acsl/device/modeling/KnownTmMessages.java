package ru.mipt.acsl.device.modeling;

import ru.mipt.acsl.*;
import ru.mipt.acsl.decode.model.component.message.StatusMessage;
import ru.mipt.acsl.decode.model.registry.Registry;
import ru.mipt.acsl.decode.model.registry.package$;
import ru.mipt.acsl.geotarget.OnBoardModelRegistry;

/**
 * @author Artem Shein
 */
public class KnownTmMessages {

    private static final Registry REGISTRY = OnBoardModelRegistry.registry();

    public static final StatusMessage MotionAll = getStatusMessageOrFail(MotionComponent.AllMessage.FQN);
    public static final StatusMessage DeviceAll = getStatusMessageOrFail(DeviceComponent.AllMessage.FQN);
    public static final StatusMessage DogPointAll = getStatusMessageOrFail(DogPointComponent.AllMessage.FQN);
    public static final StatusMessage ModeAll = getStatusMessageOrFail(ModeComponent.AllMessage.FQN);
    public static final StatusMessage RouteAll = getStatusMessageOrFail(RouteComponent.AllMessage.FQN);

    private static StatusMessage getStatusMessageOrFail(String fqn) {
        return package$.MODULE$.RegistryHelper(REGISTRY).statusMessageOrFail(fqn);
    }

    // Prevent from instantiation
    private KnownTmMessages() {

    }
}
