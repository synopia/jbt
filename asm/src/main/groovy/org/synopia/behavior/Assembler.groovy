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
    String className

    Assembler(String className) {
        this.className = className
        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        cg = new BTClassGenerator(cw)
        cg.visit(V1_5, ACC_PUBLIC, className.replaceAll("\\.", "/"), null, Type.getType(CompiledBehaviorTree.class).className.replaceAll("\\.", '/'), null)
    }

    public static BehaviorNode buildFromScript(InputStream stream) {
        Binding binding = new Binding();
        binding.setProperty("builder", new BTreeBuilder());
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding);
        return (BehaviorNode) shell.evaluate(new InputStreamReader(stream));
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
        Class c = loader.defineClass(className, cw.toByteArray());
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
