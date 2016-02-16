package ru.cpb9.geotarget.ui;

import com.google.common.base.Preconditions;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    public static final double OPACITY = 0.7;
    private final String title;
    private final Label titleLabel;
    private final VBox vbox;
    private final AnchorPane headerBox;
    private final Button closeButton;
    private final Button minMaxButton;
    private final Button scrollBarButton;
    private double y;
    private boolean dragging;
    private boolean initMinHeight;
    @NotNull
    private Optional<Node> content = Optional.empty();
    @NotNull
    private StickMode stickMode = StickMode.NONE;
    private ScrollBar scrollOpacity = new ScrollBar();

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
                minimize();
            }
        });
        scrollOpacity.setMin(0.1);
        scrollOpacity.setMax(1.);
        scrollOpacity.setValue(OPACITY);
        scrollOpacity.setOrientation(Orientation.VERTICAL);
        scrollOpacity.setVisible(false);
        scrollOpacity.setTranslateY(-scrollOpacity.getHeight());
        scrollOpacity.valueProperty().addListener(event -> {
            setOpacity(scrollOpacity.getValue());
        });
        scrollBarButton = new Button("-");
        scrollBarButton.setOnMouseClicked(event -> {
            if (scrollOpacity.isVisible()) {
                scrollOpacity.setVisible(false);
            } else {
                scrollOpacity.setTranslateX(event.getSceneX() - getLayoutX() - 5.);
                scrollOpacity.setVisible(true);
            }
        });
        HBox buttonsBox = new HBox(scrollBarButton, minMaxButton, closeButton);
        headerBox = new AnchorPane(titleLabel, buttonsBox);
        AnchorPane.setLeftAnchor(titleLabel, 5.);
        AnchorPane.setRightAnchor(buttonsBox, 5.);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        headerBox.setMaxWidth(Double.MAX_VALUE);
        vbox = new VBox(headerBox);
        getChildren().add(scrollOpacity);
        getChildren().add(vbox);
        setStyle("-fx-background-color: #ccc;-fx-border-width: 1;-fx-border-color: #444;-fx-border-style: solid");
        setOpacity(OPACITY);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(5);
        vbox.setFillWidth(true);
        final Delta dragDelta = new Delta();
        setOnMouseMoved(e ->
        {
            if (isInDraggableZone(e) || dragging) {
                setCursor(Cursor.S_RESIZE);
            } else {
                setCursor(Cursor.DEFAULT);
            }
        });
        setOnMousePressed(e ->
        {
            if (!isInDraggableZone(e)) {
                return;
            }
            dragging = true;

            // make sure that the minimum height is set to the current height once,
            // setting a min height that is smaller than the current height will
            // have no effect
            if (!initMinHeight) {
                setMinHeight(getHeight());
                initMinHeight = true;
            }
            y = e.getY();
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
            double mousey = e.getY();
            double newHeight = getMinHeight() + (mousey - y);
            setMinHeight(newHeight);
            y = mousey;
        });
        vbox.setOnMousePressed(e ->
        {
            dragDelta.x = getLayoutX() - e.getScreenX();
            dragDelta.y = getLayoutY() - e.getScreenY();
            setOpacity(0.9);
            toFront();
        });
        vbox.setOnMouseDragged(e ->
        {
            setLayoutX(e.getScreenX() + dragDelta.x);
            setLayoutY(e.getScreenY() + dragDelta.y);

            if ((getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH) &&
                    (getLayoutY() < STICKING_WIDTH)) {
                setStickMode(StickMode.RIGHTTOPCORNER);
            } else if ((getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH) &&
                    (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH)) {
                setStickMode(StickMode.RIGHTBOTTOMCORNER);
            } else if ((getLayoutX() < STICKING_WIDTH) &&
                    (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH)) {
                setStickMode(StickMode.LEFTBOTTOMCORNER);
            } else if ((getLayoutX() < STICKING_WIDTH) && (getLayoutY() < STICKING_WIDTH)) {
                setStickMode(StickMode.LEFTTOPCORNER);
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
        vbox.setOnMouseReleased(e -> setOpacity(OPACITY));
    }

    private boolean isInDraggableZone(MouseEvent event) {
        return event.getY() > (getHeight() - RESIZE_MARGIN);
    }

    private void minimize() {
        Preconditions.checkState(content.isPresent());
        Preconditions.checkState(vbox.getChildren().size() == 2);
        //Preconditions.checkState(getChildren().size() == 2);
        double width = vbox.getWidth();
        //System.out.println(content);
        //getChildren().remove(0);
        vbox.getChildren().remove(1);
        vbox.setPrefWidth(width);
        if (isSticked()) {
            setHeight(getHeight() - content.get().getLayoutBounds().getHeight() - vbox.getSpacing());
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
            case LEFTTOPCORNER:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(0.);
                break;
            case LEFTBOTTOMCORNER:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(getParent().getLayoutBounds().getHeight() - getHeight());
                break;
            case RIGHTTOPCORNER:
                setRotate(0.);
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setLayoutY(0.);
                break;
            case RIGHTBOTTOMCORNER:
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
        //Preconditions.checkState(getChildren().size() < 2);
        vbox.getChildren().add(content.get());
        //getChildren().add(content.get());
        if (isSticked()) {
            setHeight(getHeight() + content.get().getLayoutBounds().getHeight() + vbox.getSpacing());
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
