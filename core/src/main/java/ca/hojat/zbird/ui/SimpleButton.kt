package ca.hojat.zbird.ui

import ca.hojat.zbird.zbhelpers.AssetLoader
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class SimpleButton(
    private val x: Float,
    private val y: Float,
    val width: Float,
    val height: Float,
    private val buttonUp: TextureRegion,
    private val buttonDown: TextureRegion,
) {
    private val bounds = Rectangle(x, y, width, height)
    private var isPressed = false

    fun draw(batcher: SpriteBatch) {
        if (isPressed) {
            batcher.draw(buttonDown, x, y, width, height)
        } else {
            batcher.draw(buttonUp, x, y, width, height)
        }
    }

    fun isTouchDown(screenX: Int, screenY: Int) {
        if (bounds.contains(screenX.toFloat(), screenY.toFloat())) {
            isPressed = true
        }
    }

    fun isTouchUp(screenX: Int, screenY: Int): Boolean {
        // It only counts as a touchUp if the button is in a pressed state.
        if (bounds.contains(screenX.toFloat(), screenY.toFloat()) && isPressed) {
            isPressed = false
            AssetLoader.flappingSound.play()
            return true
        }

        // Whenever a finger is released, we will cancel any presses.
        isPressed = false
        return false
    }
}