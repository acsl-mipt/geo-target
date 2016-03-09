package ru.cpb9.geotarget.ui;

import com.google.common.base.Preconditions;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class Widget extends Region {
    private static final Logger LOG = LoggerFactory.getLogger(Widget.class);
    private static final double STICKING_WIDTH = 20.;
    private static final int RESIZE_MARGIN = 5;
    public static double opacity = 0.7;
    private final String title;
    private final Label titleLabel;
    private final VBox vbox;
    private final AnchorPane headerBox;
    private final Button closeButton;
    private final Button minMaxButton;
    private final Button opacitySliderButton;
    private double startValue;
    private double startCoordinateY;
    private double newOpacity;
    private double x, y, screenX, screenY;
    private double startWidth, startHeight, minWidth, minHeight, startLayoutX, startLayoutY;
    ;
    private boolean dragging;
    private boolean initMinHeight;
    private boolean initMinWidth;
    @NotNull
    private Optional<Node> content = Optional.empty();
    @NotNull
    private StickMode stickMode = StickMode.NONE;

    public Widget(@NotNull String title, @NotNull Node content) {
        this(title);
        setContent(content);
    }

    public Widget(@NotNull String title) {
        this.title = title;
        titleLabel = new Label(title);
        closeButton = new Button("x");
        minMaxButton = new Button("o");
        minMaxButton.setOnAction((e) -> {
            if (isMinimized()) {
                maximize();
            } else if (isMaximized()) {
                setMinSize(0.0, 0.0);
                minimize();
            }
        });
        opacitySliderButton = new Button("-");
        opacitySliderButton.setOnMousePressed(event -> {
            startCoordinateY = event.getSceneY();
            startValue = opacity;
        });
        opacitySliderButton.setOnMouseDragged(event_moved -> {
            newOpacity = startValue + (startCoordinateY - event_moved.getSceneY()) / 100;
            if (newOpacity < 0.1) {
                newOpacity = 0.1;
            } else if (newOpacity > 1) {
                newOpacity = 1;
            }
            setOpacity(newOpacity);
        });
        opacitySliderButton.setOnMouseReleased(event -> {
            opacity = newOpacity;
        });
        HBox buttonsBox = new HBox(opacitySliderButton, minMaxButton, closeButton);
        headerBox = new AnchorPane(titleLabel, buttonsBox);
        AnchorPane.setLeftAnchor(titleLabel, 5.);
        AnchorPane.setRightAnchor(buttonsBox, 5.);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        headerBox.setMaxWidth(Double.MAX_VALUE);
        vbox = new VBox(headerBox);
        getChildren().add(vbox);
        setStyle("-fx-background-color: #ccc;-fx-border-width: 1;-fx-border-color: #444;-fx-border-style: solid");
        setOpacity(opacity);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(5);
        vbox.setFillWidth(true);
        final Delta dragDelta = new Delta();
        setOnMouseMoved(e ->
        {
            // TODO Тут тоже как-то свич может можно прикрутить?
            if (isInNWDraggableZone(e)) {
                setCursor(Cursor.NW_RESIZE);
            } else if (isInNEDraggableZone(e)) {
                setCursor(Cursor.NE_RESIZE);
            } else if (isInSEDraggableZone(e)) {
                setCursor(Cursor.SE_RESIZE);
            } else if (isInSWDraggableZone(e)) {
                setCursor(Cursor.SW_RESIZE);
            } else if (isInNDraggableZone(e)) {
                setCursor(Cursor.N_RESIZE);
            } else if (isInEDraggableZone(e)) {
                setCursor(Cursor.E_RESIZE);
            } else if (isInSDraggableZone(e)) {
                setCursor(Cursor.S_RESIZE);
            } else if (isInWDraggableZone(e)) {
                setCursor(Cursor.W_RESIZE);
            } else {
                setCursor(Cursor.DEFAULT);
            }
        });
        setOnMousePressed(e ->
        {
            if (!isInNWDraggableZone(e) && !isInNEDraggableZone(e) && !isInSEDraggableZone(e) && !isInSWDraggableZone(e)
                    && !isInNDraggableZone(e) && !isInEDraggableZone(e) && !isInSDraggableZone(e) &&
                    !isInWDraggableZone(e)) {
                return;
            }

            if (isMinimized()) {
                return;
            }

            dragging = true;

            // make sure that the minimum height is set to the current height once,
            // setting a min height that is smaller than the current height will
            // have no effect
            if (!initMinHeight) {
                setMinHeight(getHeight());
                minHeight = getHeight();
                initMinHeight = true;
            }

            if (!initMinWidth) {
                setMinWidth(getWidth());
                minWidth = getWidth();
                initMinWidth = true;
            }
            startHeight = getHeight();
            startWidth = getWidth();
            startLayoutX = getLayoutX();
            startLayoutY = getLayoutY();
            setMinSize(minWidth, minHeight);
            setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());
            vbox.setMinSize(minWidth, minHeight);
            vbox.setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());
            x = e.getX();
            y = e.getY();
            screenX = e.getScreenX();
            screenY = e.getScreenY();
        });
        setOnMouseReleased(e ->
        {
            dragging = false;
            setCursor(Cursor.DEFAULT);
        });
        setOnMouseDragged(e ->
        {
            if (!dragging) {
                return;
            }
            // TODO Тут наверное тоже свич нужен
            if (getCursor() == Cursor.E_RESIZE) {
                double mousex = e.getX();
                double newWidth;
                newWidth = startWidth + (mousex - x);
                if (newWidth >= minWidth) {
                    setPrefWidth(newWidth);
                    vbox.setPrefWidth(newWidth);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.SE_RESIZE) {
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
                    setPrefWidth(newWidth);
                    setPrefHeight(newHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.S_RESIZE) {
                double mousey = e.getY();
                double newHeight;
                newHeight = startHeight + (mousey - y);
                if (newHeight >= minHeight) {
                    setPrefHeight(newHeight);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.SW_RESIZE) {
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
                    setPrefWidth(newWidth);
                    setPrefHeight(newHeight);
                    setLayoutX(startLayoutX - newWidth + startWidth);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.W_RESIZE) {
                // TODO Скорее всего правильно везде делать через скринХ, а не Х
                double mousex = e.getScreenX();
                double newWidth;
                newWidth = startWidth + screenX - mousex;
                if (newWidth >= minWidth) {
                    setPrefWidth(newWidth);
                    setLayoutX(startLayoutX - newWidth + startWidth);
                    vbox.setPrefWidth(newWidth);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.NW_RESIZE) {
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
                    setPrefWidth(newWidth);
                    setPrefHeight(newHeight);
                    setLayoutX(startLayoutX - newWidth + startWidth);
                    setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.N_RESIZE) {
                double mousey = e.getScreenY();
                double newHeight;
                newHeight = startHeight + screenY - mousey;
                if (newHeight >= minHeight) {
                    setPrefHeight(newHeight);
                    setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            } else if (getCursor() == Cursor.NE_RESIZE) {
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
                    setPrefWidth(newWidth);
                    setPrefHeight(newHeight);
                    setLayoutY(startLayoutY - newHeight + startHeight);
                    vbox.setPrefWidth(newWidth);
                    vbox.setPrefHeight(newHeight);
                    VBox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
                }
            }
        });
        vbox.setOnMousePressed(e ->
        {
            if (isInSEDraggableZone(e)) {
                return;
            }

            dragDelta.x = getLayoutX() - e.getScreenX();
            dragDelta.y = getLayoutY() - e.getScreenY();
            setOpacity(0.9);
            toFront();
        });
        vbox.setOnMouseDragged(e ->
        {
            if (dragging) {
                return;
            }

            setLayoutX(e.getScreenX() + dragDelta.x);
            setLayoutY(e.getScreenY() + dragDelta.y);

            if ((getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH) &&
                    (getLayoutY() < STICKING_WIDTH)) {
                setStickMode(StickMode.RIGHT_TOP);
            } else if ((getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH) &&
                    (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH)) {
                setStickMode(StickMode.RIGHT_BOTTOM);
            } else if ((getLayoutX() < STICKING_WIDTH) &&
                    (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH)) {
                setStickMode(StickMode.LEFT_BOTTOM);
            } else if ((getLayoutX() < STICKING_WIDTH) && (getLayoutY() < STICKING_WIDTH)) {
                setStickMode(StickMode.LEFT_TOP);
            } else if (getLayoutX() < STICKING_WIDTH) {
                setStickMode(StickMode.LEFT);
            } else if (getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH) {
                setStickMode(StickMode.RIGHT);
            } else if (getLayoutY() < STICKING_WIDTH) {
                setStickMode(StickMode.TOP);
            } else if (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH) {
                setStickMode(StickMode.BOTTOM);
            } else {
                setStickMode(StickMode.NONE);
            }
            updateSticking();
        });
        vbox.setOnMouseReleased(e -> setOpacity(opacity));
    }

    // TODO Это лучше свичом сделать?
    private boolean isInNWDraggableZone(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN && event.getX() < RESIZE_MARGIN;
    }

    private boolean isInNDraggableZone(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN;
    }

    private boolean isInNEDraggableZone(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN && event.getX() > (getWidth() - RESIZE_MARGIN);
    }

    private boolean isInEDraggableZone(MouseEvent event) {
        return event.getX() > (getWidth() - RESIZE_MARGIN);
    }

    private boolean isInSEDraggableZone(MouseEvent event) {
        return event.getY() > (getHeight() - RESIZE_MARGIN) && event.getX() > (getWidth() - RESIZE_MARGIN);
    }

    private boolean isInSDraggableZone(MouseEvent event) {
        return event.getY() > (getHeight() - RESIZE_MARGIN);
    }

    private boolean isInSWDraggableZone(MouseEvent event) {
        return event.getY() > (getHeight() - RESIZE_MARGIN) && event.getX() < RESIZE_MARGIN;
    }

    private boolean isInWDraggableZone(MouseEvent event) {
        return event.getX() < RESIZE_MARGIN;
    }

    private void minimize() {
        Preconditions.checkState(content.isPresent());
        Preconditions.checkState(vbox.getChildren().size() == 2);
        double width = vbox.getWidth();
        vbox.getChildren().remove(1);
        vbox.setPrefWidth(width);
        setPrefHeight(getHeight() - content.get().getLayoutBounds().getHeight() - vbox.getSpacing());
        if (isSticked()) {
            updateSticking();
        }
    }

    private void updateSticking() {
        switch (stickMode) {
            case LEFT:
                setRotate(isMinimized() ? 90. : 0.);
                setLayoutX(isMinimized() ? -getWidth() / 2 + getHeight() / 2 : 0);
                if (isMinimized() && getLayoutY() < getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getWidth() / 2 - getHeight() / 2);
                } else if (isMinimized() && getLayoutY() > getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2);
                }
                break;
            case RIGHT:
                setRotate(isMinimized() ? -90. : 0);
                setLayoutX(getParent().getLayoutBounds().getWidth() - (isMinimized() ? getWidth() / 2 + getHeight() / 2 : getWidth()));
                if (isMinimized() && getLayoutY() < getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getWidth() / 2 - getHeight() / 2);
                } else if (isMinimized() && getLayoutY() > getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2);
                }
                break;
            case TOP:
                setRotate(0.);
                setLayoutY(0.);
                break;
            case BOTTOM:
                setRotate(0.);
                setLayoutY(getParent().getLayoutBounds().getHeight() - getHeight());
                break;
            case LEFT_TOP:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(0.);
                break;
            case LEFT_BOTTOM:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(getParent().getLayoutBounds().getHeight() - getHeight());
                break;
            case RIGHT_TOP:
                setRotate(0.);
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setLayoutY(0.);
                break;
            case RIGHT_BOTTOM:
                setRotate(0.);
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setLayoutY(getParent().getLayoutBounds().getHeight() - getHeight());
                break;
            default:
                setRotate(0.);
        }
    }

    private boolean isSticked() {
        return stickMode != StickMode.NONE;
    }

    private boolean isMaximized() {
        return content != null && vbox.getChildren().size() == 2;
    }

    private void maximize() {
        Preconditions.checkState(content.isPresent());
        Preconditions.checkState(vbox.getChildren().size() < 2);
        vbox.getChildren().add(content.get());
        setPrefHeight(getHeight() + content.get().getLayoutBounds().getHeight() + vbox.getSpacing());
        if (isSticked()) {
            updateSticking();
        }
    }

    private boolean isMinimized() {
        return content.isPresent() && vbox.getChildren().size() < 2;
    }

    @NotNull
    public Optional<Node> getContent() {
        ObservableList<Node> children = vbox.getChildren();
        return children.size() < 2 ? Optional.empty() : Optional.of(children.get(1));
    }

    public void setContent(@NotNull Node content) {
        this.content = Optional.of(content);
        ObservableList<Node> children = vbox.getChildren();
        if (children.size() < 2) {
            children.add(content);
        } else {
            children.set(1, content);
        }
    }

    @NotNull
    public Button getCloseButton() {
        return closeButton;
    }

    private void setStickMode(@NotNull StickMode stickMode) {
        this.stickMode = stickMode;
    }

    @NotNull
    public StickMode getSticked() {
        return stickMode;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    private class Delta {
        double x;
        double y;
    }

}
