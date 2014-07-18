package org.synopia.behavior.nodes

import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 12.07.2014.
 */
class ActionNode extends BaseBehaviorNode {
    String arg
    boolean construct
    boolean destruct
    boolean execute
    Map customDefaults

    ActionNode(Map customDefaults) {
        this.customDefaults = customDefaults
    }

    @Override
    Map defaults() {
        def res = deepMerge(super.defaults(), [construct: true, destruct: true, execute: true])
        def d = deepMerge(res, customDefaults)
        return d
    }

    @Override
    Map setAttributes(Map map) {
        map = super.setAttributes(map)

        arg = map['arg'];
        construct = map['construct']
        execute = map['execute']
        destruct = map['destruct']

        return map
    }

    @Override
    String name() {
        return "action " + command;
    }

    @Override
    void assembleConstruct(BTMethodGenerator gen) {
        super.assembleConstruct(gen)
        if (construct) {
            gen.invokeCallback(this, BehaviorAction.CONSTRUCT, -1, arg)
            gen.pop()
        }
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        if (execute) {
            gen.invokeCallback(this, BehaviorAction.EXECUTE, -1, arg)
        } else {
            gen.push(BehaviorState.UNDEFINED.ordinal())
        }
    }

    @Override
    void assembleDestruct(BTMethodGenerator gen) {
        if (destruct) {
            gen.invokeCallback(this, BehaviorAction.DESTRUCT, -1, arg)
            gen.pop()
        }
    }
}
