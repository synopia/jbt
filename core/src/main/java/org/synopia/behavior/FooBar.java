package org.synopia.behavior;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.CompiledBehaviorTree;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by synopia on 12.07.2014.
 */
public class FooBar extends CompiledBehaviorTree {
    public FooBar() {
        super(123);
    }

    @Override
    public BehaviorState run() {
        return BehaviorState.values()[memory.getInt(0)];
    }

    int exec2(int v) {
        memory.setLong(0, 1L);
        memory.setLong(0, 5L);
        memory.setLong(0, 6L);
        memory.setLong(0, 127L);
        memory.setLong(0, 128L);
        memory.setDouble(0, 1.33d);
        return 0;
    }

    public static void main(String[] args) throws IOException {
        ClassReader reader = new ClassReader(FooBar.class.getName());
        reader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(
                System.out)), 0);
    }
}
