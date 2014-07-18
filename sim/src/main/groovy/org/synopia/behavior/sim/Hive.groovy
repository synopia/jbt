package org.synopia.behavior.sim

import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

/**
 * Created by synopia on 17.07.2014.
 */
class Hive {
    List<Area> foodSupplies = []
    Area foodStorages = new Area()
    Area brood = new Area()


    Area findFoodSupply(Actor actor) {
        return foodSupplies[0]
    }

    Area findBroodLocation(Actor actor) {

    }


}
