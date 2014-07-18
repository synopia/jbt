package org.synopia.behavior.tree;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static sun.misc.Unsafe.*;

/**
 * Created by synopia on 14.07.2014.
 */
public class UnsafeMemory {
    private ByteBuffer buffer;
    private int length;


    public UnsafeMemory(int length) {
        this.length = length;
        this.buffer = ByteBuffer.wrap(new byte[length]);
    }

    public byte getByte(int index) {
        return buffer.get(index);
    }

    public short getUnsignedByte(int index) {
        return (short) (getByte(index) & 0xff);
    }

    public short getShort(int index) {
        return buffer.getShort(index);
    }

    public int getInt(int index) {
        return buffer.getInt(index);
    }

    public long getLong(int index) {
        return buffer.getLong(index);
    }

    public float getFloat(int index) {
        return buffer.getFloat(index);
    }

    public double getDouble(int index) {
        return buffer.getDouble(index);
    }

    public String getString(int index, Charset charset) {
        return getString(index, length(), charset);
    }

    public String getStringUTF8(int index) {
        return getString(index, length(), Charset.forName("UTF8"));
    }

    public String getString(int index, int length, Charset charset) {
//        return new String(buffer, index + (int) position, length, charset);
        return null;
    }

    public void setByte(int index, byte b) {
        buffer.put(index, b);
    }

    public void setShort(int index, int value) {
        buffer.putShort(index, (short) (value & 0xffff));
    }

    public void setInt(int index, int value) {
        buffer.putInt(index, value);
    }

    public void setLong(int index, long value) {
        buffer.putLong(index, value);
    }

    public void setFloat(int index, float value) {
        buffer.putFloat(index, value);
    }

    public void setDouble(int index, double value) {
        buffer.putDouble(index, value);
    }

    public void setString(int index, String value) {
    }

    public int length() {
        return length;
    }

    public static int sizeOf(Class<?> type) {
        if (type == Byte.class || type == Byte.TYPE) {
            return 1;
        }
        if (type == Short.class || type == Short.TYPE) {
            return 2;
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return 4;
        }
        if (type == Long.class || type == Long.TYPE) {
            return 8;
        }
        if (type == Float.class || type == Float.TYPE) {
            return 4;
        }
        if (type == Double.class || type == Double.TYPE) {
            return 8;
        }
        return -1;
    }
}
