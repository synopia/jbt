package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 15.07.2014.
 */
class DecoratorTest extends BehaviorTreeTest {

    @Test
    void testDecoratedActionModify() {
        runTree([BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.RUNNING], [
                (BehaviorAction.EXECUTE): [2: [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS, BehaviorState.RUNNING, BehaviorState.RUNNING]],
                (BehaviorAction.MODIFY) : [1: [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.RUNNING]],
                (BehaviorAction.PRUNE)  : [1: []]
        ]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: false, modify: true, construct: false, destruct: false) {
                    action(id: 2, command: "print", arg: "x")
                }
            }
        }
    }

    @Test
    void testDecoratedActionPrune() {
        runTree([BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS], [
                (BehaviorAction.EXECUTE): [2: [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS]],
                (BehaviorAction.PRUNE)  : [1: [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS]],
                (BehaviorAction.MODIFY) : [1: []]
        ]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: true, modify: false, construct: false, destruct: false) {
                    action(id: 2, command: "print", arg: "x")
                }
            }
        }
    }

    @Test
    void testDecoratedAction() {
        runTree([BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS], [
                (BehaviorAction.EXECUTE): [2: [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS]],
                (BehaviorAction.PRUNE)  : [1: []],
                (BehaviorAction.MODIFY) : [1: []]
        ]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: false, modify: false, construct: false, destruct: false) {
                    action(id: 2, command: "print", arg: "x")
                }
            }
        }
    }

    @Test
    void testConstructDestructOnlySucceed() {
        def states = [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS]

        runTree(states, [(BehaviorAction.CONSTRUCT): [1: states], (BehaviorAction.DESTRUCT): [1: states]]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: false, modify: false) {
                    succeed(id: 2)
                }
            }
        }
    }

    @Test
    void testConstructDestructOnlyFail() {
        def states = [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE]

        runTree(states, [(BehaviorAction.CONSTRUCT): [1: states], (BehaviorAction.DESTRUCT): [1: states]]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: false, modify: false) {
                    fail(id: 2)
                }
            }
        }
    }

    @Test
    void testConstructDestructOnlyRunning() {
        def states = [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING]

        runTree(states, [(BehaviorAction.CONSTRUCT): [1: [BehaviorState.RUNNING]], (BehaviorAction.DESTRUCT): [:]]) {
            builder.tree {
                decorator(id: 1, command: "abc", prune: false, modify: false) {
                    running(id: 2)
                }
            }
        }
    }

}
