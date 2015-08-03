package ru.cpb9.geotarget.ui;

import c10n.C10N;
import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.terrain.CompoundElevationModel;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class LayersList extends VBox
{
    private static final Messages I = C10N.get(Messages.class);
    private static final Logger LOG = LoggerFactory.getLogger(LayersList.class);
    public static final DataFormat DATA_FORMAT = new DataFormat("layerIndex");
    private final ListView<LayerWithEnabledFlag> listView;
    @NotNull
    private ObservableList<LayerWithEnabledFlag> layersList = FXCollections.observableArrayList();

    public LayersList(WorldWindNode worldWindNode)
    {
        final LayerList layers = worldWindNode.getPanel().getModel().getLayers();
        updateLayers(layers);
        layers.addPropertyChangeListener(e -> updateLayers(layers));
        listView = new ListView<>(layersList);
        listView.setCellFactory(value -> new LayerCell());
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        final Button upButton = new Button(I.up());
        upButton.setOnAction(e -> {
            final FocusModel<LayerWithEnabledFlag> focusModel = listView.getFocusModel();
            int focusedIndex = focusModel.getFocusedIndex();
            if (focusedIndex > 0)
            {
                LayerWithEnabledFlag layer = layersList.get(focusedIndex);
                layers.set(focusedIndex, layers.get(focusedIndex - 1));
                layersList.set(focusedIndex - 1, layer);
                listView.getSelectionModel().selectPrevious();
            }
        });
        final Button downButton = new Button(I.down());
        downButton.setOnAction(e -> {
            final FocusModel<LayerWithEnabledFlag> focusModel = listView.getFocusModel();
            int focusedIndex = focusModel.getFocusedIndex();
            if (focusedIndex < layersList.size() - 1)
            {
                LayerWithEnabledFlag layer = layersList.get(focusedIndex);
                layers.set(focusedIndex, layers.get(focusedIndex + 1));
                layersList.set(focusedIndex + 1, layer);
                listView.getSelectionModel().selectNext();
            }
        });
        final Button clearAllButton = new Button(I.clearAll());
        clearAllButton.setOnAction(e -> layersList.forEach(layer -> layer.setEnabled(false)));
        listView.getSelectionModel().selectedIndexProperty().addListener((index, oldValue, newValue) -> {
            upButton.setDisable(index.getValue().equals(0));
            downButton.setDisable(index.getValue().equals(layersList.size() - 1));
        });
        hbox.getChildren().addAll(upButton, downButton, clearAllButton);

        /**
         * Switch to flat globe panel
         */
        final PaneWithTitle pSwitcher = new PaneWithTitle(I.switcher());
        final HBox hBoxFlat = new HBox();
        hBoxFlat.setPadding(new Insets(10));
        final CheckBox flatGlobe = new CheckBox(I.switchToFlat());
        hBoxFlat.getChildren().add(flatGlobe);
        pSwitcher.getChildren().add(hBoxFlat);
        getChildren().add(pSwitcher);

        /**
         * Switch to flat globe
         */
        flatGlobe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (flatGlobe.isSelected())
            {
                worldWindNode.getPanel().getModel().setGlobe(new EarthFlat());
                BasicOrbitView orbitView = (BasicOrbitView)worldWindNode.getPanel().getView();
                FlatOrbitView flatOrbitView = new FlatOrbitView();
                flatOrbitView.setCenterPosition(orbitView.getCenterPosition());
                flatOrbitView.setZoom(orbitView.getZoom( ));
                flatOrbitView.setHeading(orbitView.getHeading());
                flatOrbitView.setPitch(orbitView.getPitch());
                worldWindNode.getPanel().setView(flatOrbitView);

                LayerList layersL = worldWindNode.getPanel().getModel().getLayers();
                for(int i = 0; i < layersL.size(); i++)
                {
                    if(layersL.get(i) instanceof SkyGradientLayer)
                    {
                        layersL.set(i, new SkyColorLayer());
                        layersL.get(i).setName(I.skyGr());
                    }
                }
                worldWindNode.getPanel().redraw();
            }
            else
            {
                worldWindNode.getPanel().getModel().setGlobe(new Earth());
                FlatOrbitView flatOrbitView = (FlatOrbitView)worldWindNode.getPanel().getView();
                BasicOrbitView orbitView = new BasicOrbitView();
                orbitView.setCenterPosition(flatOrbitView.getCenterPosition());
                orbitView.setZoom(flatOrbitView.getZoom( ));
                orbitView.setHeading(flatOrbitView.getHeading());
                orbitView.setPitch(flatOrbitView.getPitch());
                worldWindNode.getPanel().setView(orbitView);

                LayerList layersL = worldWindNode.getPanel().getModel().getLayers();
                for(int i = 0; i < layersL.size(); i++)
                {
                    if(layersL.get(i) instanceof SkyColorLayer)
                    {
                        layersL.set(i, new SkyGradientLayer());
                        layersL.get(i).setName(I.skyGradient());
                    }
                }
                worldWindNode.getPanel().redraw();
            }
        });


        VBox.setVgrow(listView, Priority.ALWAYS);
        ElevationModel elevationModel = worldWindNode.getPanel().getModel().getGlobe().getElevationModel();
        if (elevationModel != null)
        {
            if (elevationModel instanceof CompoundElevationModel)
            {
                HBox modelsBox = new HBox();
                modelsBox.setPadding(new Insets(10));
                PaneWithTitle elevationModelsBox = new PaneWithTitle(I.elevationModels(), modelsBox);
                CompoundElevationModel compoundElevationModel = (CompoundElevationModel)elevationModel;
                for (ElevationModel subModel : compoundElevationModel.getElevationModels())
                {
                    CheckBox checkbox = new CheckBox(subModel.getName());
                    checkbox.setSelected(subModel.isEnabled());
                    checkbox.setOnAction(e -> subModel.setEnabled(checkbox.isSelected()));
                    modelsBox.getChildren().add(checkbox);
                }
                getChildren().add(elevationModelsBox);
            }
            else
            {
                CheckBox elevationCheckBox = new CheckBox(elevationModel.getName());
                elevationCheckBox.setPadding(new Insets(10));
                elevationCheckBox.setSelected(elevationModel.isEnabled());
                elevationCheckBox.setOnAction(e -> elevationModel.setEnabled(elevationCheckBox.isSelected()));
                getChildren().add(elevationCheckBox);
            }
        }
        PaneWithTitle layersPane = new PaneWithTitle(I.layers(), hbox, listView);
        VBox.setVgrow(layersPane, Priority.ALWAYS);
        getChildren().addAll(layersPane);
    }

    private void updateLayers(@NotNull LayerList layers)
    {
        Platform.runLater(() -> layersList.setAll(layers.stream().map(LayerWithEnabledFlag::new).collect(
                Collectors.toList())));
    }

    private static class LayerCell extends ListCell<LayerWithEnabledFlag>
    {
        @Override
        public void updateItem(@Nullable LayerWithEnabledFlag layer, boolean empty)
        {
            super.updateItem(layer, empty);
            if (!empty && layer != null)
            {
                CheckBox checkbox = new CheckBox();
                checkbox.setText(layer.getLayer().getName());
                checkbox.setSelected(layer.isEnabled());
                checkbox.setOnAction(e -> layer.setEnabled(checkbox.isSelected()));
                layer.enabledProperty().addListener((enabled, oldValue, newValue) -> checkbox.setSelected(newValue));
                setGraphic(checkbox);
                checkbox.setOnDragDetected(this::onDragDetectedEventHandler);
            }
            setOnDragDetected(this::onDragDetectedEventHandler);
            setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE));
            setOnDragDropped(e -> {
                int oldIndex = (int) e.getDragboard().getContent(DATA_FORMAT);
                int newIndex = getIndex();
                ObservableList<LayerWithEnabledFlag> list = getListView().getItems();
                LayerWithEnabledFlag oldIndexLayer = list.get(oldIndex);
                list.set(oldIndex, list.get(newIndex));
                list.set(newIndex, oldIndexLayer);
                getListView().getSelectionModel().selectIndices(newIndex);
            });
        }

        private void onDragDetectedEventHandler(MouseEvent e)
        {
            Dragboard db = getListView().startDragAndDrop(TransferMode.MOVE);
            db.setContent(Collections.singletonMap(DATA_FORMAT, getIndex()));
            e.consume();
        }
    }

    private static class LayerWithEnabledFlag
    {
        @NotNull
        private final SimpleBooleanProperty enabled = new SimpleBooleanProperty();
        @NotNull
        private final Layer layer;

        public LayerWithEnabledFlag(@NotNull Layer layer)
        {
            this.layer = layer;
            enabled.set(layer.isEnabled());
            enabled.addListener((enabled, oldValue, newValue) -> layer.setEnabled(newValue));
        }

        @NotNull
        public Layer getLayer()
        {
            return layer;
        }

        public boolean isEnabled()
        {
            return enabled.get();
        }

        @NotNull
        public SimpleBooleanProperty enabledProperty()
        {
            return enabled;
        }

        public void setEnabled(boolean enabled)
        {
            this.enabled.set(enabled);
        }
    }
}
