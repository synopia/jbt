package org.synopia.behavior.tree;

/**
 * Created by synopia on 13.07.2014.
 */
public abstract class CompiledBehaviorTree {
    public Callback callback;
    public DebugCallback debugCallback;
    public UnsafeMemory memory;
    public Context context;

    protected CompiledBehaviorTree(int size) {
        memory = new UnsafeMemory(size);
        context = new Context(this);
    }

    public abstract BehaviorState run();

}
