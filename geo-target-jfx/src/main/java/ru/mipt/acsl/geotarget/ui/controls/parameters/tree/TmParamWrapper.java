package ru.mipt.acsl.geotarget.ui.controls.parameters.tree;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

/**
 * @author Alexander Kuchuk.
 */


public class TmParamWrapper {}/* implements TreeNodeWrapper
{

    private String tmParamStatus;
    private TmParameter tmParamValue;

    public TmParamWrapper(String tmParamStatus, TmParameter tmParamValue)
    {
        this.tmParamStatus = tmParamStatus;
        this.tmParamValue = tmParamValue;
    }
    @Override
    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(tmParamStatus);
    }

    @Override
    public SimpleStringProperty infoProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public SimpleStringProperty valueProperty() {
        return tmParamValue.valueProperty();
    }

    @Override
    public SimpleStringProperty typeProperty() {
        return tmParamValue.unitProperty();
    }

    @Override
    public SimpleObjectProperty<LocalDateTime> timeProperty() {
        return tmParamValue.timeProperty();
    }
}*/
