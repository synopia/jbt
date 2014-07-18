package org.synopia.behavior.generators

import org.objectweb.asm.tree.MethodNode

/**
 * Created by synopia on 17.07.2014.
 */
class MethodTransformer {
    MethodTransformer transformer;

    MethodTransformer(MethodTransformer transformer) {
        this.transformer = transformer
    }

    public void transform(MethodNode node) {
        if (transformer != null) {
            transformer.transform(node)
        }
    }
}
