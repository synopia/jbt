package org.synopia.behavior

import org.junit.Test
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 14.07.2014.
 */
class TestSimpleNodes extends BehaviorTreeTest {
    @Test
    void testFail() {
        testTree(4, [BehaviorState.FAILURE, BehaviorState.FAILURE, BehaviorState.FAILURE], [[1], [1], [1]]) {
            builder.tree {
                fail(id: 1)
            }
        }
    }

    @Test
    void testSucceed() {
        testTree(4, [BehaviorState.SUCCESS, BehaviorState.SUCCESS, BehaviorState.SUCCESS], [[1], [1], [1]]) {
            builder.tree {
                succeed(id: 1)
            }
        }
    }

    @Test
    void testRunning() {
        testTree(4, [BehaviorState.RUNNING, BehaviorState.RUNNING, BehaviorState.RUNNING], [[1], [1], [1]]) {
            builder.tree {
                running(id: 1)
            }
        }
    }

}
