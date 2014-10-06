package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.Visitor
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.commons.GeneratorAdapter.NE

/**
 * Created by synopia on 12.07.2014.
 */
class DecoratorNode extends BaseBehaviorNode {
    private BehaviorNode child
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

    @Override
    public void insertChild(int index, BehaviorNode newChild) {
        this.child = newChild;
    }

    @Override
    public void replaceChild(int index, BehaviorNode newChild) {
        this.child = newChild;
    }

    @Override
    public BehaviorNode removeChild(int index) {
        BehaviorNode old = child;
        child = null;
        return old;
    }

    @Override
    public BehaviorNode getChild(int index) {
        return child;
    }

    @Override
    public int getChildrenCount() {
        return child == null ? 0 : 1;
    }

    @Override
    public int getMaxChildren() {
        return 1;
    }

    public BehaviorNode getChild() {
        return child;
    }

    public void setChild(BehaviorNode child) {
        this.child = child;
    }

    @Override
    public <T> T visit(T item, Visitor<T> visitor) {
        T visit = super.visit(item, visitor);
        if (child != null) {
            child.visit(visit, visitor);
        }
        return visit;
    }
}
