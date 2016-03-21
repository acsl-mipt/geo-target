package ru.cpb9.geotarget.ui;

import com.google.common.base.Preconditions;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class Widget extends Region {
    private static final double STICKING_WIDTH = 20.;
    private static final int RESIZE_MARGIN = 5;
    private static double opacity = 0.7;

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

    private ResizeZone zone;

    private WidgetCoordinates widgetCoordinates = new WidgetCoordinates();

    private HashMap<String, Double> prefWidth = new HashMap<>();
    private HashMap<String, Double> prefHeight = new HashMap<>();

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
            if (!ResizeZone.findZone(e, this).isPresent() || isMinimized()) {
                setCursor(Cursor.DEFAULT);
            } else {
                setCursor(ResizeZone.findZone(e, this).get().setCursor());
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
                    vbox.getChildren().add(tabPane);
                    // TODO Переделать
                    vbox.getChildren().remove(0, 2);
                }
            }

            if (!ResizeZone.findZone(e, this).isPresent()) {
                return;
            }

            if (isMinimized()) {
                return;
            }

            dragging = true;
            zone = ResizeZone.findZone(e, this).get();

            // make sure that the minimum height is set to the current height once,
            // setting a min height that is smaller than the current height will
            // have no effect
            if (!initMinHeight) {
                setMinHeight(getHeight());
                widgetCoordinates.setMinHeight(getHeight());
                initMinHeight = true;
            }

            if (!initMinWidth) {
                setMinWidth(getWidth());
                widgetCoordinates.setMinWidth(getWidth());
                initMinWidth = true;
            }

            setMinSize(widgetCoordinates.minWidth, widgetCoordinates.minHeight);
            setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());

            vbox.setMinSize(widgetCoordinates.minWidth, widgetCoordinates.minHeight);
            vbox.setMaxSize(getParent().getLayoutBounds().getWidth(), getParent().getLayoutBounds().getHeight());

            widgetCoordinates.setX(e.getX());
            widgetCoordinates.setY(e.getY());
            widgetCoordinates.setScreenX(e.getScreenX());
            widgetCoordinates.setScreenY(e.getScreenY());
            widgetCoordinates.setStartWidth(getWidth());
            widgetCoordinates.setStartHeight(getHeight());
            widgetCoordinates.setStartLayoutX(getLayoutX());
            widgetCoordinates.setStartLayoutY(getLayoutY());
        });
        setOnMouseReleased(e ->
        {
            prefWidth.put(title, getWidth());
            prefHeight.put(title, getHeight());
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
                zone.action(e, this);
            }
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
            if (dragging) {
                return;
            }

            setLayoutX(e.getScreenX() + dragDelta.x);
            setLayoutY(e.getScreenY() + dragDelta.y);

            StickMode stickMode = StickMode.findMode(this);
            setStickMode(stickMode);
            stickMode.update(this);
        });
        vbox.setOnMouseReleased(e -> setOpacity(opacity));

        tabPane.setOnMousePressed(e -> tabPane.getTabs().stream().filter(Tab::isSelected).forEach(tab -> {

            tab.getContent().prefWidth(prefWidth.get(tab.getText()));
            tab.getContent().prefHeight(prefHeight.get(tab.getText()));
            tabPane.setPrefSize(prefWidth.get(tab.getText()), prefHeight.get(tab.getText()));

            if (ResizeZone.findZone(e, this).isPresent()) {
                System.out.println(ResizeZone.findZone(e, this));
                return;
            }

            dragDelta.x = getLayoutX() - e.getScreenX();
            dragDelta.y = getLayoutY() - e.getScreenY();
            setOpacity(0.9);
            toFront();
        }));
        tabPane.setOnMouseDragged(e -> {
            if (dragging) {
                return;
            }

            setLayoutX(e.getScreenX() + dragDelta.x);
            setLayoutY(e.getScreenY() + dragDelta.y);
        });
    }

    private void minimize() {
        Preconditions.checkState(content.isPresent());
        Preconditions.checkState(vbox.getChildren().size() == 2);
        double width = vbox.getWidth();
        vbox.getChildren().remove(1);
        vbox.setPrefWidth(width);
        setPrefHeight(getHeight() - content.get().getLayoutBounds().getHeight() - vbox.getSpacing());
        if (isSticked()) {
            StickMode.findMode(this).update(this);
        }
    }

    private boolean isSticked() {
        return stickMode != StickMode.NONE;
    }

    protected boolean isMaximized() {
        // TODO @metadeus Что тут значит content != null ?
        return content != null && vbox.getChildren().size() == 2;
    }

    private void maximize() {
        Preconditions.checkState(content.isPresent());
        Preconditions.checkState(vbox.getChildren().size() < 2);
        vbox.getChildren().add(content.get());
        setPrefHeight(getHeight() + content.get().getLayoutBounds().getHeight() + vbox.getSpacing());
        if (isSticked()) {
            StickMode.findMode(this).update(this);
        }
    }

    protected boolean isMinimized() {
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
        VBox.setVgrow(getVbox().getChildren().get(1), Priority.ALWAYS);
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

    public WidgetCoordinates getWidgetCoordinates() {
        return widgetCoordinates;
    }

    public VBox getVbox() {
        return vbox;
    }

    public static int getResizeMargin() {
        return RESIZE_MARGIN;
    }

    public static double getStickingWidth() {
        return STICKING_WIDTH;
    }
}
