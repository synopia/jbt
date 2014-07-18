package org.synopia.behavior.sim

import groovyx.javafx.GroovyFX
import groovyx.javafx.beans.FXBindable
import javafx.beans.property.FloatProperty

/**
 * Created by synopia on 17.07.2014.
 */
class Area {
    @FXBindable
    float x
    @FXBindable
    float y
    @FXBindable
    float radius
    Storage storage = new Storage()

    Area() {
        FloatProperty p = radiusProperty()

        p.bind(storage.amountProperty().multiply(0.1f))

    }

    @Override
    String toString() {
        return "$x $y $radius"
    }
}
