package org.synopia.behavior

import org.junit.Assert
import org.junit.Test
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.Callback
import org.synopia.behavior.tree.DebugCallback

/**
 * Created by synopia on 19.10.2014.
 */
class DebugTest {
    def builder = new BTreeBuilder();

    def run(Map expected, Closure f) {
        Stack<Integer> stack = new LinkedList<>()
        Map<Integer, Integer> result = [:]
        def assembler = new Assembler("pkg.Foo")
        def tree = assembler.assemble(f())
        def sample = assembler.createInstance(new Callback() {
            @Override
            BehaviorState execute(int id, BehaviorAction action, String command) {
                return null
            }
        }, new DebugCallback() {
            @Override
            void push(int id, BehaviorAction action, String command, int memoryOffset) {
                stack.push(id)
            }

            @Override
            void pop(int returnValue) {
                def id = stack.pop()
                result.put(id, returnValue)
            }
        })
        sample.run()

        Assert.assertEquals(expected, result)
    }

    @Test
    public void testIt() {
        run([99: BehaviorState.RUNNING.ordinal()]) {
            builder.tree() {
                running(id: 99)
            }
        }
    }
}
