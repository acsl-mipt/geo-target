package ru.cpb9.geotarget.ui;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import java.util.Arrays;
import java.util.Optional;

enum ResizeZone {

    NORTH_EAST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getY() < Widget.getResizeMargin() && event.getX() > (widget.getWidth() - Widget.getResizeMargin()));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.NE_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousex = e.getX();
            double mousey = e.getScreenY();
            double newWidth, newHeight;

            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();

            if (Math.abs(mousex - widgetCoordinates.x) > Math.abs(mousey - widgetCoordinates.y)) {
                double coefficient = 1 + (mousex - widgetCoordinates.x) / widgetCoordinates.startWidth;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            } else {
                double coefficient = 1 + (widgetCoordinates.screenY - mousey) / widgetCoordinates.startHeight;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            }
            if ((newWidth >= widgetCoordinates.minWidth) && (newHeight >= widgetCoordinates.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutY(widgetCoordinates.startLayoutY - newHeight + widgetCoordinates.startHeight);
                widget.getVbox().setPrefWidth(newWidth);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    SOUTH_EAST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getY() > (widget.getHeight() - Widget.getResizeMargin()) && event.getX() > (widget.getWidth() - Widget.getResizeMargin()));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.SE_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousex = e.getX();
            double mousey = e.getY();
            double newWidth, newHeight;
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();

            if (Math.abs(mousex - widgetCoordinates.x) > Math.abs(mousey - widgetCoordinates.y)) {
                double coefficient = 1 + (mousex - widgetCoordinates.x) / widgetCoordinates.startWidth;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            } else {
                double coefficient = 1 + (mousey - widgetCoordinates.y) / widgetCoordinates.startHeight;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            }
            if ((newWidth >= widgetCoordinates.minWidth) && (newHeight >= widgetCoordinates.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.getVbox().setPrefWidth(newWidth);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    SOUTH_WEST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getY() > (widget.getHeight() - Widget.getResizeMargin()) && event.getX() < Widget.getResizeMargin());
        }

        @Override
        public Cursor setCursor() {
            return Cursor.SW_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousex = e.getScreenX();
            double mousey = e.getY();
            double newWidth, newHeight;
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();

            if (Math.abs(widgetCoordinates.screenX - mousex) > Math.abs(mousey - widgetCoordinates.y)) {
                double coefficient = 1 + (widgetCoordinates.screenX - mousex) / widgetCoordinates.startWidth;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            } else {
                double coefficient = 1 + (mousey - widgetCoordinates.y) / widgetCoordinates.startHeight;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            }
            if ((newWidth >= widgetCoordinates.minWidth) && (newHeight >= widgetCoordinates.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutX(widgetCoordinates.startLayoutX - newWidth + widgetCoordinates.startWidth);
                widget.getVbox().setPrefWidth(newWidth);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    NORTH_WEST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getY() < Widget.getResizeMargin() && event.getX() < Widget.getResizeMargin());
        }

        @Override
        public Cursor setCursor() {
            return Cursor.NW_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousex = e.getScreenX();
            double mousey = e.getScreenY();
            double newWidth, newHeight;
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();

            if (Math.abs(widgetCoordinates.screenX - mousex) > Math.abs(widgetCoordinates.screenY - mousey)) {
                double coefficient = 1 + (widgetCoordinates.screenX - mousex) / widgetCoordinates.startWidth;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            } else {
                double coefficient = 1 + (widgetCoordinates.screenY - mousey) / widgetCoordinates.startHeight;
                newWidth = widgetCoordinates.startWidth * coefficient;
                newHeight = widgetCoordinates.startHeight * coefficient;
            }
            if ((newWidth >= widgetCoordinates.minWidth) && (newHeight >= widgetCoordinates.minHeight)) {
                widget.setPrefWidth(newWidth);
                widget.setPrefHeight(newHeight);
                widget.setLayoutX(widgetCoordinates.startLayoutX - newWidth + widgetCoordinates.startWidth);
                widget.setLayoutY(widgetCoordinates.startLayoutY - newHeight + widgetCoordinates.startHeight);
                widget.getVbox().setPrefWidth(newWidth);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    NORTH {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return event.getY() < Widget.getResizeMargin();
        }

        @Override
        public Cursor setCursor() {
            return Cursor.N_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousey = e.getScreenY();
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();
            double newHeight = widgetCoordinates.startHeight + widgetCoordinates.screenY - mousey;

            if (newHeight >= widgetCoordinates.minHeight) {
                widget.setPrefHeight(newHeight);
                widget.setLayoutY(widgetCoordinates.startLayoutY - newHeight + widgetCoordinates.startHeight);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    EAST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getX() > (widget.getWidth() - Widget.getResizeMargin()));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.E_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousex = e.getX();
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();
            double newWidth = widgetCoordinates.startWidth + (mousex - widgetCoordinates.x);

            if (newWidth >= widgetCoordinates.minWidth) {
                widget.setPrefWidth(newWidth);
                widget.getVbox().setPrefWidth(newWidth);
            }
        }
    },

    SOUTH {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getY() > (widget.getHeight() - Widget.getResizeMargin()));
        }

        @Override
        public Cursor setCursor() {
            return Cursor.S_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            double mousey = e.getY();
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();
            double newHeight = widgetCoordinates.startHeight + (mousey - widgetCoordinates.y);

            if (newHeight >= widgetCoordinates.minHeight) {
                widget.setPrefHeight(newHeight);
                widget.getVbox().setPrefHeight(newHeight);
            }
        }
    },

    WEST {
        @Override
        public boolean isInZone(MouseEvent event, Widget widget) {
            return (event.getX() < Widget.getResizeMargin());
        }

        @Override
        public Cursor setCursor() {
            return Cursor.W_RESIZE;
        }

        @Override
        public void action(MouseEvent e, Widget widget) {
            // TODO Подумать над реализацией через скринХ, а не Х (везде, а не только тут)
            double mousex = e.getScreenX();
            WidgetCoordinates widgetCoordinates = widget.getWidgetCoordinates();
            double newWidth = widgetCoordinates.startWidth + widgetCoordinates.screenX - mousex;

            if (newWidth >= widgetCoordinates.minWidth) {
                widget.setPrefWidth(newWidth);
                widget.setLayoutX(widgetCoordinates.startLayoutX - newWidth + widgetCoordinates.startWidth);
                widget.getVbox().setPrefWidth(newWidth);
            }
        }
    };

    public abstract Cursor setCursor();

    public abstract void action(MouseEvent e, Widget widget);

    public abstract boolean isInZone(MouseEvent event, Widget widget);

    public static Optional<ResizeZone> findZone(MouseEvent event, Widget widget) {
        return Arrays.stream(ResizeZone.values()).filter(z -> z.isInZone(event, widget)).findAny();
    }
}
