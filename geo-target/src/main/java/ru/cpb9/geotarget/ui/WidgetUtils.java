package ru.cpb9.geotarget.ui;

public class WidgetUtils {

    public static double x, y, screenX, screenY;
    public static double startWidth, startHeight, minWidth, minHeight, startLayoutX, startLayoutY;

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
