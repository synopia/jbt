package org.synopia.behavior

import org.synopia.behavior.nodes.ActionNode
import org.synopia.behavior.nodes.DecoratorNode
import org.synopia.behavior.nodes.DynamicSelectorNode
import org.synopia.behavior.nodes.FailNode
import org.synopia.behavior.nodes.ParallelNode
import org.synopia.behavior.nodes.RunNode
import org.synopia.behavior.nodes.SelectorNode
import org.synopia.behavior.nodes.SequenceNode
import org.synopia.behavior.nodes.SucceedNode

/**
 * Created by synopia on 12.07.2014.
 */
class BTreeBuilder extends BuilderSupport {
    static int id = 1;
    @Override
    protected void setParent(Object parent, Object child) {
        parent.addChild(child);
        child.parent = parent
    }

    protected static BehaviorNode create(String name) {
        switch (name) {
            case 'tree': return new BehaviorTreeNode()
            case 'sequence': return new SequenceNode()
            case 'selector': return new SelectorNode()
            case 'parallel': return new ParallelNode()
            case 'action': return new ActionNode([:])
            case 'fail': return new FailNode()
            case 'succeed': return new SucceedNode()
            case 'running': return new RunNode()
            case 'decorator': return new DecoratorNode()
            case 'dynamic_selector': return new DynamicSelectorNode()

            case 'act_print': return new ActionNode([command: "print", construct: false, destruct: false])
            case 'act_count_down': return new ActionNode([command: "count_down", construct: true, destruct: false, locals: ["count_down": 0]])
            case 'delay': return new ActionNode([command: "delay", construct: true, destruct: false, locals: ["delay": 1000]])
            default:
                throw new IllegalArgumentException("Unknown node type: $name")
        }
    }

    protected static BehaviorNode assignId(BehaviorNode node) {
        node.id = id;
        id++;
        node
    }

    @Override
    protected Object createNode(Object name) {
        return createNode(name, null, null);
    }

    @Override
    protected Object createNode(Object name, Object value) {
        return createNode(name, null, value);
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        return createNode(name, attributes, null);
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        def node = new DebugDecorator(assignId(create((String) name)));

        if (attributes) {
            node.setAttributes(attributes)
        } else {
            node.setAttributes([:])
        }
        return node;
    }
}
