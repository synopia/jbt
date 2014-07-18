//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package pkg;

import org.synopia.behavior.tree.BehaviorAction;
import org.synopia.behavior.tree.BehaviorState;
import org.synopia.behavior.tree.CompiledBehaviorTree;

public class Foo extends CompiledBehaviorTree {

    public BehaviorState run() {
        if (this.memory.getInt(0) != 2) {
            this.execute(0);
        }

        this.memory.setInt(0, this.execute(1));
        if (this.memory.getInt(0) != 2) {
            this.execute(2);
        }

        return BehaviorState.UNDEFINED;
    }

    public int execute(int var1) {
        byte var10000;
        switch (var1) {
            case 0:
                this.debugCallback.push(1, BehaviorAction.CONSTRUCT, "", 4);
                this.debugCallback.pop(-1);
                var10000 = 3;
                break;
            case 1:
                this.debugCallback.push(1, BehaviorAction.EXECUTE, "", 4);
                var10000 = 0;
                this.debugCallback.pop(-1);
                break;
            case 2:
                this.debugCallback.push(1, BehaviorAction.DESTRUCT, "", 4);
                this.debugCallback.pop(-1);
                var10000 = 3;
                break;
            default:
                var10000 = 3;
        }

        return var10000;
    }

    public Foo() {
        super(4);
    }
}
