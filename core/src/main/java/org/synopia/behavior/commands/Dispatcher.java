package org.synopia.behavior.commands;

import org.synopia.behavior.commands.lib.CountDownCommand;
import org.synopia.behavior.commands.lib.DelayCommand;
import org.synopia.behavior.commands.lib.IfCommand;
import org.synopia.behavior.commands.lib.PrintCommand;
import org.synopia.behavior.tree.CompiledBehaviorTree;
import org.synopia.behavior.tree.Context;
import org.synopia.behavior.tree.BehaviorAction;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.Callback;
import org.synopia.behavior.tree.DebugCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by synopia on 15.07.2014.
 */
public class Dispatcher implements Callback {
    private Map<String, Command> commands = new HashMap<>();
    private CompiledBehaviorTree tree;

    public Dispatcher(CompiledBehaviorTree tree) {
        this.tree = tree;
        tree.callback = this;
        tree.debugCallback = DebugCallback.NULL;

        register(new PrintCommand());
        register(new CountDownCommand());
        register(new IfCommand());
        register(new DelayCommand());
    }

    public void register(Command cb) {
        commands.put(cb.name(), cb);
    }

    public BehaviorState run() {
        return tree.run();
    }

    @Override
    public BehaviorState execute(int id, BehaviorAction action, String command) {
        Command cb = commands.get(command);
        BehaviorState result = BehaviorState.UNDEFINED;

        if (cb != null) {
            switch (action) {
                case CONSTRUCT:
                    cb.construct(tree.context);
                    break;
                case DESTRUCT:
                    cb.destruct(tree.context);
                    break;
                case EXECUTE:
                    result = cb.execute(tree.context);
                    break;
                case PRUNE:
                    result = cb.prune(tree.context);
                    break;
                case MODIFY:
                    result = cb.modify(tree.context);
                    break;

                default:
                    throw new IllegalStateException("Unknown action " + action + " for " + command);
            }
        }
        return result;
    }

}
