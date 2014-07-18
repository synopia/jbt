/**
 * Created by synopia on 17.07.2014.
 */

import groovyx.javafx.beans.FXBindable
import org.synopia.behavior.sim.Simulator

import static groovyx.javafx.GroovyFX.start

start {
    def sim = new Simulator()

    stage(title: 'GroovyFX Hello World', visible: true) {
        scene(fill: BLACK, width: 500, height: 250) {
            sim.world.areas.each { area ->
                circle(centerX: bind(area.x()), centerY: bind(area.y()), radius: bind(area, 'radius'), fill: rgb(0, 100, 0))
            }
            sim.world.actors.each { actor ->
                circle(centerX: bind(actor.x()), centerY: bind(actor.y()), radius: 5, fill: rgb(255, 255, 0))
            }

        }
    }

    sequentialTransition(cycleCount: INDEFINITE) {
        pauseTransition(10.ms) {
            onFinished { sim.tick(100.ms) }
        }
    }.playFromStart()
}

