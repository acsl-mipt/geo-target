package ru.cpb9.geotarget.ui.controls;

import c10n.C10N;
import com.google.common.base.Preconditions;
import ru.cpb9.geotarget.DeviceController;
import ru.cpb9.geotarget.Trait;
import ru.cpb9.geotarget.model.Device;
import ru.cpb9.geotarget.DeviceRegistry;
import ru.cpb9.geotarget.TmStatus;
import ru.cpb9.geotarget.model.TmParameter;
import ru.cpb9.geotarget.model.TraitInfo;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Artem Shein
 */
public class DeviceList extends ListView<Device>
{
    private static final Messages I = C10N.get(Messages.class);
    public static final int SIGNAL_LEVEL_SIZE = 100;
    @NotNull
    private final DeviceController deviceController;

    public DeviceList(@NotNull DeviceController deviceController)
    {
        this.deviceController = deviceController;
        DeviceRegistry deviceRegistry = deviceController.getDeviceRegistry();
        setCellFactory(list -> new DeviceCell());
        getSelectionModel().selectedIndexProperty().addListener((o, oldValue, newValue) -> {
            deviceRegistry.setActiveDevice(newValue == null ? null : getItems().get(newValue.intValue()));
        });
        Device activeDevice = deviceRegistry.getActiveDevice().getValue();
        if (activeDevice != null)
        {
            getSelectionModel().select(activeDevice);
        }
        ObservableList<Device> devices = deviceRegistry.getDevices();
        setItems(devices);
        devices.addListener((Observable observable) -> setItems(devices));
    }

    private class DeviceCell extends ListCell<Device>
    {
        private long dataIndex = 0;

        public DeviceCell()
        {
            setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2)
                {
                    deviceController.navigateToActiveDevice();
                }
            });
        }

        @Override
        public void updateItem(Device device, boolean empty)
        {
            super.updateItem(device, empty);
            if (!empty)
            {
                VBox vBox = new VBox();
                HBox chartBox = new HBox();
                NumberAxis xAxis = new NumberAxis();
                xAxis.setTickLabelsVisible(false);
                xAxis.setOpacity(0);
                xAxis.setAutoRanging(false);
                NumberAxis yAxis = new NumberAxis(0, 100, 25);
                AreaChart<Number, Number> signalChart = new AreaChart<>(xAxis, yAxis);
                XYChart.Series<Number, Number> signalLevels = new XYChart.Series<>(FXCollections.observableList(
                        new LinkedList<>()));
                signalLevels.setName(I.signalLevel());
                TraitInfo deviceTraitInfo = Preconditions.checkNotNull(device.getTraitOrNull(Trait.DEVICE));
                TraitInfo motionTraitInfo = Preconditions.checkNotNull(device.getTraitOrNull(Trait.NAVIGATION_MOTION));
                Text signalLevelText = new Text();
                signalLevelText.setStyle("-fx-background-color: #ffffff;");
                TmParameter signalLevelParameter = Preconditions.checkNotNull(
                        deviceTraitInfo.getStatusMap().get("signalLevel"));
                signalLevelParameter.valueProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                ObservableList<XYChart.Data<Number, Number>> data = signalLevels.getData();
                                if (data.size() > SIGNAL_LEVEL_SIZE)
                                {
                                    data.remove(0);
                                    xAxis.setLowerBound(data.get(0).getXValue().doubleValue());
                                }
                                xAxis.setUpperBound(dataIndex);
                                data.add(new XYChart.Data<>(dataIndex++, Integer.parseInt(newValue)));
                                signalLevelText.setText(newValue + signalLevelParameter.getUnit());
                            });
                        });
                signalChart.getData().add(signalLevels);
                signalChart.setPrefHeight(100);
                signalChart.setMaxWidth(Double.MAX_VALUE);
                signalChart.setMinWidth(100);
                StackPane signalChartPane = new StackPane();
                signalChartPane.getChildren().addAll(signalChart, signalLevelText);
                chartBox.getChildren().add(signalChartPane);
                TmParameter throttleParameter = Preconditions.checkNotNull(
                        motionTraitInfo.getStatusMap().get("throttle"));
                ProgressBar throttleBar = new ProgressBar();
                throttleBar.setMaxWidth(Double.MAX_VALUE);
                throttleBar.setPrefHeight(30);
                Text throttleBarText = new Text();
                Consumer<String> throttleBarUpdateValue = (String value) -> {
                    throttleBar.setProgress(Integer.parseInt(value));
                    throttleBarText.setText(String.format("%s: %s%s", throttleParameter.getInfo(), value, throttleParameter.getUnit()));
                };
                Optional.ofNullable(throttleParameter.getValue()).ifPresent(throttleBarUpdateValue);
                throttleParameter.valueProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            Platform.runLater(() -> throttleBarUpdateValue.accept(newValue));
                        });
                StackPane throttleBarPane = new StackPane();
                throttleBarPane.getChildren().addAll(throttleBar, throttleBarText);
                HBox titleBox = new HBox(new Text(device.getName()));
                Button firstPersonViewButton = new Button(I.firstPersonView());
                firstPersonViewButton.setOnAction(e -> {
                    BasicFlyView flyView = new BasicFlyView();
                    deviceController.getWorldWind().getPanel().setView(flyView);
                    device.addListenerToStatuses((observable, oldValue, newValue) -> {
                        updateFlyViewForDevice(flyView, device);
                    }, TmStatus.NAVIGATION_MOTION_MOTION_STATUSES);
                });
                vBox.getChildren().addAll(titleBox, chartBox, throttleBarPane);
                setGraphic(vBox);
            }
        }

        private void updateFlyViewForDevice(@NotNull BasicFlyView flyView, @NotNull Device device)
        {
            double latitude = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_LATITUDE);
            double longitude = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_LONGITUDE);
            double altitude = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_ALTITUDE);
            double pitch = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_PITCH);
            double roll = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_ROLL);
            double heading = device.getTraitStatusValueAsDoubleOrThrow(TmStatus.NAVIGATION_MOTION_HEADING);
            // TODO
        }
    }
}
