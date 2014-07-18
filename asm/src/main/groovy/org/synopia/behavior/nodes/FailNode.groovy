package org.synopia.behavior.nodes

import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 13.07.2014.
 */
class FailNode extends BaseBehaviorNode {
    @Override
    Map defaults() {
        deepMerge(super.defaults(), [command: "fail"])
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        gen.push(BehaviorState.FAILURE.ordinal())
    }
}
