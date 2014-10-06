package org.synopia.behavior

/**
 * @author synopia
 */
interface Visitor<T> {
    T visit(T item, BehaviorNode node);

}