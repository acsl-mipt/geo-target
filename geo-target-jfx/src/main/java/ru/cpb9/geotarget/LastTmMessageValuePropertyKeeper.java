package ru.cpb9.geotarget;

import com.google.common.base.Preconditions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class LastTmMessageValuePropertyKeeper
{
    private Map<Class<?>, ObjectProperty<?>> lastTmMessageValueMap = new HashMap<>();

    public <T> void setTmMessageValue(@NotNull Class<T> tmMessageClass, @NotNull T value)
    {
        Preconditions.checkArgument(tmMessageClass.isInstance(value));
        //noinspection unchecked
        ((ObjectProperty<T>) lastTmMessageValueMap.computeIfAbsent(tmMessageClass, k -> new SimpleObjectProperty<T>()))
                .set(value);
    }

    @NotNull
    public <T> ObjectProperty<T> getTmMessageProperty(@NotNull Class<T> tmMessageClass)
    {
        //noinspection unchecked
        return (ObjectProperty<T>) lastTmMessageValueMap.computeIfAbsent(tmMessageClass, k -> new SimpleObjectProperty<T>());
    }

    @NotNull
    public <T> Optional<T> getTmMessageValue(@NotNull Class<T> tmMessageClass)
    {
        return Optional.ofNullable(getTmMessageProperty(tmMessageClass).get());
    }
}
