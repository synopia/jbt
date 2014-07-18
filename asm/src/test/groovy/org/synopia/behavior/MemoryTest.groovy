package org.synopia.behavior

import org.junit.Assert
import org.junit.Test

/**
 * Created by synopia on 14.07.2014.
 */
class MemoryTest {

/*
    int id = 0
    private void allocate1(Scope scope) {
        scope.allocate("number", 0)
        id++
    }
    private void allocateAll(Scope scope) {
        scope.allocate("b", (byte)0)
        scope.allocate("s", (short)0)
        scope.allocate("i", 0)
        scope.allocate("l", 0L)
        scope.allocate("f", 1.1f)
        scope.allocate("d", 1.2d)
        id++
    }
    @Test
    void test() {
        Memory memory = new Memory()
        memory.createScope("0")
        def scope = memory.createScope("1")
        allocate1(scope)
        Assert.assertTrue(scope.has("number"))
        Assert.assertFalse(scope.has("not_number"))
        Assert.assertEquals(0, scope.find("number").memoryPosition)
        scope.close(false)
        Assert.assertFalse(memory.has("number"))

        scope = memory.createScope("1")
        Assert.assertTrue(scope.has("number"))
        Assert.assertFalse(scope.has("not_number"))
        Assert.assertEquals(0, scope.find("number").memoryPosition)
        scope.close(false)
    }

    @Test
    void testAll() {
        Memory memory = new Memory()
        memory.createScope("0")
        def scope = memory.createScope("1")
        allocateAll(scope)
        Assert.assertEquals(0, scope.find("b").memoryPosition)
        Assert.assertEquals(1, scope.find("s").memoryPosition)
        Assert.assertEquals(1+2, scope.find("i").memoryPosition)
        Assert.assertEquals(1+2+4, scope.find("l").memoryPosition)
        Assert.assertEquals(1+2+4+8, scope.find("f").memoryPosition)
        Assert.assertEquals(1+2+4+8+4, scope.find("d").memoryPosition)

        def scope2 = memory.createScope("2")
        allocate1(scope2)
        Assert.assertEquals(1+2+4+8+4+8, scope2.find("number").memoryPosition)
        scope2.close(false)
        scope.close(false)

        scope = memory.createScope("1")
        Assert.assertEquals(0, scope.find("b").memoryPosition)
        Assert.assertEquals(1, scope.find("s").memoryPosition)
        Assert.assertEquals(1+2, scope.find("i").memoryPosition)
        Assert.assertEquals(1+2+4, scope.find("l").memoryPosition)
        Assert.assertEquals(1+2+4+8, scope.find("f").memoryPosition)
        Assert.assertEquals(1+2+4+8+4, scope.find("d").memoryPosition)

        scope2 = memory.createScope("2")
        Assert.assertEquals(1+2+4+8+4+8, scope2.find("number").memoryPosition)
        scope2.close(false)
        scope.close(false)
    }

    @Test
    void testScopes() {
        Memory memory = new Memory()
        memory.createScope("0")
        def scope = memory.createScope("1")
        allocate1(scope)
        Assert.assertEquals(0, scope.find("number").memoryPosition)

        def scope2 = memory.createScope("2")
        allocate1(scope2)
        Assert.assertEquals(4, scope2.find("number").memoryPosition)
        scope2.close(false)

        def scope3 = memory.createScope("3")
        allocate1(scope3)
        Assert.assertEquals(8, scope3.find("number").memoryPosition)
        scope3.close(true)

        def scope4 = memory.createScope("4")
        allocate1(scope4)
        Assert.assertEquals(8, scope4.find("number").memoryPosition)
        scope4.close(false)
        scope.close(true)
        Assert.assertEquals(0, memory.currentSize)
        Assert.assertEquals(12, memory.max)
    }
*/

}
