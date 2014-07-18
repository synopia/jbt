package org.synopia.behavior.generators

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode

import static org.objectweb.asm.Opcodes.ATHROW
import static org.objectweb.asm.Opcodes.GOTO
import static org.objectweb.asm.Opcodes.IRETURN
import static org.objectweb.asm.Opcodes.RETURN

/**
 * Created by synopia on 17.07.2014.
 */
class OptimizeJumpTransformer extends MethodTransformer {
    OptimizeJumpTransformer(MethodTransformer transformer) {
        super(transformer)
    }

    @Override
    void transform(MethodNode node) {
        def insns = node.instructions
        def it = insns.iterator()
        while (it.hasNext()) {
            AbstractInsnNode ins = it.next()
            if (ins instanceof JumpInsnNode) {
                LabelNode label = ((JumpInsnNode) ins).label;
                AbstractInsnNode target = null;
                while (true) {
                    target = label;
                    while (target != null && target.getOpcode() < 0) {
                        target = target.getNext()
                    }
                    if (target != null && target.getOpcode() == GOTO) {
                        label = ((JumpInsnNode) target).label
                    } else {
                        break;
                    }
                }
                ((JumpInsnNode) ins).label = label
                if (ins.getOpcode() == GOTO && target != null) {
                    int op = target.getOpcode()
                    if ((op >= IRETURN && op <= RETURN) || op == ATHROW) {
                        insns.set(ins, target.clone(null))
                    }
                }
            }
        }
        Object.transform(node)
    }
}
