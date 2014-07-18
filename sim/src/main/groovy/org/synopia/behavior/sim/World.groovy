package org.synopia.behavior.sim

import org.synopia.behavior.tree.BehaviorState

import java.util.concurrent.ExecutionException

/**
 * Created by synopia on 17.07.2014.
 */
class World {
    List<Actor> actors = []
    List<Area> areas = []
    Map<Actor, DurableAction> actions = [:]

    static boolean isIn(Actor actor, Area area) {
        float dist = Math.sqrt((area.x - actor.x) * (area.x - actor.x) + (area.y - actor.y) * (area.y - actor.y))
        dist < area.storage.amount
    }

    DurableAction moveTo(Actor actor, Area area) {
        def action = new MoveTo(actor, area)
        actions[actor] = action
        action
    }

    DurableAction harvest(Actor actor, Area area, float amount) {
        def action = new Transfer(area.storage, actor.storage, amount)
        actions[actor] = action
        action
    }

    DurableAction store(Actor actor, Area area, float amount) {
        def action = new Transfer(actor.storage, area.storage, amount)
        actions[actor] = action
        action
    }

    float eat(Actor actor, float amount) {
        Area area = areas.find { isIn(actor, it) }
        float eaten = 0
        if (area) {
            eaten = Math.max(0, area.radius - amount)
            eaten = Math.min(0, eaten)
            area.radius -= eaten
            actor.energy += eaten

            amount -= eaten
        }
        if (amount > 0) {
            eaten = Math.max(0, actor.carriage - amount)
            eaten = Math.max(0, eaten)
            actor.carriage -= eaten
            actor.energy += eaten
        }
        return eaten
    }


    void tick(delta) {
        actors.each { actor ->
            actor.tick(delta)
        }

        actions.each { actor, action ->
            action.tick(delta)
        }
    }

    class MoveTo extends DurableAction<BehaviorState> {
        static float MOVE_SPEED = 25f
        Actor actor
        Area target
        private float dist = Float.MAX_VALUE

        MoveTo(Actor actor, Area target) {
            this.actor = actor
            this.target = target
        }

        @Override
        void tick(Object delta) {
            float deltaX = target.x - actor.x
            float deltaY = target.y - actor.y
            dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY)
            if (dist > 0) {
                deltaX /= this.dist
                deltaY /= this.dist
                float step = MOVE_SPEED * delta.toSeconds()
                if (dist < step) {
                    step = dist
                }
                actor.x += deltaX * step
                actor.y += deltaY * step
            }
        }

        @Override
        BehaviorState get() throws InterruptedException, ExecutionException {
            if (isDone()) {
                return BehaviorState.SUCCESS
            }
            return BehaviorState.RUNNING
        }

        @Override
        boolean isDone() {
            return dist < target.radius
        }
    }

    class Transfer extends DurableAction<BehaviorState> {
        static float TRANSFER_SPEED = 10f
        Storage source
        Storage target
        float amountToTransfer
        float transferred

        Transfer(Storage source, Storage target, float amountToTransfer) {
            this.source = source
            this.target = target
            this.amountToTransfer = amountToTransfer
        }

        @Override
        void tick(Object delta) {
            if (!isDone()) {
                float toTake = Math.min(amountToTransfer - transferred, source.amount)
                float take = Math.min(delta.toSeconds() * TRANSFER_SPEED, toTake)
                target.amount += take
                source.amount -= take
                transferred += take
            }
        }

        @Override
        BehaviorState get() throws InterruptedException, ExecutionException {
            if (isDone()) {
                return isCancelled() ? BehaviorState.FAILURE : BehaviorState.SUCCESS
            }
            return BehaviorState.RUNNING
        }

        @Override
        boolean isCancelled() {
            source.amount <= 0
        }

        @Override
        boolean isDone() {
            return isCancelled() || transferred >= amountToTransfer
        }
    }
}
