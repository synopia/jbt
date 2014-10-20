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

    @Override
    void insertChild(int index, Object child) {
        delegate.insertChild(index, child)
    }

    @Override
    void replaceChild(int index, Object child) {
        delegate.replaceChild(index, child)
    }

    @Override
    Object removeChild(int index) {
        return delegate.removeChild(index)
    }

    @Override
    Object getChild(int index) {
        return delegate.getChild(index)
    }

    @Override
    int getChildrenCount() {
        return delegate.getChildrenCount()
    }

    @Override
    int getMaxChildren() {
        return delegate.getMaxChildren()
    }

    @Override
    public <T> T visit(T item, Visitor<T> visitor) {
        return delegate.visit(item, visitor);
    }

    @Override
    int getId() {
        delegate.getId()
    }

    @Override
    void setId(int id) {
        delegate.setId(id)
    }
}
