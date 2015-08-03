package ru.cpb9.geotarget.ui;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * @author Artem Shein
 */
public class PaneWithTitle extends VBox
{
    private final String title;

    public PaneWithTitle(String title, Region... children)
    {
        this.title = title;
        Text text = new Text(title);
        text.getStyleClass().add("title");
        HBox titlePane = new HBox(text);
        titlePane.getStyleClass().add("title");
        titlePane.setPadding(new Insets(5, 10, 5, 10));
        getChildren().add(titlePane);
        getChildren().addAll(children);
    }

}
