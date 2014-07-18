package org.synopia.behavior.commands.lib;

import org.synopia.behavior.commands.Command;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Context;

/**
 * Created by synopia on 15.07.2014.
 */
public class PrintCommand implements Command {
    @Override
    public String name() {
        return "print";
    }

    @Override
    public BehaviorState execute(Context context) {
        System.out.println(context.arg);
        return BehaviorState.SUCCESS;
    }

    @Override
    public BehaviorState prune(Context context) {
        System.out.println(context.arg);
        return BehaviorState.SUCCESS;
    }

    @Override
    public BehaviorState modify(Context context) {
        System.out.println(context.arg);
        return BehaviorState.values()[context.returnValue];
    }

    @Override
    public void construct(Context context) {

    }

    @Override
    public void destruct(Context context) {

    }
}
