package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.UnsafeMemory

/**
 * Created by synopia on 14.07.2014.
 */
class DynamicSelectorTest extends BehaviorTreeTest {
    @Test
    void testRecheck() {
        runTree([BehaviorState.RUNNING, BehaviorState.SUCCESS, BehaviorState.RUNNING],
                [(BehaviorAction.EXECUTE): [3: [BehaviorState.FAILURE, BehaviorState.SUCCESS, BehaviorState.FAILURE]]]
        ) {
            builder.tree {
                dynamic_selector(id: 1) {
                    sequence(id: 2) {
                        action(id: 3, command: "xyz")
                    }
                    sequence(id: 4) {
                        running(id: 5)
                    }
                }
            }
        }
    }

    @Test
    void testRunningSelectors() {
        testTree(4 * (1 + 2 + 2 + 1 + 1), [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2, 3, 4, 5], [1, 2, 3, 4, 5], [1, 2, 3, 4, 5]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    sequence(id: 2) {
                        fail(id: 3)
                    }
                    sequence(id: 4) {
                        running(id: 5)
                    }
                }
            }
        }
    }

    @Test
    void testRunningSuccess() {
        testTree(4 * (1 + 3 + 2), [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    running(id: 2)
                    succeed(id: 3)
                }
            }
        }
    }

    @Test
    void testRunningFail() {
        testTree(4 * (1 + 3 + 2), [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    running(id: 2)
                    fail(id: 3)
                }
            }
        }
    }

    @Test
    void testSuccessFail() {
        testTree(4 * (1 + 3 + 2), [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    succeed(id: 2)
                    fail(id: 3)
                }
            }
        }
    }

    @Test
    void testFailSuccess() {
        testTree(4 * (1 + 3 + 2), [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS],
                [[1, 2, 3], [1, 2, 3], [1, 2, 3]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    fail(id: 2)
                    succeed(id: 3)
                }
            }
        }
    }

    @Test
    void testFail() {
        testTree(4 * (1 + 3 + 1), [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    fail(id: 2)
                }
            }
        }
    }

    @Test
    void testSucceed() {
        testTree(4 * (1 + 3 + 1), [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    succeed(id: 2)
                }
            }
        }
    }

    @Test
    void testRunning() {
        testTree(4 * (1 + 3 + 1), [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                dynamic_selector(id: 1) {
                    running(id: 2)
                }
            }
        }
    }
}
