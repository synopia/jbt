/**
 * Created by synopia on 15.07.2014.
 */
builder.tree(debug: [construct: true, execute: true, destruct: true, prune: true, modify: true]) {
//    dynamic_selector(id:0) {
//        for( int j=1;j<=0;j++) {
//            sequence(id:j*100) {
//                for (int i = 1; i <= 1; i++) {
//                    act_count_down(locals: [["count_down": i * 100]])
//                    act_print(id:j*100+i, arg: "$j $i done")
//                }
//                fail()
//                fail()
//            }
//        }
//        dynamic_selector() {
//            fail()
//            succeed()
//        }
//        dynamic_selector() {
//            fail()
//            succeed()
//        }
//    }
    sequence(id: 1) {
        sequence(id: 2) {
            succeed(id: 3)
        }
        sequence(id: 4) {
            running(id: 5)
        }
    }
}
