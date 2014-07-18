package org.synopia.behavior.commands.lib;

import org.synopia.behavior.commands.Command;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Context;

import java.util.Date;

/**
 * Created by synopia on 15.07.2014.
 */
public class DelayCommand implements Command {
    @Override
    public BehaviorState execute(Context context) {
        return condition(context) ? BehaviorState.SUCCESS : BehaviorState.RUNNING;
    }

    @Override
    public BehaviorState prune(Context context) {
        return execute(context);
    }

    @Override
    public BehaviorState modify(Context context) {
        return BehaviorState.UNDEFINED;
    }

    boolean condition(Context context) {
        int timeMarker = context.memory.getInt(context.memoryOffset);
        int delta = timeMarker - (int) (new Date().getTime());
        return delta < 0;
    }

    @Override
    public void construct(Context context) {
        int delay = context.memory.getInt(context.memoryOffset);
        int millis = (int) (new Date().getTime());
        context.memory.setInt(context.memoryOffset, millis + delay);
    }

    @Override
    public void destruct(Context context) {
        context.memory.setInt(context.memoryOffset, -1);
    }

    @Override
    public String name() {
        return "delay";
    }
}
