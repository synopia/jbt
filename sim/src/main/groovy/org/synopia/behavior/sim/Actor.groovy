package org.synopia.behavior.sim

import groovyx.javafx.beans.FXBindable
import org.synopia.behavior.commands.Command
import org.synopia.behavior.commands.Dispatcher
import org.synopia.behavior.tree.BehaviorState
import org.synopia.behavior.tree.CompiledBehaviorTree
import org.synopia.behavior.tree.Context

/**
 * Created by synopia on 17.07.2014.
 */
class Actor {
    @FXBindable
    float x
    @FXBindable
    float y
    Storage storage = new Storage()

    CompiledBehaviorTree tree
    Dispatcher dispatcher
    World world
    Hive hive;
    Area area

    Actor(World world, Hive hive) {
        this.world = world
        this.hive = hive
    }

    void tick(delta) {
        tree.run()
    }

    void setTree(CompiledBehaviorTree tree) {
        this.tree = tree;
        dispatcher = new Dispatcher(tree)

        registerImmediateCommand("find_food_supply", { Context context ->
            area = hive.findFoodSupply(this)
            return area != null ? BehaviorState.SUCCESS : BehaviorState.FAILURE
        })
        registerImmediateCommand("find_food_storage", { Context context ->
            area = hive.foodStorages
            return area != null ? BehaviorState.SUCCESS : BehaviorState.FAILURE
        })
        registerDurableCommand("move_to", { Context context ->
            world.moveTo(this, area)
        })
        registerDurableCommand("harvest", { Context context ->
            world.harvest(this, area, (float) (10 - storage.amount))
        })
        registerDurableCommand("store", { Context context ->
            world.store(this, area, storage.amount)
        })
    }

    void registerImmediateCommand(String name, Closure f) {
        this.dispatcher.register(new Command() {
            @Override
            void construct(Context context) {
            }

            @Override
            BehaviorState execute(Context context) {
                return f(context)
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

            @Override
            String name() {
                return name
            }
        })
    }

    void registerDurableCommand(String name, Closure f) {
        this.dispatcher.register(new Command() {
            DurableAction<BehaviorState> action

            @Override
            void construct(Context context) {
                action = f(context)
            }

            @Override
            BehaviorState execute(Context context) {
                return action.get()
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

            @Override
            String name() {
                return name
            }
        })
    }

    @Override
    String toString() {
        return "$x $y ${storage.amount}"
    }
}
