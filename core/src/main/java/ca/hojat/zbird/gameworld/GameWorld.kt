package ca.hojat.zbird.gameworld

import ca.hojat.zbird.gameobjects.Bird
import ca.hojat.zbird.gameobjects.ScrollHandler
import ca.hojat.zbird.zbhelpers.AssetLoader
import ca.hojat.zbird.zbhelpers.AssetLoader.getHighScore
import ca.hojat.zbird.zbhelpers.AssetLoader.setHighScore
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle

class GameWorld(val midPointY: Int) {
    val bird: Bird
    val scroller: ScrollHandler
    private val ground: Rectangle
    var score: Int = 0
        private set
    private var runTime = 0f
    private var renderer: GameRenderer? = null

    private var currentState: GameState

    enum class GameState {
        MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    init {
        currentState = GameState.MENU
        bird = Bird(33f, (midPointY - 5).toFloat(), 17, 12)
        // The grass should start 66 pixels below the midPointY
        scroller = ScrollHandler(this, (midPointY + 66).toFloat())
        ground = Rectangle(0f, (midPointY + 66).toFloat(), 137f, 11f)
    }

    fun update(delta: Float) {
        runTime += delta

        when (currentState) {
            GameState.READY, GameState.MENU -> updateReady(delta)
            GameState.RUNNING -> updateRunning(delta)
            else -> {}
        }
    }

    private fun updateReady(delta: Float) {
        bird.updateReady(runTime)
        scroller.updateReady(delta)
    }

    private fun updateRunning(delta: Float) {
        var delta = delta
        if (delta > .15f) {
            delta = .15f
        }

        bird.update(delta)
        scroller.update(delta)

        if (scroller.collides(bird) && bird.isAlive) {
            scroller.stop()
            bird.die()
            AssetLoader.deathSound!!.play()
            renderer!!.prepareTransition(255, 255, 255, .3f)

            AssetLoader.fallSound!!.play()
        }

        if (Intersector.overlaps(bird.boundingCircle, ground)) {
            if (bird.isAlive) {
                AssetLoader.deathSound!!.play()
                renderer!!.prepareTransition(255, 255, 255, .3f)

                bird.die()
            }

            scroller.stop()
            bird.decelerate()
            currentState = GameState.GAMEOVER

            if (score > getHighScore()) {
                setHighScore(score)
                currentState = GameState.HIGHSCORE
            }
        }
    }

    fun addScore(increment: Int) {
        score += increment
    }

    fun start() {
        currentState = GameState.RUNNING
    }

    fun ready() {
        currentState = GameState.READY
        renderer!!.prepareTransition(0, 0, 0, 1f)
    }

    fun restart() {
        score = 0
        bird.onRestart(midPointY - 5)
        scroller.onRestart()
        ready()
    }

    val isReady: Boolean
        get() = currentState == GameState.READY

    val isGameOver: Boolean
        get() = currentState == GameState.GAMEOVER

    val isHighScore: Boolean
        get() = currentState == GameState.HIGHSCORE

    val isMenu: Boolean
        get() = currentState == GameState.MENU

    val isRunning: Boolean
        get() = currentState == GameState.RUNNING

    fun setRenderer(renderer: GameRenderer?) {
        this.renderer = renderer
    }
}
