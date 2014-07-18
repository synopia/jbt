import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.ASMifierClassVisitor
import org.synopia.behavior.Assembler
import org.synopia.behavior.BTreeBuilder
import org.synopia.behavior.tree.BehaviorAction
import org.synopia.behavior.tree.DebugCallback
import org.synopia.behavior.commands.Dispatcher

/**
 * Created by synopia on 12.07.2014.
 */
def builder = new BTreeBuilder();

//def tree = builder.sequence {
//    action(command: 'print')
//}
def tree = builder.tree {
    sequence(id: 100, debug: [execute: true]) {
        act_count_down(start: 2)
        act_print(arg: 'Start', id: 1)
//        action(command: 'count_down',  id: 2)
        succeed(id: 3,)
        succeed(id: 4)
        running(id: 5)
        fail(id: 6)
//        decorator {
//            action(command: 'print', arg: 'Decorator')
//        }
//        sequence {
//            action(command: 'print', arg: 'Start')
//            action(command: 'count_down', locals: [start: 2], id: 1)
//            action(command: 'print', arg: 'count', id:2)
//        }
//        sequence {
//            action(command: 'count_down', locals: [start: 8] , id:3)
//            action(command: 'print', arg: 'Next')
//            action(command: 'print', arg: 'count')
//            action(command: 'count_down', locals: [start: 5])
//            action(command: 'print', arg: 'End', id:4)
//        }
    }
}.toTree()

/*

class CountDownCommand extends Command {
    @Override
    Callback.State execute(Callback.Action action, String command, UnsafeMemory memory, int offset) {
        switch (action) {
            case Callback.Action.CONSTRUCT:
                memory.setInt(offset, 0)
                break
            case Callback.Action.EXECUTE:
                int mem = memory.getInt(offset)-1
                memory.setInt(offset, mem)
                return mem <= 0 ? Callback.State.SUCCESS : Callback.State.RUNNING
        }
        return Callback.State.UNDEFINED
    }
}
*/

def assembler = new Assembler()
tree.assemble(assembler)

ASMifierClassVisitor visitor = new ASMifierClassVisitor(new PrintWriter(System.out))
//reader = new ClassReader(assembler.cw.toByteArray());
reader = new ClassReader("org.synopia.behavior.FooBar");
reader.accept(visitor, 0)

def instance = assembler.createInstance(Dispatcher.instance, new DebugCallback() {
    @Override
    void push(int id, BehaviorAction action, String command, int memoryOffset) {

    }

    @Override
    void pop(int returnValue) {

    }
})
println(instance.memory.length)
(1..10).each {
    instance.run();
    println " ---"
}


