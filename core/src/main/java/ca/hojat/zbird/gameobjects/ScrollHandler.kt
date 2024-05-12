package ca.hojat.zbird.gameobjects

import ca.hojat.zbird.gameworld.GameWorld
import ca.hojat.zbird.zbhelpers.AssetLoader

class ScrollHandler(private val gameWorld: GameWorld, yPos: Float) {
    @JvmField
    val frontGrass: Grass
    @JvmField
    val backGrass: Grass
    @JvmField
    val pipe1: Pipe
    @JvmField
    val pipe2: Pipe
    @JvmField
    val pipe3: Pipe

    init {
        frontGrass = Grass(0f, yPos, 143, 11, SCROLL_SPEED.toFloat())
        backGrass = Grass(
            frontGrass.tailX, yPos, 143, 11,
            SCROLL_SPEED.toFloat()
        )

        pipe1 = Pipe(210f, 0f, 22, 60, SCROLL_SPEED.toFloat(), yPos)
        pipe2 = Pipe(
            pipe1.tailX + PIPE_GAP, 0f, 22, 70, SCROLL_SPEED.toFloat(),
            yPos
        )
        pipe3 = Pipe(
            pipe2.tailX + PIPE_GAP, 0f, 22, 60, SCROLL_SPEED.toFloat(),
            yPos
        )
    }

    fun updateReady(delta: Float) {
        frontGrass.update(delta)
        backGrass.update(delta)

        // Same with grass
        if (frontGrass.isScrolledLeft) {
            frontGrass.reset(backGrass.tailX)
        } else if (backGrass.isScrolledLeft) {
            backGrass.reset(frontGrass.tailX)
        }
    }

    fun update(delta: Float) {
        // Update our objects
        frontGrass.update(delta)
        backGrass.update(delta)
        pipe1.update(delta)
        pipe2.update(delta)
        pipe3.update(delta)

        // Check if any of the pipes are scrolled left,
        // and reset accordingly
        if (pipe1.isScrolledLeft) {
            pipe1.reset(pipe3.tailX + PIPE_GAP)
        } else if (pipe2.isScrolledLeft) {
            pipe2.reset(pipe1.tailX + PIPE_GAP)
        } else if (pipe3.isScrolledLeft) {
            pipe3.reset(pipe2.tailX + PIPE_GAP)
        }

        // Same with grass
        if (frontGrass.isScrolledLeft) {
            frontGrass.reset(backGrass.tailX)
        } else if (backGrass.isScrolledLeft) {
            backGrass.reset(frontGrass.tailX)
        }
    }

    fun stop() {
        frontGrass.stop()
        backGrass.stop()
        pipe1.stop()
        pipe2.stop()
        pipe3.stop()
    }

    fun collides(bird: Bird): Boolean {
        if (!pipe1.isScored
            && pipe1.x + (pipe1.getWidth() / 2f) < bird.getX()
            + bird.width
        ) {
            addScore()
            pipe1.isScored = true
            AssetLoader.coinSound!!.play()
        } else if (!pipe2.isScored
            && pipe2.x + (pipe2.getWidth() / 2f) < bird.getX()
            + bird.width
        ) {
            addScore()
            pipe2.isScored = true
            AssetLoader.coinSound!!.play()
        } else if (!pipe3.isScored
            && pipe3.x + (pipe3.getWidth() / 2f) < bird.getX()
            + bird.width
        ) {
            addScore()
            pipe3.isScored = true
            AssetLoader.coinSound!!.play()
        }

        return (pipe1.collides(bird) || pipe2.collides(bird) || pipe3
            .collides(bird))
    }

    private fun addScore() {
        gameWorld.addScore(1)
    }

    fun onRestart() {
        frontGrass.onRestart(0f, SCROLL_SPEED.toFloat())
        backGrass.onRestart(frontGrass.tailX, SCROLL_SPEED.toFloat())
        pipe1.onRestart(210f, SCROLL_SPEED.toFloat())
        pipe2.onRestart(pipe1.tailX + PIPE_GAP, SCROLL_SPEED.toFloat())
        pipe3.onRestart(pipe2.tailX + PIPE_GAP, SCROLL_SPEED.toFloat())
    }

    companion object {
        const val SCROLL_SPEED: Int = -59
        const val PIPE_GAP: Int = 49
    }
}
