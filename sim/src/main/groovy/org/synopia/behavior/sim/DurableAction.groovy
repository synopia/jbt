package org.synopia.behavior.sim

import org.synopia.behavior.tree.BehaviorState

import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by synopia on 18.07.2014.
 */
public abstract class DurableAction<T> implements Future<T> {
    abstract void tick(delta);

    @Override
    boolean cancel(boolean mayInterruptIfRunning) {
        return false
    }

    @Override
    boolean isCancelled() {
        return false
    }

    @Override
    boolean isDone() {
        return false
    }

    @Override
    T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get()
    }
}
