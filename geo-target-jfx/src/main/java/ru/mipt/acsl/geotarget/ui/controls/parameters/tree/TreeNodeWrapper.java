package ru.mipt.acsl.geotarget.ui.controls.parameters.tree;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * @author Alexander Kuchuk.
 */


public interface TreeNodeWrapper
{
    @NotNull
    public SimpleStringProperty nameProperty();
    @NotNull
    public SimpleStringProperty infoProperty();
    @NotNull
    public SimpleStringProperty valueProperty();
    @NotNull
    public SimpleStringProperty typeProperty();
    @NotNull
    public SimpleObjectProperty<LocalDateTime> timeProperty();
}
