package org.synopia.behavior
/**
 * Created by synopia on 12.07.2014.
 */
interface BehaviorNode<N> {
    void insertChild(int index, N child);

    void replaceChild(int index, N child);

    N removeChild(int index);

    N getChild(int index);

    int getChildrenCount();

    int getMaxChildren();

    /**
     * Visitor pattern to visit the complete tree.
     */
    public <T> T visit(T item, Visitor<T> visitor);
}
