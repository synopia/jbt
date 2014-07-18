/**
 * Created by synopia on 15.07.2014.
 */
builder.tree {
    sequence() {
        sequence() {
            delay()
            act_print(arg: 'One', id: 1)
            act_count_down(locals: ["count_down": 5])
            act_print(arg: 'Two', id: 1)
            act_count_down(locals: ["count_down": 4])
            act_print(arg: 'Three', id: 1)
        }
        sequence(id: 100, debug: [execute: true]) {
            delay()
            act_print(arg: 'Start', id: 1)
            act_count_down(locals: ["count_down": 2])
//        act_count_down(start: 2)
//        action(command: 'count_down',  id: 2)
            succeed(id: 3,)
            succeed(id: 4)
            running(id: 5)
            fail(id: 6)
        }
    }
}
