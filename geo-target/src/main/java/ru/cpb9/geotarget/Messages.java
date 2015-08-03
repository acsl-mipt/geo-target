package ru.cpb9.geotarget;

import c10n.C10NMessages;
import c10n.annotations.En;
import c10n.annotations.Ru;

/**
 * @author Artem Shein
 */
@C10NMessages
interface Messages
{
    @En("Parameters table")
    @Ru("Таблица параметров")
    String parametersTable();

    @En("Parameters tree")
    @Ru("Дерево параметров")
    String parametersTree();

    @En("GeoTarget")
    @Ru("GeoTarget")
    String appName();

    @En("File")
    @Ru("Файл")
    String file();

    @En("Exit")
    @Ru("Выход")
    String exit();

    @En("Layers")
    @Ru("Слои")
    String layers();

    @En("Device list")
    @Ru("Список устройств")
    String deviceList();

    @En("Devices")
    @Ru("Устройства")
    String devices();

    @En("Device Tail")
    @Ru("Отображение пути")
    String deviceTail();

    @En("LatLon Graticule")
    @Ru("Сетка")
    String graticule();

    @En("Path Builder")
    @Ru("Построение маршрута")
    String pathBuilder();

    @En("Flight device")
    @Ru("Пилотажное устройство")
    String flightDevice();
}
