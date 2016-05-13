package ru.mipt.acsl.geotarget.ui.controls;

import c10n.C10NMessages;
import c10n.annotations.En;
import c10n.annotations.Ru;

/**
 * @author Artem Shein
 */
@C10NMessages
interface Messages
{
    @En("Time")
    @Ru("Время")
    String time();

    @En("Trait")
    @Ru("Интерфейс")
    String trait();

    @En("Device")
    @Ru("Устройство")
    String device();

    @En("Status")
    @Ru("Статус")
    String status();

    @En("Value")
    @Ru("Значение")
    String value();

    @En("Devices")
    @Ru("Устройства")
    String devices();

    @En("Controls")
    @Ru("Элементы управления")
    String controls();

    @En("First-person view")
    @Ru("Вид от первого лица")
    String firstPersonView();

    @En("Signal level")
    @Ru("Уровень сигнала")
    String signalLevel();
}
