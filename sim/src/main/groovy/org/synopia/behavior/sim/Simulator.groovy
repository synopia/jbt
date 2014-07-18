package org.synopia.behavior.sim

import org.synopia.behavior.Assembler
import org.synopia.behavior.BTreeBuilder
import org.synopia.behavior.BehaviorNode

/**
 * Created by synopia on 17.07.2014.
 */
class Simulator {
    BehaviorNode tree = new BTreeBuilder().tree() {
        sequence() {
            action(command: "find_food_supply")
            action(command: "move_to")
            action(command: "harvest")

            action(command: "find_food_storage")
            action(command: "move_to")
            action(command: "store")

            running()
        }
    }
    Assembler assembler
    World world

    Simulator() {
        assembler = new Assembler()
        this.assembler.assemble(tree)

        world = new World()
        def hive = new Hive()


        def food = new Area(x: 300, y: 200)
        food.storage.amount = 300
        world.areas << food
        hive.foodSupplies << food

        def storage = new Area(x: 10, y: 20)
        storage.storage.amount = 3
        world.areas << storage
        hive.foodStorages = storage

        def actor = new Actor(world, hive)
        actor.setTree(assembler.createInstance())
        world.actors << actor
    }

    void tick(delta) {
        world.tick(delta)
    }
}
