import org.synopia.behavior.commands.Dispatcher
import org.synopia.behavior.tree.CompiledBehaviorTree
import pkg.Foo

/**
 * Created by synopia on 16.07.2014.
 */

CompiledBehaviorTree o = (CompiledBehaviorTree) new Foo()
Dispatcher dispatcher = new Dispatcher(o);
while (true) {
    dispatcher.run();
    System.out.println(".");
}
