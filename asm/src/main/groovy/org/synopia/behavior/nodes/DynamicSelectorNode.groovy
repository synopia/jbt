package org.synopia.behavior.nodes

import org.objectweb.asm.Label
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorState

import static org.objectweb.asm.commons.GeneratorAdapter.EQ
import static org.objectweb.asm.commons.GeneratorAdapter.NE

/**
 * Created by synopia on 13.07.2014.
 */
class DynamicSelectorNode extends CompositeNode {

    @Override
    Map defaults() {
        deepMerge(super.defaults(), [locals: [[new_branch: -1], [old_branch: -1], [jump_back: -1]], command: "dynamic_selector"])
    }


    @Override
    void addChild(BehaviorNode child) {
        int cnt = children.size()

        super.addChild(child)

        allocate("$cnt", -1)
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
        gen.reserveLocal { int local ->
            Label exit = new Label(), trueExit = new Label(), jumpBack = new Label();
            gen.store(find("jump_back")) {
                gen.push(0)
            }
            List<Label> destructLabels = children.collect { child ->
                gen.newLabel()
            }
            List<Label> destructLabels2 = children.collect { child ->
                gen.newLabel()
            }
            children.eachWithIndex { child, int index ->
                gen.load(find("$index"))
                gen.push(BehaviorState.UNDEFINED.ordinal());
                Label skip = gen.newLabel();
                gen.ifICmp(NE, skip)

                child.assembleConstruct(gen)

                gen.mark(skip)

                gen.store(find("new_branch")) {
                    gen.push(index)
                }

                child.assembleExecute(gen)


                gen.storeLocal(local)

                gen.store(find("$index")) {
                    gen.loadLocal(local)
                }

                gen.loadLocal(local)
                gen.push(BehaviorState.RUNNING.ordinal());
                gen.ifICmp(EQ, exit);

                gen.mark(destructLabels[index])
                child.assembleDestruct(gen)
                gen.load(find("jump_back"))
                gen.push(1)

                gen.ifICmp(EQ, jumpBack)

                gen.loadLocal(local)
                gen.push(BehaviorState.SUCCESS.ordinal());
                gen.ifICmp(EQ, exit);

            }
            gen.goTo(trueExit)
            gen.mark(exit)

            gen.load(find("old_branch"))
            gen.push(-1)
            gen.ifICmp(NE, trueExit)

            gen.load(find("old_branch"))
            gen.load(find("new_branch"))
            gen.ifICmp(EQ, trueExit)

            gen.store(find("jump_back")) {
                gen.push(1)
            }

            gen.load(find("old_branch"))
            gen.visitTableSwitchInsn(0, children.size() - 1, jumpBack, destructLabels2.toArray(new Label[destructLabels2.size()]))
            children.eachWithIndex { def child, index ->
                gen.mark(destructLabels2[index])
                gen.goTo(destructLabels[index])
            }
            gen.mark(jumpBack)

            gen.store(find("old_branch")) {
                gen.load(find("new_branch"))
            }

            gen.store(find("new_branch")) {
                gen.push(-1)
            }

            gen.mark(trueExit)

            gen.loadLocal(local)
        }
    }
}
