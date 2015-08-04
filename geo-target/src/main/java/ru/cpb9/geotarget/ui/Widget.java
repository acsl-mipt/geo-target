package ru.cpb9.geotarget.ui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class Widget extends Region
{
    private final String title;
    private final Label titleLabel;
    private final VBox vbox;

    public Widget(@NotNull String title)
    {
        this.title = title;
        titleLabel = new Label(title);
        vbox = new VBox(titleLabel);
        getChildren().add(vbox);
        setStyle("-fx-background-color: #ccc");
        setOpacity(0.7);
        setPrefHeight(0);
        setPrefWidth(0);
    }

    public void setContent(@NotNull Node content)
    {
        ObservableList<Node> children = getChildren();
        if (children.size() < 2)
        {
            children.add(content);
        }
        else
        {
            children.set(1, content);
        }
    }
}
