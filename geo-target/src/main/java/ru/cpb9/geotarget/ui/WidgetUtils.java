package ru.cpb9.geotarget.ui;

import java.util.HashMap;

public class WidgetUtils {

    public static double x, y, screenX, screenY;
    public static double startWidth, startHeight, minWidth, minHeight, startLayoutX, startLayoutY;
    public static HashMap<String, Double> prefWidth = new HashMap<>();
    public static HashMap<String, Double> prefHeight = new HashMap<>();
//    public static double prefWidth, prefHight;

    public WidgetUtils(double x, double y, double screenX, double screenY, double startWidth, double startHeight, double minWidth, double minHeight, double startLayoutX, double startLayoutY) {
        WidgetUtils.x = x;
        WidgetUtils.y = y;
        WidgetUtils.screenX = screenX;
        WidgetUtils.screenY = screenY;
        WidgetUtils.startWidth = startWidth;
        WidgetUtils.startHeight = startHeight;
        WidgetUtils.minWidth = minWidth;
        WidgetUtils.minHeight = minHeight;
        WidgetUtils.startLayoutX = startLayoutX;
        WidgetUtils.startLayoutY = startLayoutY;
    }
}
