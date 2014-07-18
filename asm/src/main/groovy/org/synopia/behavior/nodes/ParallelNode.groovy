package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.Opcodes.*
import static org.objectweb.asm.commons.GeneratorAdapter.EQ
import static org.objectweb.asm.commons.GeneratorAdapter.NE

/**
 * Created by synopia on 13.07.2014.
 */
class ParallelNode extends CompositeNode {

    @Override
    Map defaults() {
        deepMerge(super.defaults(), [locals: [[success_counter: -1]], command: "parallel"])
    }

    @Override
    int size() {
        def childrenSize = 0
        children.each { child ->
            childrenSize += child.size()
        }
        super.size() + childrenSize
    }

    @Override
    void assembleSetup() {
        super.assembleSetup()

        int mo = localsSize + memoryOffset
        children.each { child ->
            child.memoryOffset = mo
            child.assembleSetup()
            mo += child.size()
        }
    }

    @Override
    void assembleConstruct(BTMethodGenerator gen) {
        super.assembleConstruct(gen)

        children.each { child ->
            child.assembleConstruct(gen)
        }
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        gen.store(find("success_counter")) {
            gen.push(0)
        }
        Label exitFailure = gen.newLabel()

        children.each { BehaviorNode node ->
            node.assembleExecute(gen)

            gen.dup()
            gen.push(BehaviorState.FAILURE.ordinal());
            gen.ifICmp(EQ, exitFailure)

            Label skip = new Label()
            gen.push(BehaviorState.SUCCESS.ordinal())
            gen.ifICmp(NE, skip)

            gen.store(find("success_counter")) {
                gen.load(find("success_counter"))
                gen.push(1)
                gen.visitInsn(IADD)
            }

            gen.mark(skip)
        }
        gen.push(BehaviorState.RUNNING.ordinal())

        gen.load(find("success_counter"))
        gen.push(children.size())
        gen.ifICmp(NE, exitFailure)

        gen.pop()
        gen.push(BehaviorState.SUCCESS.ordinal())

        gen.mark(exitFailure)
    }

    @Override
    void assembleDestruct(BTMethodGenerator gen) {
        children.each { child ->
            child.assembleDestruct(gen)
        }
    }
}
