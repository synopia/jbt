package org.synopia.behavior;

import org.junit.Assert;
import org.junit.Test;
import org.synopia.behavior.tree.UnsafeMemory;

/**
 * Created by synopia on 14.07.2014.
 */
public class UnsafeMemoryTest {

    @Test
    public void testGetterSetter() {
        UnsafeMemory memory = new UnsafeMemory(100);
        Object samples[] = new Object[]{
                (byte) 0, (byte) 1, (byte) -127, (byte) 127,
                (short) 0, (short) 255,
                0, -2550000,
                Long.MAX_VALUE,
                Float.MAX_VALUE,
                Double.MAX_VALUE,
        };
        int pos = 0;
        for (Object sample : samples) {
            if (sample instanceof Byte) {
                memory.setByte(pos, (byte) sample);
                pos++;
            }
            if (sample instanceof Short) {
                memory.setShort(pos, (short) sample);
                pos += 2;
            }
            if (sample instanceof Integer) {
                memory.setInt(pos, (int) sample);
                pos += 4;
            }
            if (sample instanceof Long) {
                memory.setLong(pos, (long) sample);
                pos += 8;
            }
            if (sample instanceof Float) {
                memory.setFloat(pos, (float) sample);
                pos += 4;
            }
            if (sample instanceof Double) {
                memory.setDouble(pos, (double) sample);
                pos += 8;
            }
        }

        pos = 0;
        for (Object sample : samples) {
            if (sample instanceof Byte) {
                Assert.assertEquals(sample, memory.getByte(pos));
                pos++;
            }
            if (sample instanceof Short) {
                Assert.assertEquals(sample, memory.getShort(pos));
                pos += 2;
            }
            if (sample instanceof Integer) {
                Assert.assertEquals(sample, memory.getInt(pos));
                pos += 4;
            }
            if (sample instanceof Long) {
                Assert.assertEquals(sample, memory.getLong(pos));
                pos += 8;
            }
            if (sample instanceof Float) {
                Assert.assertEquals(sample, memory.getFloat(pos));
                pos += 4;
            }
            if (sample instanceof Double) {
                Assert.assertEquals(sample, memory.getDouble(pos));
                pos += 8;
            }

        }
    }
}
