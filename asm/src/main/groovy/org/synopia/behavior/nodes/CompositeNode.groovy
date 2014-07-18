package org.synopia.behavior.nodes

import org.synopia.behavior.BehaviorNode

/**
 * Created by synopia on 12.07.2014.
 */
class CompositeNode extends BaseBehaviorNode {
    protected List<BehaviorNode> children = []

    public void addChild(BehaviorNode child) {
        children << child;
    }


    @Override
    String toString() {
        return super.toString();
    }
}
