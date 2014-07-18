package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.commons.GeneratorAdapter.NE

/**
 * Created by synopia on 12.07.2014.
 */
class DecoratorNode extends BaseBehaviorNode {
    protected BehaviorNode child
    boolean construct
    boolean prune
    boolean modify
    boolean destruct

    @Override
    Map defaults() {
        deepMerge(super.defaults(), [construct: true, prune: true, modify: true, destruct: true])
    }

    @Override
    Map setAttributes(Map map) {
        map = super.setAttributes(map)

        construct = map['construct']
        prune = map['prune']
        modify = map['modify']
        destruct = map['destruct']

        return map
    }

    void addChild(BehaviorNode child) {
        this.child = child
    }

    @Override
    void assembleSetup() {
        super.assembleSetup()

        child.assembleSetup()
    }

    @Override
    void assembleConstruct(BTMethodGenerator gen) {
        super.assembleConstruct(gen)

        if (construct) {
            gen.invokeCallback(this, BehaviorAction.CONSTRUCT, -1)
            gen.pop()
        }

        child.assembleConstruct(gen)
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        Label exit = gen.newLabel()
        if (prune) {
            gen.invokeCallback(this, BehaviorAction.PRUNE, -1)
            gen.dup()
            gen.push(BehaviorState.SUCCESS.ordinal())
            gen.ifICmp(NE, exit)
            gen.pop()
        }
        child.assembleExecute(gen)
        if (modify) {
            gen.pop()
            gen.invokeCallback(this, BehaviorAction.MODIFY, -1)
        }

        gen.mark(exit)
    }

    @Override
    void assembleDestruct(BTMethodGenerator gen) {
        if (destruct) {
            gen.invokeCallback(this, BehaviorAction.DESTRUCT, -1)
            gen.pop()
        }
        child.assembleDestruct(gen)
    }

    @Override
    void assembleTearDown() {
        child.assembleTearDown()
    }
}
