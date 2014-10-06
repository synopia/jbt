Java Behavior Trees Framework
===========

Small behavior tree framework. Trees can be built using groovy code:

     builder.tree {
         sequence() {
             sequence() {
                 delay()
                 act_print(arg: 'One')
                 act_count_down(locals: ["count_down": 5])
                 act_print(arg: 'Two')
                 act_count_down(locals: ["count_down": 4])
                 act_print(arg: 'Three')
             }
             sequence(debug: [execute: true]) {
                 delay()
                 act_print(arg: 'Start')
                 act_count_down(locals: ["count_down": 2])
                 succeed(id: 3,)
                 succeed(id: 4)
                 running(id: 5)
                 fail(id: 6)
             }
         }
     }

Once you have a tree constructed (using builder or by directly wiring up the nodes), the tree is compiled into java bytecode.
This bytecode may be used independently of the jbt:asm project (with all that fancy groovy stuff).

The compiled class has one method `run()` that runs the tree one full step and returns the new behavior state of the tree.


Behavior Nodes
------

* **Succeed** Always returns with success
* **Fail** Always returns with failure
* **Run** Always return with running
* **Sequence** Runs every child in a sequence. Return with failure, once a child fails
* **Selector** Runs every child until it returns success
* **Parallel** Runs every child parallel. Stops when one children fails or all are succeeded
* **Dynamic Selector** Works like a selector, but rechecks all failed children each tick
* **Action** Uses a callback to start any action
* **Decorator** Uses a callback to control the result of the decorated node

