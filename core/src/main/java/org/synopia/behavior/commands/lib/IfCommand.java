package org.synopia.behavior.commands.lib;

import org.synopia.behavior.commands.Command;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Context;

/**
 * Created by synopia on 15.07.2014.
 */
public class IfCommand implements Command {
    @Override
    public String name() {
        return "if";
    }

    protected boolean condition(Context context) {
        return true;
    }

    @Override
    public BehaviorState prune(Context context) {
        return condition(context) ? BehaviorState.SUCCESS : BehaviorState.FAILURE;
    }

    @Override
    public BehaviorState modify(Context context) {
        return BehaviorState.UNDEFINED;
    }


    @Override
    public BehaviorState execute(Context context) {
        return prune(context);
    }

    @Override
    public void construct(Context context) {

    }

    @Override
    public void destruct(Context context) {

    }
}
