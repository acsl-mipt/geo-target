package ru.cpb9.geotarget.ui;

import com.google.common.base.Preconditions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private boolean dragging;
    private boolean initMinHeight;
    private boolean initMinWidth;
    private ZonesEnum zone;
    WidgetUtils widgetUtils;
    TabPane tabPane = new TabPane();
    @NotNull
    private Optional<Node> content = Optional.empty();
    @NotNull
    private StickMode stickMode = StickMode.NONE;

    public Widget(@NotNull String title, @NotNull Node content) {
        this(title);
        setContent(content);
        setAccessibleText(title);
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
            // TODO Обратить внимание, что первый виджет - ЭддДевайсВиджет
            if (!ZonesEnum.findZone(e, RESIZE_MARGIN, this).isPresent()) {
                setCursor(Cursor.DEFAULT);
            } else {
                setCursor(ZonesEnum.findZone(e, RESIZE_MARGIN, this).get().setCursor());
            }
        });
        setOnMousePressed(e ->
        {
            ObservableList<Node> childrenUnmodifiable = this.getParent().getChildrenUnmodifiable();
            for (int n = 1; n < childrenUnmodifiable.size(); n++) {
                Node node = childrenUnmodifiable.get(n);
                if (this.getLayoutX() > node.getLayoutX()
                        && this.getLayoutX() < (node.getLayoutX() + node.getBoundsInLocal().getWidth())
                        && this.getLayoutY() > node.getLayoutY()
                        && this.getLayoutY() < (node.getLayoutY() + node.getBoundsInLocal().getHeight())
                        && node.isVisible()) {
                    Tab tab1 = new Tab(this.getAccessibleText());
                    tab1.setContent(this.content.get());
                    Tab tab2 = new Tab(this.getParent().getChildrenUnmodifiable().get(n).getAccessibleText());
                    tab2.setContent(this.getParent().getChildrenUnmodifiable().get(n));
                    tabPane.getTabs().addAll(tab1, tab2);
                    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                    tabPane.setMinSize(0, 0);
                    tabPane.setMaxSize(600, 600);
                    vbox.getChildren().add(tabPane);
                    // TODO Правильно как?
                    vbox.getChildren().remove(0);
                    vbox.getChildren().remove(0);
                }
            }

            if (!ZonesEnum.findZone(e, RESIZE_MARGIN, this).isPresent()) {
                return;
            }

            if (isMinimized()) {
                return;
            }

            dragging = true;
            zone = ZonesEnum.findZone(e, RESIZE_MARGIN, this).get();

            // make sure that the minimum height is set to the current height once,
            // setting a min height that is smaller than the current height will
            // have no effect
            if (!initMinHeight) {
                setMinHeight(getHeight());
                WidgetUtils.minHeight = getHeight();
                initMinHeight = true;
            }

            if (!initMinWidth) {
                setMinWidth(getWidth());
                WidgetUtils.minWidth = getWidth();
                initMinWidth = true;
            }

            setMinSize(WidgetUtils.minWidth, WidgetUtils.minHeight);
            setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());
            vbox.setMinSize(WidgetUtils.minWidth, WidgetUtils.minHeight);
            vbox.setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());
            widgetUtils = new WidgetUtils(e.getX(), e.getY(), e.getScreenX(), e.getScreenY(), getWidth(), getHeight(),
                    WidgetUtils.minWidth, WidgetUtils.minHeight, getLayoutX(), getLayoutY());
        });
        setOnMouseReleased(e ->
        {
            WidgetUtils.prefWidth.put(title, getWidth());
            WidgetUtils.prefHeight.put(title, getHeight());
            dragging = false;
            zone = null;
            setCursor(Cursor.DEFAULT);
        });
        setOnMouseDragged(e ->
        {
            if (!dragging) {
                return;
            }

            if (zone != null) {
                zone.action(e, RESIZE_MARGIN, widgetUtils, this, vbox);
            }
        });

        vbox.setOnMousePressed(e ->
        {
            if (ZonesEnum.findZone(e, RESIZE_MARGIN, this).isPresent()) {
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

        tabPane.setOnMousePressed(e -> tabPane.getTabs().stream().filter(Tab::isSelected).forEach(tab -> {

            tab.getContent().prefWidth(WidgetUtils.prefWidth.get(tab.getText()));
            tab.getContent().prefHeight(WidgetUtils.prefHeight.get(tab.getText()));

            tabPane.setPrefSize(WidgetUtils.prefWidth.get(tab.getText()) + 20, WidgetUtils.prefHeight.get(tab.getText()) + 20);
            setPrefSize(WidgetUtils.prefWidth.get(tab.getText()) + 40, WidgetUtils.prefHeight.get(tab.getText()) + 40);

        }));
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
                setLayoutX(isMinimized() ? -getWidth() / 2 + getPrefHeight() / 2 : 0);
                if (isMinimized() && getLayoutY() < getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getWidth() / 2 - getHeight() / 2);
                } else if (isMinimized() && getLayoutY() > getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2) {
                    setLayoutY(getParent().getLayoutBounds().getHeight() - getWidth() / 2 - getHeight() / 2);
                }
                break;
            case RIGHT:
                setRotate(isMinimized() ? -90. : 0);
                setLayoutX(getParent().getLayoutBounds().getWidth() - (isMinimized() ? getWidth() / 2 + getPrefHeight() / 2 : getWidth()));
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
                setLayoutY(getParent().getLayoutBounds().getHeight() - getPrefHeight());
                break;
            case LEFT_TOP:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(0.);
                break;
            case LEFT_BOTTOM:
                setRotate(0.);
                setLayoutX(0.);
                setLayoutY(getParent().getLayoutBounds().getHeight() - getPrefHeight());
                break;
            case RIGHT_TOP:
                setRotate(0.);
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setLayoutY(0.);
                break;
            case RIGHT_BOTTOM:
                setRotate(0.);
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setLayoutY(getParent().getLayoutBounds().getHeight() - getPrefHeight());
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
