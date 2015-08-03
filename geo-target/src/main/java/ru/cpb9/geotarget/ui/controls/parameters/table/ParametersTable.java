package ru.cpb9.geotarget.ui.controls.parameters.table;

import c10n.C10N;
import ru.cpb9.geotarget.DateTimeUtils;
import ru.cpb9.geotarget.DeviceController;
import ru.cpb9.geotarget.model.TmParameter;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Alexander Kuchuk
 * @author Artem Shein
 */

public class ParametersTable extends TableView<TmParameter>
{
    private static final Messages I = C10N.get(Messages.class);
    private final ObservableList<TmParameter> parametersList = FXCollections.observableArrayList();
    private final DeviceController deviceController;

    public ParametersTable(@NotNull DeviceController deviceController)
    {
        this.deviceController = deviceController;
        deviceController.getDeviceRegistry().getDevices().addListener((Observable observable) -> updateData());
        updateData();
        TableColumn<TmParameter, String> timeColumn = new TableColumn<>(I.time());
        timeColumn.setCellValueFactory(cellData -> new StringBinding()
        {
            {
                bind(cellData.getValue().timeProperty());
            }
            @Override
            protected String computeValue()
            {
                LocalDateTime localDateTime = cellData.getValue().timeProperty().get();
                return localDateTime == null ? "" : DateTimeUtils.format(localDateTime);
            }
        });
        TableColumn<TmParameter, String> traitColumn = new TableColumn<>(I.trait());
        traitColumn.setCellValueFactory(cellData -> cellData.getValue().traitProperty());
        TableColumn<TmParameter, String> deviceColumn = new TableColumn<>(I.device());
        deviceColumn.setCellValueFactory(cellData -> cellData.getValue().deviceProperty());
        TableColumn<TmParameter, String> statusColumn = new TableColumn<>(I.status());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        TableColumn<TmParameter, String> valueColumn = new TableColumn<>(I.value());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        getColumns().addAll(timeColumn, traitColumn, deviceColumn, statusColumn, valueColumn);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }

    private void updateData()
    {
        parametersList.clear();
        deviceController.getDeviceRegistry().getDevices().stream().forEach(device -> Optional.ofNullable(device.getFirmware()).ifPresent(
                firmware -> firmware.getTraitInfoList()
                        .forEach(
                                trait -> trait.getStatusMap()
                                        .forEach((status, parameter) -> parametersList.add(parameter)))));
        setItems(parametersList);
        Platform.runLater(() -> {
            getColumns().get(0).setVisible(false);
            getColumns().get(0).setVisible(true);
        });
    }

}
