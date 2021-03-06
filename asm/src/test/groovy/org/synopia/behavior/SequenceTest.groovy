package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 13.07.2014.
 */

class SequenceTest extends BehaviorTreeTest {

    @Test
    void testRunningSequences() {
        testTree(4 * 3, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2, 3, 4, 5], [1, 4, 5], [1, 4, 5]]) {
            builder.tree {
                sequence(id: 1) {
                    sequence(id: 2) {
                        succeed(id: 3)
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
        testTree(8, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                sequence(id: 1) {
                    running(id: 2)
                    succeed(id: 3)
                }
            }
        }
    }

    @Test
    void testRunningFail() {
        testTree(8, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING],
                [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                sequence(id: 1) {
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
                sequence(id: 1) {
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
                sequence(id: 1) {
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
                sequence(id: 1) {
                    fail(id: 2)
                }
            }
        }
    }

    @Test
    void testSucceed() {
        testTree(8, [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                sequence(id: 1) {
                    succeed(id: 2)
                }
            }
        }
    }

    @Test
    void testRunning() {
        testTree(8, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING], [[1, 2], [1, 2], [1, 2]]) {
            builder.tree {
                sequence(id: 1) {
                    running(id: 2)
                }
            }
        }
    }

}
