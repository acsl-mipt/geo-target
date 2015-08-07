package ru.cpb9.geotarget.ui;

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

    @En("Switch view")
    @Ru("Переключить вид")
    String switchToFlat();

    @En("Switch globe view")
    @Ru("Изменить вид глобуса")
    String switcher();

    @En("Up")
    @Ru("Вверх")
    String up();

    @En("Down")
    @Ru("Вниз")
    String down();

    @En("Elevation models")
    @Ru("Модели высот")
    String elevationModels();

    @En("Layers")
    @Ru("Слои")
    String layers();

    @En("Stars")
    @Ru("Звёзды")
    String stars();

    @En("Full size Earth image")
    @Ru("Полноразмерная картинка Земли")
    String fullSizeEarthImage();

    @En("Blue sphere")
    @Ru("Синяя сфера")
    String blueSphere();

    @En("Compass")
    @Ru("Компас")
    String compass();

    @En("Scale bar")
    @Ru("Размерная линейка")
    String scaleBar();

    @En("World map")
    @Ru("Мировая карта")
    String worldMap();

    @En("Sky gradient")
    @Ru("Небесный градиент")
    String skyGradient();

    @En("Sky")
    @Ru("Небесный градиент")
    String skyGr();

    @En("OpenStreetMap")
    @Ru("OpenStreetMap")
    String openStreetMap();

    @En("Clear all")
    @Ru("Очистить все")
    String clearAll();

    @En("Google Satellite")
    @Ru("Гугл Спутник")
    String googleSatellite();

    @En("Yandex Satellite")
    @Ru("Яндекс Спутник")
    String yandexSatellite();

    @En("Yandex Maps")
    @Ru("Яндекс Карты")
    String yandexMaps();

    @En("Google Maps")
    @Ru("Гугл Карты")
    String googleMaps();

    @En("Controls")
    @Ru("Элементы управления")
    String controls();

    @En("Google Landscape")
    @Ru("Гугл Ландшафт")
    String googleLandscape();

    @En("Yandex Narodnaya")
    @Ru("Яндекс Народная")
    String yandexNarodnaya();

    @En("Internal flying device model")
    @Ru("Внутренняя модель летающего аппарата")
    String internalFlyingDeviceModel();

    @En("Mavlink")
    @Ru("Мавлинк")
    String mavlink();

    @En("Add device")
    @Ru("Добавить устройство")
    String addDevice();

    @En("Device type:")
    @Ru("Тип устройства:")
    String deviceType();

    @En("Local port:")
    @Ru("Локальный порт:")
    String localPort();

    @En("Add")
    @Ru("Добавить")
    String add();
}
