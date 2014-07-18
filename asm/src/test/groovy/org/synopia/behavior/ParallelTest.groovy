package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 14.07.2014.
 */
class ParallelTest extends BehaviorTreeTest {
    @Test
    void testRunning2() {
        runTree([BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS],
                [(BehaviorAction.EXECUTE): [
                        1: [BehaviorState.RUNNING, BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS],
                        2: [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS, BehaviorState.SUCCESS],
                        3: [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.SUCCESS]
                ]]) {
            builder.tree {
                parallel {
                    sequence {
                        action(id: 1, command: "foo")
                    }
                    sequence {
                        action(id: 2, command: "bar")
                    }
                    sequence {
                        action(id: 3, command: "baz")
                    }
                }
            }
        }
    }

    @Test
    void testRunningSuccess() {
        testTree(8, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2, 3], [1, 2, 3], [1, 2, 3]]) {
            builder.tree {
                parallel(id: 1) {
                    running(id: 2)
                    succeed(id: 3)
                }
            }
        }
    }

    @Test
    void testRunningFail() {
        testTree(8, [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE],
                [[1, 2, 3], [1, 2, 3], [1, 2, 3]]) {
            builder.tree {
                parallel(id: 1) {
                    running(id: 2)
                    fail(id: 3)
                }
            }
        }
    }

    @Test
    void testSuccessFail() {
        testTree(8, [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE],
                [[1, 2, 3], [1, 2, 3], [1, 2, 3]]) {
            builder.tree {
                parallel(id: 1) {
                    succeed(id: 2)
                    fail(id: 3)
                }
            }
        }
    }

    @Test
    void testFailSuccess() {
        testTree(8, [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                parallel(id: 1) {
                    fail(id: 2)
                    succeed(id: 3)
                }
            }
        }
    }

    @Test
    void testFail() {
        testTree(8, [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                parallel(id: 1) {
                    fail(id: 2)
                }
            }
        }
    }

    @Test
    void testSucceed() {
        testTree(8, [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                parallel(id: 1) {
                    succeed(id: 2)
                }
            }
        }
    }

    @Test
    void testRunning() {
        testTree(8, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                parallel(id: 1) {
                    running(id: 2)
                }
            }
        }
    }

    @Test
    void testEmpty() {
        testTree(8, [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS], [[1], [1], [1]]) {
            builder.tree {
                parallel(id: 1) {
                }
            }
        }
    }

}
