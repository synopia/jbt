package org.synopia.behavior.commands;

import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Context;

/**
 * Created by synopia on 15.07.2014.
 */
public interface Command {
    void construct(Context context);

    BehaviorState execute(Context context);

    void destruct(Context context);

    BehaviorState prune(Context context);

    BehaviorState modify(Context context);

    String name();

}
