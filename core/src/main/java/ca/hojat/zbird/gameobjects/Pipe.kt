package ca.hojat.zbird.gameobjects

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import java.util.Random


class Pipe(
    x: Float,
    y: Float,
    width: Int,
    height: Int,
    scrollSpeed: Float,
    private val groundY: Float
) :
    Scrollable(
        x,
        y,
        width,
        height,
        scrollSpeed
    ) {

    private val r = Random()
    private val skullUp = Rectangle()
    private val skullDown = Rectangle()
    private val barUp = Rectangle()
    private val barDown = Rectangle()

    var isScored = false


    override fun update(delta: Float) {
        super.update(delta)
        barUp.set(position.x, position.y, width.toFloat(), height.toFloat())
        barDown.set(
            position.x,
            position.y + height + VERTICAL_GAP,
            width.toFloat(),
            groundY - (position.y + height + VERTICAL_GAP)
        )
        skullUp.set(
            position.x - (SKULL_WIDTH - width) / 2,
            position.y + height - SKULL_HEIGHT,
            SKULL_WIDTH.toFloat(),
            SKULL_HEIGHT.toFloat()
        )
        skullDown.set(
            position.x - (SKULL_WIDTH - width) / 2,
            barDown.y,
            SKULL_WIDTH.toFloat(),
            SKULL_HEIGHT.toFloat()
        )
    }

    override fun reset(newX: Float) {
        super.reset(newX)
        height = r.nextInt(90) + 15
        isScored = false
    }

    fun onRestart(x: Float, scrollSpeed: Float) {
        velocity.x = scrollSpeed
        reset(x)
    }

    fun collides(bird: Bird): Boolean {
        if (position.x < bird.x + bird.width) {
            return (Intersector.overlaps(bird.boundingCircle, barUp)
                    || Intersector.overlaps(bird.boundingCircle, barDown)
                    || Intersector.overlaps(bird.boundingCircle, skullUp)
                    || Intersector.overlaps(bird.boundingCircle, skullDown))
        }
        return false
    }

    companion object {
        const val VERTICAL_GAP = 45
        const val SKULL_WIDTH = 24
        const val SKULL_HEIGHT = 11

    }
}