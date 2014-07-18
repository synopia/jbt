package org.synopia.behavior

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Type
import org.synopia.behavior.generators.BTClassGenerator
import org.synopia.behavior.tree.Callback
import org.synopia.behavior.tree.CompiledBehaviorTree
import org.synopia.behavior.tree.DebugCallback

import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.V1_5

/**
 * Created by synopia on 12.07.2014.
 */
class Assembler {
    ClassWriter cw
    BTClassGenerator cg

    Assembler() {
        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        cg = new BTClassGenerator(cw)
        cg.visit(V1_5, ACC_PUBLIC, "pkg/Foo", null, Type.getType(CompiledBehaviorTree.class).className.replaceAll("\\.", '/'), null)
    }

    def assemble(BehaviorNode tree) {
        tree.assemble(cg)
        return tree
    }

    byte[] getBytecode() {
        cw.toByteArray()
    }

    CompiledBehaviorTree createInstance() {
        def loader = new MyClassLoader(this.class.classLoader)
        Class c = loader.defineClass("pkg.Foo", cw.toByteArray());
        c.newInstance()
    }

    def createInstance(Callback callback, DebugCallback dbgCallback = null) {
        def instance = createInstance()

        instance.callback = callback
        instance.debugCallback = dbgCallback
        return instance
    }

    public class MyClassLoader extends ClassLoader {
        MyClassLoader(ClassLoader var1) {
            super(var1)
        }

        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length)
        }
    }

}
