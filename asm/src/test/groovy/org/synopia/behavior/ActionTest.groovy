package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 14.07.2014.
 */
class ActionTest extends BehaviorTreeTest {
    @Test
    void testExecuteCallback() {
        def states = [BehaviorState.RUNNING, BehaviorState.SUCCESS, BehaviorState.FAILURE]

        runTree(states, [(BehaviorAction.EXECUTE): [1: states]]) {
            builder.tree {
                action(id: 1, command: "xyz")
            }
        }
    }

    @Test
    void testExecuteCallbackOff() {
        def states = [BehaviorState.UNDEFINED, BehaviorState.UNDEFINED, BehaviorState.UNDEFINED]

        runTree(states, [(BehaviorAction.EXECUTE): [1: []]]) {
            builder.tree {
                action(id: 1, command: "xyz", execute: false)
            }
        }
    }

    @Test
    void testConstructCallbackOff() {
        def states = [BehaviorState.UNDEFINED, BehaviorState.UNDEFINED, BehaviorState.UNDEFINED]

        runTree(states, [(BehaviorAction.CONSTRUCT): [1: []]]) {
            builder.tree {
                action(id: 1, command: "xyz", construct: false)
            }
        }
    }

    @Test
    void testDestructCallbackOff() {
        def states = [BehaviorState.UNDEFINED, BehaviorState.UNDEFINED, BehaviorState.UNDEFINED]

        runTree(states, [(BehaviorAction.DESTRUCT): [1: []]]) {
            builder.tree {
                action(id: 1, command: "xyz", destruct: false)
            }
        }
    }
}
