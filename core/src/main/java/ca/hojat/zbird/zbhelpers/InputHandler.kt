package ca.hojat.zbird.zbhelpers

import ca.hojat.zbird.gameworld.GameWorld
import ca.hojat.zbird.ui.SimpleButton
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor

class InputHandler(
    private val myWorld: GameWorld,
    private val scaleFactorX: Float,
    private val scaleFactorY: Float
) :
    InputProcessor {


    private val myBird = myWorld.bird
    private val menuButtons = arrayListOf<SimpleButton>()
    private val playButton = SimpleButton(
        136f / 2 - (AssetLoader.playButtonUp!!.regionWidth / 2f),
        myWorld.midPointY + 50f,
        29f,
        16f,
        AssetLoader.playButtonUp!!,
        AssetLoader.playButtonDown!!
    )

    init {
        menuButtons.add(playButton)
    }

    fun getMenuButtons() = menuButtons

    override fun keyDown(keycode: Int): Boolean {

        // Can now use Space Bar to play the game
        if (keycode == Input.Keys.SPACE) {


            if (myWorld.isMenu) {
                myWorld.ready()
            } else if (myWorld.isReady) {
                myWorld.start()
            }

            myBird.onClick()

            if (myWorld.isGameOver || myWorld.isHighScore) {
                myWorld.restart()
            }
        }
        return false
    }

    override fun keyUp(keycode: Int) = false

    override fun keyTyped(character: Char) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        if (myWorld.isMenu) {
            playButton.isTouchDown(scaleX(screenX), scaleY(screenY))
        } else if (myWorld.isReady) {
            myWorld.start()
            myBird.onClick()
        } else if (myWorld.isRunning) {
            myBird.onClick()
        }

        if (myWorld.isGameOver || myWorld.isHighScore) {
            myWorld.restart()
        }

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (myWorld.isMenu) {
            if (playButton.isTouchUp(scaleX(screenX), scaleY(screenY))) {
                myWorld.ready()
                return true
            }
        }

        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun scrolled(amountX: Float, amountY: Float) = false

    private fun scaleX(screenX: Int) = (screenX / scaleFactorX).toInt()
    private fun scaleY(screenY: Int) = (screenY / scaleFactorY).toInt()
}