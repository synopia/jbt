package org.synopia.behavior.nodes

import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorState

/**
 * Created by synopia on 13.07.2014.
 */
class RunNode extends BaseBehaviorNode {
    @Override
    Map defaults() {
        deepMerge(super.defaults(), [command: "running"])
    }

    @Override
    void assembleExecute(BTMethodGenerator gen) {
        gen.push(BehaviorState.RUNNING.ordinal());
    }
}
