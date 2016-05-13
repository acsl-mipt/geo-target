package ru.mipt.acsl.device.modeling.flying;

/**
 * @author Artem Shein
 */
public enum FlyingMode
{
    Off(0), // всё выключено
    PayloadReady(1), // готовность полезной нагрузки
    EngineReady(2), // готовность двигателя
    TakingOff(3), // взлетаем
    FreeFlying(4), // летим без маршрута
    RouteFlying(5), // летим маршруту
    Waiting(6), // находимся в режиме ожидания
    Returning(7), // летим домой (если отдельно не задан "дом", то летим в точку старта")
    Landing(8); // садимся

    private short code;

    FlyingMode(int code)
    {
        this.code = (short) code;
    }

    public short getCode()
    {
        return code;
    }
}
