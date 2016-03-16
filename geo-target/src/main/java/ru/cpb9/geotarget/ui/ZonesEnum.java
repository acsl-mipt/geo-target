package ru.cpb9.geotarget.ui;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

enum ZonesEnum {

    NORTH {
        @Override
        public Cursor setCursor() {
            return Cursor.N_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousey = e.getScreenY();
            double newHeight = startHeight + screenY - mousey;
            if (newHeight >= minHeight) {
                widget.setPrefHeight(newHeight);
                widget.setLayoutY(startLayoutY - newHeight + startHeight);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    EAST {
        @Override
        public Cursor setCursor() {
            return Cursor.E_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double newWidth = startWidth + (mousex - x);
            if (newWidth >= minWidth) {
                widget.setPrefWidth(newWidth);
                vbox.setPrefWidth(newWidth);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    SOUTH {
        @Override
        public Cursor setCursor() {
            return Cursor.S_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousey = e.getY();
            double newHeight = startHeight + (mousey - y);
            if (newHeight >= minHeight) {
                widget.setPrefHeight(newHeight);
                vbox.setPrefHeight(newHeight);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    WEST {
        @Override
        public Cursor setCursor() {
            return Cursor.W_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            // TODO Подумать над реализацией через скринХ, а не Х (везде, а не только тут)
            double mousex = e.getScreenX();
            double newWidth = startWidth + screenX - mousex;
            if (newWidth >= minWidth) {
                widget.setPrefWidth(newWidth);
                widget.setLayoutX(startLayoutX - newWidth + startWidth);
                vbox.setPrefWidth(newWidth);
                VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            }
        }
    },
    NORTH_EAST {
        @Override
        public Cursor setCursor() {
            return Cursor.NE_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double mousey = e.getScreenY();
            double newWidth, newHeight;
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
        }
    },
    SOUTH_EAST {
        @Override
        public Cursor setCursor() {
            return Cursor.SE_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousex = e.getX();
            double mousey = e.getY();
            double newWidth, newHeight;
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
        }
    },
    SOUTH_WEST {
        @Override
        public Cursor setCursor() {
            return Cursor.SW_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
            double mousex = e.getScreenX();
            double mousey = e.getY();
            double newWidth, newHeight;
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
        }
    },
    NORTH_WEST {
        @Override
        public Cursor setCursor() {
            return Cursor.NW_RESIZE;
        }

        @Override
        public void action (MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                            double x, double y, double screenX, double screenY,
                            double startLayoutX, double startLayoutY,
                            double minWidth, double minHeight, Widget widget, VBox vbox) {
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
        }
    };

    public abstract Cursor setCursor();
    public abstract void action(MouseEvent e, double RESIZE_MARGIN, double startWidth, double startHeight,
                                double x, double y, double screenX, double screenY,
                                double startLayoutX, double startLayoutY,
                                double minWidth, double minHeight, Widget widget, VBox vbox);

    public static Optional<ZonesEnum> findZone(MouseEvent event, double RESIZE_MARGIN, Widget widget) {
        if ((event.getY() < RESIZE_MARGIN && event.getX() < RESIZE_MARGIN)) {
            return Optional.of(NORTH_WEST);
        } else if ((event.getY() < RESIZE_MARGIN && event.getX() > (widget.getWidth() - RESIZE_MARGIN))) {
            return Optional.of(NORTH_EAST);
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() > (widget.getWidth() - RESIZE_MARGIN))) {
            return Optional.of(SOUTH_EAST);
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN) && event.getX() < RESIZE_MARGIN)) {
            return Optional.of(SOUTH_WEST);
        } else if ((event.getY() < RESIZE_MARGIN)) {
            return Optional.of(NORTH);
        } else if ((event.getX() > (widget.getWidth() - RESIZE_MARGIN))) {
            return Optional.of(EAST);
        } else if ((event.getY() > (widget.getHeight() - RESIZE_MARGIN))) {
            return Optional.of(SOUTH);
        } else if ((event.getX() < RESIZE_MARGIN)) {
            return Optional.of(WEST);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ZonesEnum> inZone(Widget widget) {
        if (widget.getCursor() == Cursor.NW_RESIZE) {
            return Optional.of(NORTH_WEST);
        } else if (widget.getCursor() == Cursor.NE_RESIZE) {
            return Optional.of(NORTH_EAST);
        } else if (widget.getCursor() == Cursor.SE_RESIZE) {
            return Optional.of(SOUTH_EAST);
        } else if (widget.getCursor() == Cursor.SW_RESIZE) {
            return Optional.of(SOUTH_WEST);
        } else if (widget.getCursor() == Cursor.N_RESIZE) {
            return Optional.of(NORTH);
        } else if (widget.getCursor() == Cursor.E_RESIZE) {
            return Optional.of(EAST);
        } else if (widget.getCursor() == Cursor.S_RESIZE) {
            return Optional.of(SOUTH);
        } else if (widget.getCursor() == Cursor.W_RESIZE) {
            return Optional.of(WEST);
        } else {
            return Optional.empty();
        }
    }

}
