package ru.cpb9.geotarget.ui;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

enum ZonesEnum {
    NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NONE;

    public static ZonesEnum findZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
        if ((event.getY() < RESIZE_MARGIN && event.getX() < RESIZE_MARGIN) || widget.getCursor() == Cursor.NW_RESIZE) {
            return NORTH_WEST;
        } else if ((event.getY() < RESIZE_MARGIN && event.getX() > (widget.getWidth() - RESIZE_MARGIN)) || widget.getCursor() == Cursor.NE_RESIZE) {
            return NORTH_EAST;
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() > (widget.getWidth() - RESIZE_MARGIN)) || widget.getCursor() == Cursor.SE_RESIZE) {
            return SOUTH_EAST;
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() < RESIZE_MARGIN) || widget.getCursor() == Cursor.SW_RESIZE) {
            return SOUTH_WEST;
        } else if ((event.getY() < RESIZE_MARGIN) || widget.getCursor() == Cursor.N_RESIZE) {
            return NORTH;
        } else if ((event.getX() > (widget.getWidth() - RESIZE_MARGIN)) || widget.getCursor() == Cursor.E_RESIZE) {
            return EAST;
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN)) || widget.getCursor() == Cursor.S_RESIZE) {
            return SOUTH;
        } else if ((event.getX() < RESIZE_MARGIN) || widget.getCursor() == Cursor.W_RESIZE) {
            return WEST;
        } else {
            return NONE;
        }
    }

    public static Cursor setCursor (MouseEvent e, double RESIZE_MARGIN, Widget widget) {
        ZonesEnum zone = ZonesEnum.findZone(e, RESIZE_MARGIN, widget);
        switch (zone) {
            case NORTH_WEST:
                return Cursor.NW_RESIZE;
            case NORTH_EAST:
                return Cursor.NE_RESIZE;
            case SOUTH_EAST:
                return Cursor.SE_RESIZE;
            case SOUTH_WEST:
                return Cursor.SW_RESIZE;
            case NORTH:
                return Cursor.N_RESIZE;
            case EAST:
                return Cursor.E_RESIZE;
            case SOUTH:
                return Cursor.S_RESIZE;
            case WEST:
                return Cursor.W_RESIZE;
            case NONE:
                return Cursor.DEFAULT;
            default:
                return Cursor.DEFAULT;
        }
    }

    public static void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                               double x, double y, double screenX, double screenY,
                               double startLayoutX, double startLayoutY,
                               double minWidth, double minHeight, Widget widget, VBox vbox) {
        ZonesEnum zone = ZonesEnum.findZone(e, RESIZE_MARGIN, widget);
        switch (zone) {
            case NORTH_WEST:
                double mousex = e.getScreenX();
                double mousey = e.getScreenY();
                double newWidth, newHeight;
                if (Math.abs(screenX - mousex) > Math.abs(screenY - mousey)) {
                    double coefficient = 1 + (screenX - mousex) / startWidth;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                } else {
                    double coefficient = 1 + (screenY - mousey) / startHeight;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                }
                if ((newWidth >= minWidth) && (newHeight >= minHeight)) {
                    widget.setPrefWidth(newWidth);
                    widget.setPrefHeight(newHeight);
                    widget.setLayoutX(startLayoutX - newWidth + startWidth);
                    widget.setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case NORTH_EAST:
                mousex = e.getX();
                mousey = e.getScreenY();
                if (Math.abs(mousex - x) > Math.abs(mousey - y)) {
                    double coefficient = 1 + (mousex - x) / startWidth;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                } else {
                    double coefficient = 1 + (screenY - mousey) / startHeight;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                }
                if ((newWidth >= minWidth) && (newHeight >= minHeight)) {
                    widget.setPrefWidth(newWidth);
                    widget.setPrefHeight(newHeight);
                    widget.setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case SOUTH_EAST:
                mousex = e.getX();
                mousey = e.getY();
                if (Math.abs(mousex - x) > Math.abs(mousey - y)) {
                    double coefficient = 1 + (mousex - x) / startWidth;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                } else {
                    double coefficient = 1 + (mousey - y) / startHeight;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                }
                if ((newWidth >= minWidth) && (newHeight >= minHeight)) {
                    widget.setPrefWidth(newWidth);
                    widget.setPrefHeight(newHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case SOUTH_WEST:
                mousex = e.getScreenX();
                mousey = e.getY();
                if (Math.abs(screenX - mousex) > Math.abs(mousey - y)) {
                    double coefficient = 1 + (screenX - mousex) / startWidth;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                } else {
                    double coefficient = 1 + (mousey - y) / startHeight;
                    newWidth = startWidth * coefficient;
                    newHeight = startHeight * coefficient;
                }
                if ((newWidth >= minWidth) && (newHeight >= minHeight)) {
                    widget.setPrefWidth(newWidth);
                    widget.setPrefHeight(newHeight);
                    widget.setLayoutX(startLayoutX - newWidth + startWidth);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case NORTH:
                mousey = e.getScreenY();
                newHeight = startHeight + screenY - mousey;
                if (newHeight >= minHeight) {
                    widget.setPrefHeight(newHeight);
                    widget.setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case EAST:
                mousex = e.getX();
                newWidth = startWidth + (mousex - x);
                if (newWidth >= minWidth) {
                    widget.setPrefWidth(newWidth);
                    vbox.setPrefWidth(newWidth);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case SOUTH:
                mousey = e.getY();
                newHeight = startHeight + (mousey - y);
                if (newHeight >= minHeight) {
                    widget.setPrefHeight(newHeight);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
            case WEST:
                // TODO Подумать над реализацией через скринХ, а не Х (везде, а не только тут)
                mousex = e.getScreenX();
                newWidth = startWidth + screenX - mousex;
                if (newWidth >= minWidth) {
                    widget.setPrefWidth(newWidth);
                    widget.setLayoutX(startLayoutX - newWidth + startWidth);
                    vbox.setPrefWidth(newWidth);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
                break;
        }
    }
}
