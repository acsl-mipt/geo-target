package ru.cpb9.geotarget.ui.controls.parameters.tree;

import ru.cpb9.geotarget.model.TraitInfo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

/**
 * @author Alexander Kuchuk.
 */


public class TraitWrapper implements TreeNodeWrapper
{
    private final TraitInfo traitInfo;

    public TraitWrapper(TraitInfo traitInfo)
    {
        this.traitInfo = traitInfo;
    }



    @Override
    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(traitInfo.getName());
    }

    @Override
    public SimpleStringProperty infoProperty() {
        return new SimpleStringProperty(traitInfo.getInfo());
    }

    @Override
    public SimpleStringProperty valueProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public SimpleStringProperty typeProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public SimpleObjectProperty<LocalDateTime> timeProperty() {
        return new SimpleObjectProperty<>();
    }
}
