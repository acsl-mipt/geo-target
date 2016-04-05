package ru.cpb9.geotarget.ui.controls.parameters.tree;

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

    @En("Mcc")
    @Ru("Информация о борте")
    String tableTreeColumnMcc();

    @En("Information")
    @Ru("Информация")
    String tableTreeColumnInfo();

    @En("Time")
    @Ru("Время")
    String tableTreeColumnTime();

    @En("Value")
    @Ru("Значения")
    String tableTreeColumnValue();

    @En("Unit")
    @Ru("Единицы измерения")
    String tableTreeColumnUnit();
}
