package ru.cpb9.geotarget.ui.controls.parameters.tree;

import c10n.C10N;
import ru.cpb9.geotarget.DateTimeUtils;
import ru.cpb9.geotarget.DeviceController;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.model.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Alexander Kuchuk.
 */


public class ParametersTree extends VBox
{
    private static final Messages I = C10N.get(Messages.class);
    private TreeTableView<TreeNodeWrapper> paramsTree;

    public ParametersTree(@NotNull DeviceController deviceController)
    {
//        ObservableList<Device> devices = deviceController.getDeviceRegistry().getDevices();
//        devices.addListener((ListChangeListener<Device>) c -> {
//            setNodes(devices);
//            Platform.runLater(() -> {
//                paramsTree.getColumns().get(0).setVisible(false);
//                paramsTree.getColumns().get(0).setVisible(true);
//            });
//        });

        TreeItem<TreeNodeWrapper> rootNode = new TreeItem<>(new RootTreeNode(I.devices()));
        paramsTree = new TreeTableView<>(rootNode);

        TreeTableColumn<TreeNodeWrapper, String> mccColumn = new TreeTableColumn<>(I.tableTreeColumnMcc());
        TreeTableColumn<TreeNodeWrapper, String> valueColumn = new TreeTableColumn<>(I.tableTreeColumnValue());
        TreeTableColumn<TreeNodeWrapper, String> timeColumn = new TreeTableColumn<>(I.tableTreeColumnTime());
        TreeTableColumn<TreeNodeWrapper, String> unitColumn = new TreeTableColumn<>(I.tableTreeColumnUnit());
        TreeTableColumn<TreeNodeWrapper, String> infoColumn = new TreeTableColumn<>(I.tableTreeColumnInfo());

        mccColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());

//        mccColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<TreeNodeWrapper, String> p) ->
//                new ReadOnlyStringWrapper(p.valueProperty().valueProperty().nameProperty().resolve()));

        infoColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<TreeNodeWrapper, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().infoProperty().get()));

//        valueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<TreeNodeWrapper, String> p) ->
//                new ReadOnlyStringWrapper(p.valueProperty().valueProperty().valueProperty().resolve()));

        valueColumn.setCellValueFactory(param -> param.getValue().getValue().valueProperty());
        timeColumn.setCellValueFactory(param -> new StringBinding()
        {
            {
                bind(param.getValue().getValue().timeProperty());
            }
            @Override
            protected String computeValue()
            {
                LocalDateTime localDateTime = param.getValue().getValue().timeProperty().get();
                return localDateTime == null ? "" : DateTimeUtils.format(localDateTime);
            }
        });
        unitColumn.setCellValueFactory(param -> param.getValue().getValue().typeProperty());

        paramsTree.getColumns().addAll(mccColumn, valueColumn, timeColumn, unitColumn, infoColumn);
        paramsTree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        getChildren().addAll(paramsTree);
        paramsTree.setShowRoot(true);
        setVgrow(paramsTree, Priority.ALWAYS);
    }
    public void setNodes(ObservableList<Device> devices)
    {
        paramsTree.getRoot().getChildren().clear();
        for (Device device : devices)
        {
            /*
            TreeItem<TreeNodeWrapper> deviceNode = new TreeItem<>(new DeviceWrapper(device));
            Optional<Firmware> firmware = Optional.ofNullable(device.getFirmware());
            TreeItem<TreeNodeWrapper> traitNode = null;
            if (firmware.isPresent())
            {
                for (TraitInfo traitInfo : firmware.get().getTraitInfoList())
                {
                    String[] traitSplit = traitInfo.getName().split(Pattern.quote("."), 2);
                    if (traitSplit.length > 1)
                    {
                        if (traitNode != null && (traitNode.getValue().nameProperty().get().equals(traitSplit[0])))
                        {
                            TreeItem<TreeNodeWrapper> traitItem = new TreeItem<>(new TraitWrapper(new TraitInfo(traitSplit[1], traitInfo
                                    .getKind(), traitInfo.getInfo())));
                            traitNode.getChildren().add(traitItem);
                            for (Map.Entry<String, TmParameter> set : traitInfo.getStatusMap().entrySet())
                            {
                                traitItem.getChildren().add(new TreeItem<>(new TmParamWrapper(set.getKey(), set.getValue())));
                            }
                        }
                        else
                        {
                            traitNode = new TreeItem<>(new TraitWrapper(new TraitInfo(traitSplit[0], traitInfo.getKind(), traitInfo
                                    .getInfo())));
                            traitNode.getChildren().add(new TreeItem<>(new TraitWrapper(new TraitInfo(traitSplit[1], traitInfo
                                    .getKind(), traitInfo.getInfo()))));
                            deviceNode.getChildren().add(traitNode);
                        }
                    }
                    else
                    {
                        traitNode = new TreeItem<>(new TraitWrapper(traitInfo));
                        for (Map.Entry<String, TmParameter> set : traitInfo.getStatusMap().entrySet())
                        {
                            traitNode.getChildren().add(new TreeItem<>(new TmParamWrapper(set.getKey(), set.getValue())));
                        }
                        deviceNode.getChildren().add(traitNode);
                    }
                }
            }
            paramsTree.getRoot().getChildren().add(deviceNode);*/
        }
    }
}
