package org.synopia.behavior.generators

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.Method

import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.ASM5

/**
 * Created by synopia on 17.07.2014.
 */
class BTClassGenerator extends ClassVisitor {
    BTClassGenerator(ClassVisitor var2) {
        super(ASM5, var2)
    }

    BTMethodGenerator generateMethod(String method, Closure f) {
        Method m = Method.getMethod(method)
        MethodVisitor v = visitMethod(ACC_PUBLIC, m.name, m.descriptor, null, null)
        def generator = new BTMethodGenerator(ASM5, v, ACC_PUBLIC, m.name, m.descriptor)

        f(generator)

        generator.endMethod()
    }
}
