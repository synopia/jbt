package org.synopia.behavior.tree;

import org.synopia.behavior.tree.UnsafeMemory;

/**
 * Created by synopia on 15.07.2014.
 */
public class Context {
    public CompiledBehaviorTree tree;
    public int memoryOffset;
    public int returnValue;
    public String arg;
    public UnsafeMemory memory;
    private int position;

    public Context(CompiledBehaviorTree tree) {
        this.tree = tree;
        this.memory = tree.memory;
    }

    public void rewind() {
        position = 0;
    }

    public byte readByte() {
        byte res = memory.getByte(memoryOffset + position);
        position++;
        return res;
    }

    public short readUnsignedByte() {
        short res = memory.getUnsignedByte(memoryOffset + position);
        position += 1;
        return res;
    }

    public short readShort() {
        short res = memory.getShort(memoryOffset + position);
        position += 2;
        return res;
    }

    public int readInt() {
        int res = memory.getInt(memoryOffset + position);
        position += 4;
        return res;
    }

    public long readLong() {
        long res = memory.getLong(memoryOffset + position);
        position += 8;
        return res;
    }

    public float readFloat() {
        float res = memory.getFloat(memoryOffset + position);
        position += 4;
        return res;
    }

    public double readDouble() {
        double res = memory.getDouble(memoryOffset + position);
        position += 8;
        return res;
    }
}
