package ru.mipt.acsl.geotarget.model;

import ru.mipt.acsl.geotarget.ui.controls.parameters.tree.TreeNodeWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * @author Artem Shein
 */
public class RootTreeNode implements TreeNodeWrapper
{
    @NotNull
    private final SimpleStringProperty name;
    @NotNull
    private static final SimpleStringProperty info = new ReadOnlyStringWrapper("");
    @NotNull
    private static final SimpleStringProperty value = new ReadOnlyStringWrapper("");
    @NotNull
    private static final SimpleStringProperty type = new ReadOnlyStringWrapper("");
    @NotNull
    private static final SimpleObjectProperty<LocalDateTime> time = new SimpleObjectProperty<>();

    public RootTreeNode(@NotNull String title)
    {
        name = new ReadOnlyStringWrapper(title);
    }

    @Override
    @NotNull
    public SimpleStringProperty nameProperty()
    {
        return name;
    }

    @NotNull
    @Override
    public SimpleStringProperty infoProperty()
    {
        return info;
    }

    @NotNull
    @Override
    public SimpleStringProperty valueProperty()
    {
        return value;
    }

    @Override
    @NotNull
    public SimpleStringProperty typeProperty() {
        return type;
    }

    @Override
    @NotNull
    public SimpleObjectProperty<LocalDateTime> timeProperty() {
        return time;
    }
}
