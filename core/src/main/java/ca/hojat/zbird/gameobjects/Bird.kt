package ca.hojat.zbird.gameobjects

import ca.hojat.zbird.zbhelpers.AssetLoader
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import kotlin.math.sin

class Bird(
     x: Float,
    y: Float,
    private val _width: Int,
    private val _height: Int
) {

    val width: Int
        get() = this._width

    val height: Int
        get() = this._height

    private val position = Vector2(x, y)
    private val velocity = Vector2(0f, 0f)
    private val acceleration = Vector2(0f, 460f)
    private var rotation = 0f


    private val originalY = y
    private var isAlive = true
    private val boundingCircle = Circle()


    fun update(delta: Float) {
        velocity.add(acceleration.cpy().scl(delta))
        if (velocity.y > 200)
            velocity.y = 200f

        // Don't let the bird go above the ceiling
        if (position.y < -13) {
            position.y = -13f
            velocity.y = 0f
        }
        position.add(velocity.cpy().scl(delta))

        /**
         * Set the circle's center to be (9, 6) with respect to the bird.
         * Set the circle's radius to be 6.5f.
         */
        boundingCircle.set(position.x + 9, position.y + 6, 6.5f)


        // Rotate counterclockwise
        if (velocity.y < 0) {
            rotation -= 600 * delta

            if (rotation < -20) {
                rotation = -20f
            }
        }


        // Rotate clockwise
        if (isFalling() || isAlive.not()) {
            rotation += 480 * delta
            if (rotation > 90) {
                rotation = 90f
            }
        }

    }

    fun updateReady(runTime: Float) {
        position.y = 2 * sin((7 * runTime).toDouble()).toFloat() + originalY
    }

    fun isFalling() = velocity.y > 110

    fun shouldntFlap() = (velocity.y > 70) || (!isAlive)

    fun onClick() {
        if (isAlive) {
            AssetLoader.flappingSound?.play()
            velocity.y = -140f
        }

    }

    fun die() {
        isAlive = false
        velocity.y = 0f
    }

    fun decelerate() {
        acceleration.y = 0f
    }

    fun onRestart(y: Int) {
        rotation = 0f
        position.y = y.toFloat()
        velocity.x = 0f
        velocity.y = 0f
        acceleration.x = 0f
        acceleration.y = 460f
        isAlive = true
    }

    fun getX() = position.x

    fun getY() = position.y

    fun getRotation() = rotation
    fun getBoundingCircle() = boundingCircle
    fun isAlive() = isAlive

}