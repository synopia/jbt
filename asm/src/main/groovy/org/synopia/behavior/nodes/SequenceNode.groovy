package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.commons.GeneratorAdapter.EQ
import static org.objectweb.asm.commons.GeneratorAdapter.NE

/**
 * Created by synopia on 12.07.2014.
 */
class SequenceNode extends CompositeNode {

    @Override
    Map defaults() {
        deepMerge(super.defaults(), [locals: [[reEntry: -1]], command: "sequence"])
    }

    @Override
    int size() {
        def childrenSize = 0
        children.each { child ->
            childrenSize = Math.max(childrenSize, child.size())
        }
        super.size() + childrenSize
    }

    @Override
    void assembleSetup() {
        super.assembleSetup()
        children.each { child ->
            child.memoryOffset = localsSize + memoryOffset
            child.assembleSetup()
        }
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        List labels = children.collectAll {
            gen.newLabel()
        }
        Label dflt = gen.newLabel()
        Label exitSuccess = gen.newLabel()
        Label exitRunning = gen.newLabel()
        Label exitFailure = gen.newLabel()
        gen.load(find("reEntry"))
        gen.visitTableSwitchInsn(0, labels.size() - 1, dflt, (Label[]) labels.toArray(new Label[labels.size()]))
        def index = 0
        Label loop = gen.mark()
        labels.each { Label it ->
            children[index].assembleConstruct(gen)
            gen.mark(it)
            gen.store(find("reEntry")) {
                gen.push(index)
            }

            children[index].assembleExecute(gen)
            gen.dup()
            gen.push(BehaviorState.RUNNING.ordinal())
            gen.ifICmp(EQ, exitRunning)

            children[index].assembleDestruct(gen)
            gen.push(BehaviorState.SUCCESS.ordinal())
            gen.ifICmp(NE, exitFailure)

            index++
        }
        gen.push(BehaviorState.SUCCESS.ordinal())
        gen.goTo(exitSuccess)

        gen.mark(dflt)
        gen.goTo(loop)

        gen.mark(exitFailure)
        gen.push(BehaviorState.FAILURE.ordinal())

        gen.mark(exitSuccess)
        gen.store(find("reEntry")) {
            gen.push(-1)
        }
        gen.mark(exitRunning)
    }
}
