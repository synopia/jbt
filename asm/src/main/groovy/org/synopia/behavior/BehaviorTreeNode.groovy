package org.synopia.behavior

import org.objectweb.asm.Type
import org.objectweb.asm.commons.Method
import org.synopia.behavior.generators.BTClassGenerator
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.nodes.BaseBehaviorNode
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.CompiledBehaviorTree

import static org.objectweb.asm.commons.GeneratorAdapter.EQ

/**
 * Created by synopia on 12.07.2014.
 */
class BehaviorTreeNode extends BaseBehaviorNode {
    protected BehaviorNode root

    @Override
    Map defaults() {
        deepMerge(super.defaults(), [construct: false, prune: false, modify: false, destruct: false, locals: [[state: -1]]])
    }

    @Override
    int size() {
        super.size() + root.size()
    }

    void addChild(BehaviorNode child) {
        this.root = child
    }

    void assemble(BTClassGenerator cgen) {
        assembleSetup()
        cgen.generateMethod("org.synopia.behavior.tree.BehaviorState run()") { BTMethodGenerator gen ->
            Method vals = Method.getMethod("org.synopia.behavior.tree.BehaviorState[] values()")

            assembleExecute(gen)

            gen.invokeStatic(Type.getType(BehaviorState), vals)
            gen.load(find("state"))
            gen.arrayLoad(Type.getType(BehaviorState))
            gen.returnValue()
        }
        cgen.generateMethod("void <init>()") { BTMethodGenerator gen ->
            gen.loadThis()
            gen.push(size())
            gen.invokeConstructor(Type.getType(CompiledBehaviorTree.class), Method.getMethod("void <init>(int)"))
            gen.returnValue()

        }
        assembleTearDown()
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        gen.load(find("state"))
        gen.push(BehaviorState.RUNNING.ordinal())
        def label = gen.newLabel()
        gen.ifICmp(EQ, label)
        root.assembleConstruct(gen)

        gen.mark(label)
        gen.store(find("state")) {
            root.assembleExecute(gen)
        }

        gen.load(find("state"))
        gen.push(BehaviorState.RUNNING.ordinal())
        label = gen.newLabel()
        gen.ifICmp(EQ, label)
        root.assembleDestruct(gen)

        gen.mark(label)
    }

    @Override
    void assembleSetup() {
        super.assembleSetup()

        int mo = localsSize + memoryOffset
        root.memoryOffset = mo
        root.assembleSetup()
    }

    @Override
    void assembleTearDown() {
        super.assembleTearDown()
        root.assembleTearDown()
    }
}
