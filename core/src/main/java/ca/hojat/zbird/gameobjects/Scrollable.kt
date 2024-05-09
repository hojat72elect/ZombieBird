package ca.hojat.zbird.gameobjects

import com.badlogic.gdx.math.Vector2

open class Scrollable(
    x: Float,
    y: Float,
    @JvmField val width: Int,
    @JvmField var height: Int,
    scrollSpeed: Float
) {

    @JvmField
    protected var position = Vector2(x, y)

    @JvmField
    protected var velocity = Vector2(scrollSpeed, 0f)

    // Getters for instance variables
    var isScrolledLeft = false
        protected set

    open fun update(delta: Float) {
        position.add(velocity.cpy().scl(delta))

        // If the Scrollable object is no longer visible:
        if (position.x + width < 0) {
            isScrolledLeft = true
        }
    }

   open fun getWidth() = width
   open fun getHeight() = height

    /**
     * Reset: Should Override in subclass for more specific behavior.
     */
    open fun reset(newX: Float) {
        position.x = newX
        isScrolledLeft = false
    }

    fun stop() {
        velocity.x = 0f
    }

    val tailX: Float
        get() = position.x + width

    val x: Float
        get() = position.x

    val y: Float
        get() = position.y
}
