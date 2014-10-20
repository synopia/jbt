package org.synopia.behavior

import groovy.json.internal.Byt
import org.junit.Assert
import org.junit.Test
import org.synopia.behavior.commands.Command
import org.synopia.behavior.commands.Dispatcher
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.Context

/**
 * Created by synopia on 14.07.2014.
 */
class LocalsTest extends BehaviorTreeTest {
    static SAMPLES = [
            [b1: (byte) 0],
            [b2: Byte.MAX_VALUE],
            [b3: Byte.MIN_VALUE],
            [s1: (short) 0],
            [s2: Short.MIN_VALUE],
            [s3: Short.MAX_VALUE],
            [i1: 0],
            [i2: Integer.MIN_VALUE],
            [i3: Integer.MAX_VALUE],
            [l1: 0L],
            [l2: Long.MIN_VALUE],
            [l3: Long.MAX_VALUE],
            [f1: 0f],
            [f2: Float.MIN_VALUE],
            [f3: Float.MAX_VALUE],
            [d1: 0d],
            [d2: Double.MIN_VALUE],
            [d3: Double.MAX_VALUE],
            [st: "XYZ"],
    ]

    @Test
    void testLocalsConstructed() {
        def tree = builder.tree {
            action(id: 1, command: "xyz", locals: SAMPLES)
        }

        def dispatcher = runAction(tree, "xyz", { context ->
            SAMPLES.each { hash ->
                hash.each { name, value ->
                    Assert.assertTrue(value == readNext(context, value.class));
                }
            }
        }, { context ->
            return BehaviorState.RUNNING
        }, { context ->
        });
        dispatcher.run();
    }

    def readNext(Context context, Class<?> type) {
        switch (type) {
            case Byte.class: return context.readByte()
            case Short.class: return context.readShort()
            case Integer.class: return context.readInt()
            case Long.class: return context.readLong()
            case Float.class: return context.readFloat()
            case Double.class: return context.readDouble()
            case String.class: return context.readString()
        }
    }

    def runAction(def tree, String cmd, Closure construct, Closure execute, Closure destruct) {
        def assembler = new Assembler("pkg.Foo")
        tree.assemble(assembler.cg)

        def dispatcher = new Dispatcher(assembler.createInstance())
        dispatcher.register(new Command() {
            @Override
            void construct(Context context) {
                construct(context)
            }

            @Override
            BehaviorState execute(Context context) {
                return execute(context)
            }

            @Override
            void destruct(Context context) {
                destruct(context)
            }

            @Override
            BehaviorState prune(Context context) {
                return null
            }

            @Override
            BehaviorState modify(Context context) {
                return null
            }

            @Override
            String name() {
                return cmd;
            }
        });
        dispatcher
    }

    @Test
    void testLocalsHoldChanges() {
        def tree = builder.tree {
            action(id: 1, command: "xyz", locals: [[x: 43], [y: 23456]])
        }

        def assembler = new Assembler("pkg.Foo")
        tree.assemble(assembler.cg)

        def dispatcher = new Dispatcher(assembler.createInstance())
        def count = 42
        dispatcher.register(new Command() {
            @Override
            String name() {
                return "xyz"
            }

            @Override
            void construct(Context context) {

            }

            @Override
            BehaviorState execute(Context context) {
                def old = context.memory.getInt(context.memoryOffset)
                Assert.assertEquals(old - count, 1)
                context.memory.setInt(context.memoryOffset, count)
                count--
                return BehaviorState.RUNNING
            }

            @Override
            void destruct(Context context) {

            }

            @Override
            BehaviorState prune(Context context) {
                return null
            }

            @Override
            BehaviorState modify(Context context) {
                return null
            }
        })
        dispatcher.run();
        dispatcher.run();
        dispatcher.run();
        dispatcher.run();
        dispatcher.run();
    }

}
