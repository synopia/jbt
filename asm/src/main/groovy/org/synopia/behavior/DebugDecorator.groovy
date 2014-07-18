package org.synopia.behavior

import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorAction

/**
 * Created by synopia on 17.07.2014.
 */
class DebugDecorator implements BehaviorNode {
    private delegate

    DebugDecorator(delegate) {
        this.delegate = delegate
    }

    void assembleConstruct(BTMethodGenerator gen) {
        gen.pushDebug(this, BehaviorAction.CONSTRUCT)
        delegate.assembleConstruct(gen)
        gen.popDebug(this, BehaviorAction.CONSTRUCT)
    }

    void assembleExecute(BTMethodGenerator gen) {
        gen.pushDebug(this, BehaviorAction.EXECUTE)
        delegate.assembleExecute(gen)
        gen.popDebug(this, BehaviorAction.EXECUTE)
    }

    void assembleDestruct(BTMethodGenerator gen) {
        gen.pushDebug(this, BehaviorAction.DESTRUCT)
        delegate.assembleDestruct(gen)
        gen.popDebug(this, BehaviorAction.DESTRUCT)
    }

    @Override
    Object invokeMethod(String name, args) {
        delegate."$name"(*args)
    }

    def propertyMissing(String name, args) {
        delegate."$name" = args
    }

    def propertyMissing(String name) {
        delegate."$name"
    }

    @Override
    String toString() {
        delegate.name()
    }

}
