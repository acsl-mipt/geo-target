package ru.cpb9.geotarget.ui;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

enum ZonesEnum {

    NORTH_EAST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getY() < RESIZE_MARGIN && event.getX() > (widget.getWidth() - RESIZE_MARGIN));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.NE_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double mousey = e.getScreenY();
            double newWidth, newHeight;
            if (Math.abs(mousex - WidgetUtils.x) > Math.abs(mousey - WidgetUtils.y)) {
                double coefficient = 1 + (mousex - WidgetUtils.x) / WidgetUtils.startWidth;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            } else {
                double coefficient = 1 + (WidgetUtils.screenY - mousey) / WidgetUtils.startHeight;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            }
            if ((newWidth >= WidgetUtils.minWidth) && (newHeight >= WidgetUtils.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutY(WidgetUtils.startLayoutY - newHeight + WidgetUtils.startHeight);
                vbox.setPrefWidth(newWidth);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    SOUTH_EAST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() > (widget.getWidth() - RESIZE_MARGIN));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.SE_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double mousey = e.getY();
            double newWidth, newHeight;
            if (Math.abs(mousex - WidgetUtils.x) > Math.abs(mousey - WidgetUtils.y)) {
                double coefficient = 1 + (mousex - WidgetUtils.x) / WidgetUtils.startWidth;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            } else {
                double coefficient = 1 + (mousey - WidgetUtils.y) / WidgetUtils.startHeight;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            }
            if ((newWidth >= WidgetUtils.minWidth) && (newHeight >= WidgetUtils.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                vbox.setPrefWidth(newWidth);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    SOUTH_WEST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() < RESIZE_MARGIN);
        }

        @Override
        public Cursor setCursor() {
            return Cursor.SW_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousex = e.getScreenX();
            double mousey = e.getY();
            double newWidth, newHeight;
            if (Math.abs(WidgetUtils.screenX - mousex) > Math.abs(mousey - WidgetUtils.y)) {
                double coefficient = 1 + (WidgetUtils.screenX - mousex) / WidgetUtils.startWidth;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            } else {
                double coefficient = 1 + (mousey - WidgetUtils.y) / WidgetUtils.startHeight;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            }
            if ((newWidth >= WidgetUtils.minWidth) && (newHeight >= WidgetUtils.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutX(WidgetUtils.startLayoutX - newWidth + WidgetUtils.startWidth);
                vbox.setPrefWidth(newWidth);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    NORTH_WEST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getY() < RESIZE_MARGIN && event.getX() < RESIZE_MARGIN);
        }

        @Override
        public Cursor setCursor() {
            return Cursor.NW_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousex = e.getScreenX();
            double mousey = e.getScreenY();
            double newWidth, newHeight;
            if (Math.abs(WidgetUtils.screenX - mousex) > Math.abs(WidgetUtils.screenY - mousey)) {
                double coefficient = 1 + (WidgetUtils.screenX - mousex) / WidgetUtils.startWidth;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            } else {
                double coefficient = 1 + (WidgetUtils.screenY - mousey) / WidgetUtils.startHeight;
                newWidth = WidgetUtils.startWidth * coefficient;
                newHeight = WidgetUtils.startHeight * coefficient;
            }
            if ((newWidth >= WidgetUtils.minWidth) && (newHeight >= WidgetUtils.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutX(WidgetUtils.startLayoutX - newWidth + WidgetUtils.startWidth);
                widget.setLayoutY(WidgetUtils.startLayoutY - newHeight + WidgetUtils.startHeight);
                vbox.setPrefWidth(newWidth);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    NORTH {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return event.getY() < RESIZE_MARGIN;
        }

        @Override
        public Cursor setCursor() {
            return Cursor.N_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousey = e.getScreenY();
            double newHeight = WidgetUtils.startHeight + WidgetUtils.screenY - mousey;
            if (newHeight >= WidgetUtils.minHeight) {
                widget.setPrefHeight(newHeight);
                widget.setLayoutY(WidgetUtils.startLayoutY - newHeight + WidgetUtils.startHeight);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    EAST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getX() > (widget.getWidth() - RESIZE_MARGIN));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.E_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double newWidth = WidgetUtils.startWidth + (mousex - WidgetUtils.x);
            if (newWidth >= WidgetUtils.minWidth) {
                widget.setPrefWidth(newWidth);
                vbox.setPrefWidth(newWidth);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    SOUTH {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getY() > (widget.getHeight() - RESIZE_MARGIN));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.S_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            double mousey = e.getY();
            double newHeight = WidgetUtils.startHeight + (mousey - WidgetUtils.y);
            if (newHeight >= WidgetUtils.minHeight) {
                widget.setPrefHeight(newHeight);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    WEST {
        @Override
        public boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
            return (event.getX() < RESIZE_MARGIN);
        }

        @Override
        public Cursor setCursor() {
            return Cursor.W_RESIZE;
        }

        @Override
        public void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox) {
            // TODO Подумать над реализацией через скринХ, а не Х (везде, а не только тут)
            double mousex = e.getScreenX();
            double newWidth = WidgetUtils.startWidth + WidgetUtils.screenX - mousex;
            if (newWidth >= WidgetUtils.minWidth) {
                widget.setPrefWidth(newWidth);
                widget.setLayoutX(WidgetUtils.startLayoutX - newWidth + WidgetUtils.startWidth);
                vbox.setPrefWidth(newWidth);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    };

    public abstract Cursor setCursor();

    public abstract void action(MouseEvent e, double RESIZE_MARGIN, WidgetUtils widgetUtils, Widget widget, VBox vbox);

    public abstract boolean isInZone(MouseEvent event, double RESIZE_MARGIN, Widget widget);

    public static Optional<ZonesEnum> findZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
        for (ZonesEnum zone : ZonesEnum.values()) {
            if (zone.isInZone(event, RESIZE_MARGIN, widget)) {
                return Optional.of(zone);
            }
        }
        return Optional.empty();
    }
}
