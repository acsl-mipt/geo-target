package ru.cpb9.geotarget.ui.controls;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import c10n.C10N;
import com.google.common.collect.Sets;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import ru.cpb9.device.modeling.KnownTmMessages;
import ru.cpb9.geotarget.*;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.cpb9.geotarget.akka.messages.TmMessageSubscribe;
import ru.cpb9.geotarget.akka.messages.TmMessageUnsubscribe;
import ru.cpb9.geotarget.client.akka.ActorName;
import ru.cpb9.geotarget.model.Device;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import javafx.application.Platform;
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
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.mipt.acsl.DeviceComponent;
import ru.mipt.acsl.MotionComponent;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class DeviceList extends ListView<DeviceInfo>
{
    private static final Messages I = C10N.get(Messages.class);
    public static final int SIGNAL_LEVEL_SIZE = 100;
    @NotNull
    private final DeviceController deviceController;

    public DeviceList(@NotNull DeviceController deviceController, @NotNull ActorRef tmServer)
    {
        ActorsRegistry.getInstance().makeActor(DeviceListUpdateActor.class,
                ActorName.DEVICE_LIST_UPDATE.getName(), this, tmServer);
        this.deviceController = deviceController;
        DeviceRegistry deviceRegistry = deviceController.getDeviceRegistry();
        setCellFactory(list -> new DeviceCell());
        getSelectionModel().selectedIndexProperty().addListener((o, oldValue, newValue) -> {
            deviceRegistry.setActiveDevice(newValue == null ? Optional.empty() : Optional.of(
                    getItems().get(newValue.intValue()).getDevice()));
        });
        Optional<Device> activeDevice = deviceRegistry.activeDeviceProperty().getValue();
        ObservableList<Device> devices = deviceRegistry.getDevices();
        getItems().addAll(devices.stream().map(DeviceInfo::new).collect(Collectors.toList()));
        activeDevice.ifPresent(d -> getSelectionModel()
                .select(getItems().stream().filter(di -> di.getDevice().equals(d)).findAny()
                        .orElseThrow(AssertionError::new)));
        devices.addListener((ListChangeListener<Device>) c -> {
            while (c.next())
            {
                if (!(c.wasPermutated() || c.wasUpdated()))
                {
                    if (c.getRemovedSize() > 0)
                    {
                        Set<Device> removedDevices = Sets.newHashSet(c.getRemoved());
                        Iterator<DeviceInfo> iterator = getItems().iterator();
                        while (iterator.hasNext())
                        {
                            if (removedDevices.contains(iterator.next().getDevice()))
                            {
                                iterator.remove();
                            }
                        }
                    }
                    if (c.getAddedSize() > 0)
                    {
                        getItems()
                                .addAll(c.getAddedSubList().stream().map(DeviceInfo::new).collect(Collectors.toList()));
                    }
                }
            }
        });
    }

    private class DeviceCell extends ListCell<DeviceInfo>
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
        public void updateItem(DeviceInfo deviceInfo, boolean empty)
        {
            super.updateItem(deviceInfo, empty);
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
                SimpleObjectProperty<MotionComponent.AllMessage> motionProperty = deviceInfo.getMotionProperty();
                SimpleObjectProperty<DeviceComponent.AllMessage> deviceProperty = deviceInfo.getDeviceProperty();
                Text signalLevelText = new Text();
                signalLevelText.setStyle("-fx-background-color: #ffffff;");
                deviceProperty.addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                ObservableList<XYChart.Data<Number, Number>> data = signalLevels.getData();
                                if (data.size() > SIGNAL_LEVEL_SIZE)
                                {
                                    data.remove(0);
                                    xAxis.setLowerBound(data.get(0).getXValue().doubleValue());
                                }
                                xAxis.setUpperBound(dataIndex);
                                short signalLevel = newValue.getSignalLevel();
                                data.add(new XYChart.Data<>(dataIndex++, signalLevel));
                                // TODO: use unit from model (model should generate Java source for it)
                                signalLevelText.setText(signalLevel + "%");
                            });
                        });
                signalChart.getData().add(signalLevels);
                signalChart.setPrefHeight(100);
                signalChart.setMaxWidth(Double.MAX_VALUE);
                signalChart.setMinWidth(100);
                StackPane signalChartPane = new StackPane();
                signalChartPane.getChildren().addAll(signalChart, signalLevelText);
                chartBox.getChildren().add(signalChartPane);
                /*TmParameter throttleParameter = Preconditions.checkNotNull(
                        motionTraitInfo.getStatusMap().get("throttle"));*/
                ProgressBar throttleBar = new ProgressBar();
                throttleBar.setMaxWidth(Double.MAX_VALUE);
                throttleBar.setPrefHeight(30);
                Text throttleBarText = new Text();
                Consumer<Short> throttleBarUpdateValue = (Short value) -> {
                    throttleBar.setProgress(value / 100.);
                    // TODO: use unit and info from model
                    throttleBarText.setText(String.format("%s: %s%s", "Тяга", value, "%"));
                };
                Optional.ofNullable(motionProperty.getValue()).map(MotionComponent.AllMessage::getThrottle).ifPresent(
                        throttleBarUpdateValue);
                motionProperty.addListener(
                        (observable, oldValue, newValue) -> {
                            Platform.runLater(() -> throttleBarUpdateValue.accept(newValue.getThrottle()));
                        });
                StackPane throttleBarPane = new StackPane();
                throttleBarPane.getChildren().addAll(throttleBar, throttleBarText);
                HBox titleBox = new HBox(new Text(deviceInfo.getDevice().getDeviceGuid().map(Object::toString).orElse("No name")));
                Button firstPersonViewButton = new Button(I.firstPersonView());
                firstPersonViewButton.setOnAction(e -> {
                    BasicFlyView flyView = new BasicFlyView();
                    deviceController.getWorldWind().getPanel().setView(flyView);
                    /* TODO:
                    deviceInfo.addListenerToStatuses((observable, oldValue, newValue) -> {
                        updateFlyViewForDevice(flyView, deviceInfo);
                    }, TmStatus.NAVIGATION_MOTION_MOTION_STATUSES);*/
                });
                vBox.getChildren().addAll(titleBox, chartBox, throttleBarPane);
                setGraphic(vBox);
            }
        }
    }

    public static class DeviceListUpdateActor extends UntypedActor
    {
        @NotNull
        private final ActorRef tmServer;
        @NotNull
        private final DeviceList deviceList;

        public DeviceListUpdateActor(@NotNull DeviceList deviceList, @NotNull ActorRef tmServer)
        {
            this.tmServer = tmServer;
            this.deviceList = deviceList;
            deviceList.getItems().stream().forEach(this::subscribeFor);
            deviceList.getItems().addListener((ListChangeListener<DeviceInfo>) c -> {
                while (c.next())
                {
                    if (!(c.wasPermutated() || c.wasUpdated()))
                    {
                        c.getRemoved().forEach(this::unsubscribeFrom);
                        c.getAddedSubList().forEach(this::subscribeFor);
                    }
                }
            });
        }

        private void subscribeFor(@NotNull DeviceInfo deviceInfo)
        {
            DeviceGuid deviceGuid = deviceInfo.getDevice().getDeviceGuid().orElseThrow(AssertionError::new);
            ActorRef self = getSelf();
            tmServer.tell(new TmMessageSubscribe(deviceGuid, KnownTmMessages.MOTION_ALL), self);
            tmServer.tell(new TmMessageSubscribe(deviceGuid, KnownTmMessages.DEVICE_ALL), self);
        }

        private void unsubscribeFrom(@NotNull DeviceInfo deviceInfo)
        {
            DeviceGuid deviceGuid = deviceInfo.getDevice().getDeviceGuid().orElseThrow(AssertionError::new);
            ActorRef self = getSelf();
            tmServer.tell(new TmMessageUnsubscribe(deviceGuid, KnownTmMessages.MOTION_ALL), self);
            tmServer.tell(new TmMessageUnsubscribe(deviceGuid, KnownTmMessages.DEVICE_ALL), self);
        }

        @Override
        public void onReceive(Object o) throws Exception
        {
            if (o instanceof TmMessage)
            {
                TmMessage tmMessage = (TmMessage)o;
                IfDevMessage message = tmMessage.getMessage();

                if (message.equals(KnownTmMessages.MOTION_ALL))
                {
                    MotionComponent.AllMessage allMessage = (MotionComponent.AllMessage) tmMessage.getValues();
                    deviceList.updateDeviceMotion(tmMessage.getDeviceGuid(), allMessage);
                }
                else if (message.equals(KnownTmMessages.DEVICE_ALL))
                {
                    DeviceComponent.AllMessage allMessage = (DeviceComponent.AllMessage) tmMessage.getValues();
                    deviceList.updateDevice(tmMessage.getDeviceGuid(), allMessage);
                }
            }
            else
            {
                unhandled(o);
            }
        }
    }

    private void updateDevice(@NotNull DeviceGuid deviceGuid, @NotNull DeviceComponent.AllMessage allMessage)
    {
        getItems().stream().filter(di -> di.getDevice().getDeviceGuid().map(deviceGuid::equals).orElse(false)).findAny()
                .ifPresent(di -> di.setDevice(allMessage));
    }

    private void updateDeviceMotion(@NotNull DeviceGuid deviceGuid, @NotNull MotionComponent.AllMessage allMessage)
    {
        getItems().stream().filter(di -> di.getDevice().getDeviceGuid().map(deviceGuid::equals).orElse(false)).findAny()
                .ifPresent(di -> di.setMotion(allMessage));
    }
}
