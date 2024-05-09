package ca.hojat.zbird.screens

import ca.hojat.zbird.gameworld.GameRenderer
import ca.hojat.zbird.gameworld.GameWorld
import ca.hojat.zbird.zbhelpers.InputHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen

class GameScreen : Screen {
    private val world: GameWorld
    private val renderer: GameRenderer
    private var runTime = 0f

    init {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val gameWidth = 136f
        val gameHeight = screenHeight / (screenWidth / gameWidth)
        val midPointY = (gameHeight / 2).toInt()

        world = GameWorld(midPointY)
        Gdx.input.inputProcessor = InputHandler(
            world,
            screenWidth / gameWidth,
            screenHeight / gameHeight
        )
        renderer = GameRenderer(world, gameHeight.toInt(), midPointY)
        world.setRenderer(renderer)
    }

    override fun render(delta: Float) {
        runTime += delta
        world.update(delta)
        renderer.render(delta, runTime)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun show() {
    }

    override fun hide() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }
}
