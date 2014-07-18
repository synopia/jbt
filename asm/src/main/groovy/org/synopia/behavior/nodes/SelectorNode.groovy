package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.commons.GeneratorAdapter.EQ

/**
 * Created by synopia on 13.07.2014.
 */
class SelectorNode extends CompositeNode {
    @Override
    Map defaults() {
        deepMerge(super.defaults(), [locals: [[reEntry: -1]], commands: "selector"])
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
        List<Label> labels = children.collectAll {
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
        labels.each { it ->
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
            gen.ifICmp(EQ, exitSuccess)

            index++
        }
        gen.push(BehaviorState.FAILURE.ordinal())
        gen.goTo(exitFailure)

        gen.mark(dflt)
        gen.goTo(loop)

        gen.mark(exitSuccess)
        gen.push(BehaviorState.SUCCESS.ordinal())

        gen.mark(exitFailure)
        gen.store(find("reEntry")) {
            gen.push(-1)
        }

        gen.mark(exitRunning)
    }
}
