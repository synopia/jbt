package org.synopia.behavior

import org.junit.Assert
import org.junit.Before
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.Callback
import org.synopia.behavior.tree.CompiledBehaviorTree
import org.synopia.behavior.tree.Context
import org.synopia.behavior.tree.DebugCallback
import org.synopia.behavior.tree.UnsafeMemory

/**
 * Created by synopia on 14.07.2014.
 */
class BehaviorTreeTest {
    protected BTreeBuilder builder
    CompiledBehaviorTree sample;

    @Before
    void setup() {
        builder = new BTreeBuilder();
    }

    void runTree(List states, Map input, Closure f) {
        def actionStacks = [:]
        BehaviorAction.values().each {
            if (input[it]) {
                def cmdStacks = [:]
                actionStacks[it] = cmdStacks
                input[it].each { key, value ->
                    cmdStacks[key] = new Stack()
                    cmdStacks[key] += value.reverse()
                }
            }
        }
        def assembler = new Assembler()
        def tree = assembler.assemble(f())
        def sample = assembler.createInstance(new Callback() {
            @Override
            BehaviorState execute(int id, BehaviorAction action, String command) {
                if (actionStacks[action]) {
                    if (actionStacks[action][id] != null) {
                        return actionStacks[action][id].pop();
                    }
                }
                return BehaviorState.UNDEFINED;
            }
        }, new DebugCallback() {
            @Override
            void push(int id, BehaviorAction action, String command, int memoryOffset) {

            }

            @Override
            void pop(int returnValue) {

            }
        })

        Assert.assertEquals(states, states.collect {
            sample.run()
            BehaviorState.values()[sample.memory.getInt(0)]
        })
        def sum = 0
        actionStacks.each { key, value ->
            value.each { k, v ->
                sum += v.size()
            }
        }
        Assert.assertEquals(0, sum)
    }

    void testTree(int expectedMem, List states, List execute, Closure f = null) {
        def assembler = new Assembler()
        def tree = assembler.assemble(f())
        def actualExecute = []
        sample = assembler.createInstance(new Callback() {
            @Override
            BehaviorState execute(int id, BehaviorAction action, String command) {
                if (f) {
                    return f(id, action, command, BehaviorTreeTest.this.sample.memory, -1)
                }
                return BehaviorState.UNDEFINED;
            }
        }, new DebugCallback() {
            @Override
            void push(int id, BehaviorAction action, String command, int memoryOffset) {
                if (action == BehaviorAction.EXECUTE) {
                    actualExecute << id
                }
            }

            @Override
            void pop(int returnValue) {
            }
        })

        def totalExecute = []
        Assert.assertEquals(states, states.collect {
            sample.run()
            totalExecute << actualExecute
            actualExecute = []
            BehaviorState.values()[sample.memory.getInt(0)]
        })
        Assert.assertEquals(execute, totalExecute)
        Assert.assertEquals(expectedMem, sample.memory.length())
        Assert.assertEquals(expectedMem, tree.size())
    }

    static List formatMemory(UnsafeMemory memory, int offset) {
        List result = []
        def length = memory.length()
        for (int i = 0; i < length; i += 4) {
            int mem = memory.getInt(i + offset)
            result << mem
        }
        result
    }

}
