package org.synopia.behavior.generators

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method
import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.GlobalVariable
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.Callback
import org.synopia.behavior.tree.Context
import org.synopia.behavior.tree.DebugCallback
import org.synopia.behavior.tree.UnsafeMemory

/**
 * Created by synopia on 17.07.2014.
 */
class BTMethodGenerator extends GeneratorAdapter {
    int localCounter = 0
    int maxLocalCounter = localCounter

    BTMethodGenerator(int var1, MethodVisitor var2, int var3, String var4, String var5) {
        super(var1, var2, var3, var4, var5)
    }

    void invokeCallback(BehaviorNode node, BehaviorAction action, int returnLocal, String arg = null) {
        Method m = Method.getMethod("org.synopia.behavior.tree.BehaviorState execute(int,org.synopia.behavior.tree.BehaviorAction,java.lang.String) ")
        Method ord = Method.getMethod("int ordinal()")
        loadThis()
        getField(Type.getType("pkg/Foo"), "context", Type.getType(Context))
        push(node.memoryOffset)
        putField(Type.getType(Context), "memoryOffset", Type.INT_TYPE)
        if (arg != null) {
            loadThis()
            getField(Type.getType("pkg/Foo"), "context", Type.getType(Context))
            push(arg)
            putField(Type.getType(Context), "arg", Type.getType(String))
        }

        loadThis()
        getField(Type.getType("pkg/Foo"), "callback", Type.getType(Callback))
        push(node.id)
        getStatic(Type.getType(BehaviorAction), action.toString(), Type.getType(BehaviorAction))
        push(node.command)
        invokeInterface(Type.getType(Callback), m)
        invokeVirtual(Type.getType(BehaviorState), ord)
    }


    void store(GlobalVariable var, Closure f) {
        Method m = Method.getMethod("void set${var.methodName()} (int, ${var.typeName()})")
        def index = var.memoryPosition()
        loadThis()
        getField(Type.getType("pkg/Foo"), "memory", Type.getType(UnsafeMemory))
        push(index)
        f()
        invokeVirtual(Type.getType(UnsafeMemory.class), m)
    }

    void load(GlobalVariable var) {
        Method m = Method.getMethod("${var.typeName()} get${var.methodName()}(int)")
        def index = var.memoryPosition()
        loadThis()
        getField(Type.getType("pkg/Foo"), "memory", Type.getType(UnsafeMemory))
        push(index)
        invokeVirtual(Type.getType(UnsafeMemory.class), m)
    }

    void reserveLocal(Closure f) {
        def local = newLocal(Type.INT_TYPE)
        localCounter++
        maxLocalCounter++
        f(local)
        localCounter--
    }

    void pushDebug(BehaviorNode node, BehaviorAction action) {
        if (node.debug(action)) {
            loadThis();
            getField(Type.getType("pkg/Foo"), "debugCallback", Type.getType(DebugCallback.class))
            push(node.id)
            getStatic(Type.getType(BehaviorAction.class), action.toString(), Type.getType(BehaviorAction.class))
            push(node.command);
            push(node.memoryOffset)
            Method m = Method.getMethod("void push(int, org.synopia.behavior.tree.BehaviorAction, String, int)")
            invokeInterface(Type.getType(DebugCallback.class), m)
        }
    }

    void popDebug(BehaviorNode node, BehaviorAction action) {
        if (node.debug(action)) {
            loadThis()
            getField(Type.getType("pkg/Foo"), "debugCallback", Type.getType(DebugCallback.class))
            push(-1)
            Method m = Method.getMethod("void pop(int)")
            invokeInterface(Type.getType(DebugCallback.class), m)
        }
    }
}
