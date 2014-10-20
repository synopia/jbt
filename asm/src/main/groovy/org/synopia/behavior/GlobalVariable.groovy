package org.synopia.behavior

import org.synopia.behavior.tree.Context
import org.synopia.behavior.tree.UnsafeMemory

/**
 * Created by synopia on 16.07.2014.
 */
class GlobalVariable {
    BehaviorNode node
    String name
    def constructValue
    int memoryOffset
    Class<?> type
    int size

    GlobalVariable(BehaviorNode node, String name, int memoryOffset, def value) {
        this.node = node
        this.name = name
        constructValue = value
        this.type = value.class
        this.memoryOffset = memoryOffset
        this.size = type == String.class ? value.getBytes().length + 2 : UnsafeMemory.sizeOf(type)
    }

    int memoryPosition() {
        memoryOffset + node.memoryOffset
    }

    boolean valid() {
        size > 0
    }

    Object read(Context context) {
        context."read${methodName()}"()
    }

    String methodName() {
        switch (type) {
            case Byte.class: return "Byte"
            case Short.class: return "Short"
            case Integer.class: return "Int"
            case Long.class: return "Long"
            case Float.class: return "Float"
            case Double.class: return "Double"
            case String.class: return "String"
            default: throw new IllegalStateException("Unsupported type $type");
        }
    }

    String typeName() {
        switch (type) {
            case Byte.class: return "byte"
            case Short.class: return "int"
            case Integer.class: return "int"
            case Long.class: return "long"
            case Float.class: return "float"
            case Double.class: return "double"
            case String.class: return "String"
            default: throw new IllegalStateException("Unsupported type $type");
        }
    }
}
