package org.synopia.behavior.nodes

import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.Visitor

/**
 * Created by synopia on 12.07.2014.
 */
class CompositeNode extends BaseBehaviorNode {
    protected List<BehaviorNode> children = []

    @Override
    String toString() {
        return super.toString();
    }

    @Override
    public int getMaxChildren() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void insertChild(int index, BehaviorNode child) {
        if (index == -1) {
            children.add(child);
        } else {
            children.add(index, child);
        }
    }

    @Override
    public void replaceChild(int index, BehaviorNode child) {
        if (index == children.size()) {
            children.add(null);
        }
        children.set(index, child);
    }

    @Override
    public BehaviorNode removeChild(int index) {
        return children.remove(index);
    }

    @Override
    public BehaviorNode getChild(int index) {
        return children.get(index);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @Override
    def <T> T visit(T item, Visitor<T> visitor) {
        T childItem = super.visit(item, visitor);
        for (BehaviorNode child : children) {
            child.visit(childItem, visitor);
        }
        return childItem;
    }
}
