package ru.cpb9.geotarget.ui.controls.parameters.linebuilder;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.DeviceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * @author Alexander Kuchuk.
 */


public class LineBuilder extends VBox
{
    @NotNull
    private final DeviceController deviceController;
    private final Polyline polyline = new Polyline();
    private ObservableList<Position> positions = FXCollections.observableArrayList();

    private boolean armed = false;

    private boolean active = false;

    public LineBuilder(@NotNull DeviceController deviceController)
    {
        this.deviceController = deviceController;
        LinePanel linePanel = new LinePanel(this);

        polyline.setLineWidth(5);
        polyline.setColor(Color.RED);
        polyline.setFollowTerrain(true);

        RenderableLayer layer = new RenderableLayer();
        deviceController.worldWind().getPanel().getModel().getLayers().add(layer);
        layer.addRenderable(polyline);

        getChildren().add(linePanel);
        setVgrow(linePanel, Priority.ALWAYS);

        deviceController.worldWind().getPanel().getInputHandler().addMouseListener(new MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
                if (armed && mouseEvent.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    if ((mouseEvent.getModifiersEx() & java.awt.event.MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        if (!mouseEvent.isControlDown()) {
                            active = true;
                            addPosition();
                        }
                    }
                    mouseEvent.consume();
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (armed && (e.getButton() == java.awt.event.MouseEvent.BUTTON1)) {
                    if (e.isControlDown())
                    {
                        removePosition();
                    }
                }
                e.consume();
            }
        });
    }

    private void removePosition()
    {
        if (positions.size() == 0)
            return;

        positions.remove(positions.size() - 1);
        polyline.setPositions(positions);

        SwingUtilities.invokeLater(() -> deviceController.worldWind().getPanel().redraw());
    }

    private void addPosition()
    {
        Position pos = deviceController.worldWind().getPanel().getCurrentPosition();

        if (pos == null)
            return;

        positions.add(pos);
        polyline.setPositions(positions);

        SwingUtilities.invokeLater(()->deviceController.worldWind().getPanel().redraw());
    }

    private void clear()
    {
        while (positions.size() > 0)
        {
            removePosition();
        }
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }

    public boolean isArmed() {
        return armed;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    private final class LinePanel extends VBox
    {
        private Button startButton;
        private Button endButton;
        private Button pauseButton;
        private ObservableList<String> panelLabels = FXCollections.observableArrayList();
        @NotNull private final LineBuilder lineBuilder;

        private LinePanel(@NotNull LineBuilder lineBuilder)
        {
            this.lineBuilder = lineBuilder;
            makePanel();
        }

        private void makePanel()
        {
            HBox buttonBox = new HBox();
            Image imageOk = new Image(getClass().getClassLoader().getResourceAsStream("Start.png"));
            Image imageStop = new Image(getClass().getClassLoader().getResourceAsStream("Stop.png"));
            Image imagePause = new Image(getClass().getClassLoader().getResourceAsStream("Pause.png"));
            startButton = new Button("Start", new ImageView(imageOk));
            pauseButton = new Button("Pause", new ImageView(imagePause));
            endButton = new Button("End", new ImageView(imageStop));

            startButton.setDisable(false);
            endButton.setDisable(true);
            pauseButton.setDisable(true);

            buttonBox.getChildren().addAll(startButton, pauseButton, endButton);
            buttonBox.setPadding(new Insets(10, 5, 10, 5));
            buttonBox.setSpacing(5);

            startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                lineBuilder.setArmed(true);
                lineBuilder.clear();
                endButton.setDisable(false);
                pauseButton.setDisable(false);
                startButton.setDisable(true);
            });

            pauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                lineBuilder.setArmed(!lineBuilder.isArmed());
                endButton.setDisable(false);
                startButton.setDisable(true);

                if (!lineBuilder.isArmed())
                {
                    pauseButton.setText("Resume");
                    pauseButton.setGraphic(new ImageView(imageOk));
                }
                else
                {
                    pauseButton.setText("Pause");
                    pauseButton.setGraphic(new ImageView(imagePause));
                }

                pauseButton.setDisable(false);
            });

            endButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                lineBuilder.setArmed(false);
                endButton.setDisable(true);
                startButton.setDisable(false);
                pauseButton.setDisable(true);
                pauseButton.setText("Pause");
                pauseButton.setGraphic(new ImageView(imagePause));
            });

            ListView<String> posList = new ListView<>();
            posList.setItems(panelLabels);

            positions.addListener((ListChangeListener<Position>) c ->
            {
                while (c.next())
                {
                    if (c.wasAdded())
                    {
                            Position pos = positions.get(positions.size() - 1);
                            Platform.runLater(() -> panelLabels.add(String.format("Latitude %7.4f\u00B0", pos.getLatitude().getDegrees()) + "  ,  " +
                                    String.format("Longitude %7.4f\u00B0", pos.getLongitude().getDegrees())));
                    }
                    else if (c.wasRemoved())
                    {
                        if (positions.size() > 0)
                            Platform.runLater(()->panelLabels.remove(panelLabels.size() - 1));
                        else
                            Platform.runLater(panelLabels::clear);
                    }

                }
            });
            setPadding(new Insets(10, 5, 10, 5));
            getChildren().addAll(buttonBox, posList);
            setVgrow(posList, Priority.ALWAYS);
        }
    }
}
