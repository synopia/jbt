package org.synopia.behavior.tree;

/**
 * Created by synopia on 15.07.2014.
 */
public interface DebugCallback {
    void push(int id, BehaviorAction action, String command, int memoryOffset);

    void pop(int returnValue);

    public static DebugCallback NULL = new DebugCallback() {
        @Override
        public void push(int id, BehaviorAction action, String command, int memoryOffset) {
        }

        @Override
        public void pop(int returnValue) {
        }
    };
}
