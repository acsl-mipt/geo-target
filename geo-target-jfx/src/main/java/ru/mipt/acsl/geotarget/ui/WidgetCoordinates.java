package ru.mipt.acsl.geotarget.ui;

public class WidgetCoordinates {

    public double x, y, screenX, screenY;
    public double startWidth, startHeight, minWidth, minHeight, startLayoutX, startLayoutY;

    public WidgetCoordinates() {
    }

    public WidgetCoordinates(double x, double y, double screenX, double screenY, double startWidth, double startHeight,
                             double minWidth, double minHeight, double startLayoutX, double startLayoutY) {
        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
        this.startWidth = startWidth;
        this.startHeight = startHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.startLayoutX = startLayoutX;
        this.startLayoutY = startLayoutY;
    }

    // TODO Использовать pattern builder
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public double getStartWidth() {
        return startWidth;
    }

    public double getStartHeight() {
        return startHeight;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public double getStartLayoutX() {
        return startLayoutX;
    }

    public double getStartLayoutY() {
        return startLayoutY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setScreenX(double screenX) {
        this.screenX = screenX;
    }

    public void setScreenY(double screenY) {
        this.screenY = screenY;
    }

    public void setStartWidth(double startWidth) {
        this.startWidth = startWidth;
    }

    public void setStartHeight(double startHeight) {
        this.startHeight = startHeight;
    }

    public void setMinWidth(double minWidth) {
        this.minWidth = minWidth;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public void setStartLayoutX(double startLayoutX) {
        this.startLayoutX = startLayoutX;
    }

    public void setStartLayoutY(double startLayoutY) {
        this.startLayoutY = startLayoutY;
    }
}
