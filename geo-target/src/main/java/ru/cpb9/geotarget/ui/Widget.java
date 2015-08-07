package ru.cpb9.geotarget.ui;

import com.google.common.base.Preconditions;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class Widget extends Region
{
    public static final double OPACITY = 0.7;
    private static final Logger LOG = LoggerFactory.getLogger(Widget.class);
    private static final double STICKING_WIDTH = 20.;
    private final String title;
    private final Label titleLabel;
    private final VBox vbox;
    private final AnchorPane headerBox;
    private final Button closeButton;
    private final Button minMaxButton;
    @Nullable
    private Node content;
    @NotNull
    private StickMode stickMode = StickMode.NONE;

    public Widget(@NotNull String title, @NotNull Node content)
    {
        this(title);
        setContent(content);
    }

    public Widget(@NotNull String title)
    {
        this.title = title;
        titleLabel = new Label(title);
        closeButton = new Button("x");
        minMaxButton = new Button("o");
        minMaxButton.setOnAction((e) -> {
            if (isMinimized()) { maximize(); } else if (isMaximized()) { minimize(); }
        });
        HBox buttonsBox = new HBox(minMaxButton, closeButton);
        headerBox = new AnchorPane(titleLabel, buttonsBox);
        AnchorPane.setLeftAnchor(titleLabel, 5.);
        AnchorPane.setRightAnchor(buttonsBox, 5.);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        headerBox.setMaxWidth(Double.MAX_VALUE);
        vbox = new VBox(headerBox);
        getChildren().add(vbox);
        setStyle("-fx-background-color: #ccc;-fx-border-width: 1;-fx-border-color: #444;-fx-border-style: solid");
        setOpacity(OPACITY);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(5);
        vbox.setFillWidth(true);
        final Delta dragDelta = new Delta();
        titleLabel.setOnMousePressed((e) ->
        {
            dragDelta.x = getLayoutX() - e.getScreenX();
            dragDelta.y = getLayoutY() - e.getScreenY();
            setOpacity(0.9);
            toFront();

        });
        titleLabel.setOnMouseDragged((e) ->
        {
            setLayoutX(e.getScreenX() + dragDelta.x);
            setLayoutY(e.getScreenY() + dragDelta.y);
            if (getLayoutX() < STICKING_WIDTH)
            {
                setStickMode(StickMode.LEFT);
            }
            else if (getParent().getLayoutBounds().getWidth() - getLayoutX() - getWidth() < STICKING_WIDTH)
            {
                setStickMode(StickMode.RIGHT);
            }
            else if (getLayoutY() < STICKING_WIDTH)
            {
                setStickMode(StickMode.TOP);
            }
            else if (getParent().getLayoutBounds().getHeight() - getLayoutY() - getHeight() < STICKING_WIDTH)
            {
                setStickMode(StickMode.BOTTOM);
            }
            else
            {
                setStickMode(StickMode.NONE);
            }
            updateSticking();
        });
        titleLabel.setOnMouseReleased((e) -> setOpacity(OPACITY));
    }

    private void minimize()
    {
        Preconditions.checkNotNull(content);
        Preconditions.checkState(vbox.getChildren().size() == 2);
        double width = vbox.getWidth();
        vbox.getChildren().remove(1);
        vbox.setPrefWidth(width);
        if (isSticked())
        {
            setHeight(getHeight() - content.getLayoutBounds().getHeight() - vbox.getSpacing());
            updateSticking();
        }
    }

    private void updateSticking()
    {
        switch (stickMode)
        {
            case LEFT:
                setLayoutX(0);
                setRotate(isMinimized()? 90. : 0.);
                break;
            case RIGHT:
                setLayoutX(getParent().getLayoutBounds().getWidth() - getWidth());
                setRotate(isMinimized()? -90. : 0);
                break;
            case TOP:
                setLayoutY(0.);
                setRotate(0.);
                break;
            case BOTTOM:
                setLayoutY(getParent().getLayoutBounds().getHeight() - getHeight());
                setRotate(0.);
                break;
            default:
                setRotate(0.);
        }
    }

    private boolean isSticked()
    {
        return stickMode != StickMode.NONE;
    }

    private boolean isMaximized()
    {
        return content != null && vbox.getChildren().size() == 2;
    }

    private void maximize()
    {
        Preconditions.checkNotNull(content);
        Preconditions.checkState(vbox.getChildren().size() < 2);
        vbox.getChildren().add(content);
        if (isSticked())
        {
            setHeight(getHeight() + content.getLayoutBounds().getHeight() + vbox.getSpacing());
            updateSticking();
        }
    }

    private boolean isMinimized()
    {
        return content != null && vbox.getChildren().size() < 2;
    }

    @NotNull
    public Optional<Node> getContent()
    {
        ObservableList<Node> children = vbox.getChildren();
        return children.size() < 2 ? Optional.empty() : Optional.of(children.get(1));
    }

    public void setContent(@NotNull Node content)
    {
        this.content = content;
        ObservableList<Node> children = vbox.getChildren();
        if (children.size() < 2)
        {
            children.add(content);
        }
        else
        {
            children.set(1, content);
        }
    }

    @NotNull
    public Button getCloseButton()
    {
        return closeButton;
    }

    private void setStickMode(@NotNull StickMode stickMode)
    {
        this.stickMode = stickMode;
    }

    @NotNull
    public StickMode getSticked()
    {
        return stickMode;
    }

    @NotNull
    public String getTitle()
    {
        return title;
    }

    private class Delta
    {
        double x;
        double y;
    }
}
