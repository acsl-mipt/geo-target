package ru.mipt.acsl.fsm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * @author Artem Shein
 */
public class Transition<M, A>
{
    @NotNull
    private final M from;
    @NotNull
    private final A action;
    @NotNull
    private final M to;
    @Nullable
    private final Action apply;
    @Nullable
    private final Supplier<Boolean> constraint;

    public Transition(@NotNull M from, @NotNull A action, @NotNull M to)
    {
        this(from, action, to, null);
    }

    public Transition(@NotNull M from, @NotNull A action, @NotNull M to, @Nullable Action apply)
    {
        this(from, action, to, apply, null);
    }

    public Transition(@NotNull M from, @NotNull A action, @NotNull M to, @Nullable Action apply, @Nullable Supplier<Boolean> constraint)
    {
        this.from = from;
        this.action = action;
        this.to = to;
        this.apply = apply;
        this.constraint = constraint;
    }

    @NotNull
    public M getFrom()
    {
        return from;
    }

    @NotNull
    public A getAction()
    {
        return action;
    }

    @NotNull
    public M getTo()
    {
        return to;
    }

    @Nullable
    public Action getApply()
    {
        return apply;
    }

    @Nullable
    public Supplier<Boolean> getConstraint()
    {
        return constraint;
    }

    public interface Action
    {
        void run();
    }
}
