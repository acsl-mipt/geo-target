package ru.cpb9.fsm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Artem Shein
 */
public class Fsm<M, A>
{
    @NotNull
    private final Set<M> modes = new HashSet<>();
    @NotNull
    private M mode;
    @NotNull
    private Set<A> actions = new HashSet<>();
    @NotNull
    private final Map<M, Map<A, Transition<M, A>>> table = new HashMap<>();

    public Fsm(@NotNull M start, @NotNull Transition<M, A>[] transitions)
    {
        this(start, Lists.newArrayList(transitions));
    }

    public Fsm(@NotNull M start, @NotNull List<Transition<M, A>> transitions)
    {
        this.mode = start;
        for (Transition<M, A> transition : transitions)
        {
            M from = transition.getFrom();
            Map<A, Transition<M, A>> actionToTransitionMap = table.computeIfAbsent(from, k -> new HashMap<>());
            A action = transition.getAction();
            actions.add(action);
            Preconditions.checkState(!actionToTransitionMap.containsKey(action));
            actionToTransitionMap.put(action, transition);
            modes.add(from);
            modes.add(transition.getTo());
        }
        Preconditions.checkState(modes.contains(mode));
    }

    public boolean activate(A action)
    {
        Map<A, Transition<M, A>> actionToTransitionMap = table.get(mode);
        if (actionToTransitionMap == null)
        {
            return false;
        }
        Transition<M, A> transition = actionToTransitionMap.get(action);
        if (transition == null) {
            return false;
        }
        if (transition.getConstraint() == null || transition.getConstraint().get())
        {
            if (transition.getApply() != null)
            {
                transition.getApply().run();
            }
            mode = transition.getTo();
            return true;
        }
        return false;
    }

    public boolean isValidAction(A action)
    {
        return actions.contains(action);
    }
}
