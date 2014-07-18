package org.synopia.behavior.commands.lib;

import org.synopia.behavior.commands.Command;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Context;

/**
 * Created by synopia on 15.07.2014.
 */
public class CountDownCommand implements Command {
    @Override
    public String name() {
        return "count_down";
    }

    @Override
    public BehaviorState execute(Context context) {
        int curr = context.tree.memory.getInt(context.memoryOffset);
        if (curr > 0) {
            curr -= 1;
            context.tree.memory.setInt(context.memoryOffset, curr);
            return BehaviorState.RUNNING;
        }
        return BehaviorState.SUCCESS;
    }

    @Override
    public void construct(Context context) {

    }

    @Override
    public void destruct(Context context) {

    }

    @Override
    public BehaviorState prune(Context context) {
        return BehaviorState.UNDEFINED;
    }

    @Override
    public BehaviorState modify(Context context) {
        return BehaviorState.UNDEFINED;
    }
}
