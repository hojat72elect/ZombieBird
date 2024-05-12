package ca.hojat.zbird.gameworld

import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.TweenEquations
import aurelienribon.tweenengine.TweenManager
import ca.hojat.zbird.gameobjects.Bird
import ca.hojat.zbird.gameobjects.Grass
import ca.hojat.zbird.gameobjects.Pipe
import ca.hojat.zbird.tweenaccessors.AssetValue
import ca.hojat.zbird.tweenaccessors.ValueAccessor
import ca.hojat.zbird.ui.SimpleButton
import ca.hojat.zbird.zbhelpers.AssetLoader
import ca.hojat.zbird.zbhelpers.AssetLoader.getHighScore
import ca.hojat.zbird.zbhelpers.InputHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class GameRenderer(private val myWorld: GameWorld, gameHeight: Int, private val midPointY: Int) {
    private val shapeRenderer: ShapeRenderer

    private val batcher: SpriteBatch

    private val alpha = AssetValue()
    // Buttons

    private val menuButtons: List<SimpleButton> =
        (Gdx.input.inputProcessor as InputHandler)
            .getMenuButtons()
    private val transitionColor: Color

    // Game Objects
    private var bird: Bird? = null
    private var frontGrass: Grass? = null
    private var backGrass: Grass? = null
    private var pipe1: Pipe? = null
    private var pipe2: Pipe? = null
    private var pipe3: Pipe? = null

    // Game Assets
    private var bg: TextureRegion? = null
    private var grass: TextureRegion? = null
    private var birdMid: TextureRegion? = null
    private var skullUp: TextureRegion? = null
    private var skullDown: TextureRegion? = null
    private var bar: TextureRegion? = null
    private var ready: TextureRegion? = null
    private var zbLogo: TextureRegion? = null
    private var gameOver: TextureRegion? = null
    private var highScore: TextureRegion? = null
    private var scoreboard: TextureRegion? = null
    private var star: TextureRegion? = null
    private var noStar: TextureRegion? = null
    private var retry: TextureRegion? = null
    private var birdAnimation: Animation<TextureRegion>? = null

    // Tween stuff
    private var manager: TweenManager? = null

    init {
        val cam = OrthographicCamera()
        cam.setToOrtho(true, 136f, gameHeight.toFloat())

        batcher = SpriteBatch()
        batcher.projectionMatrix = cam.combined
        shapeRenderer = ShapeRenderer()
        shapeRenderer.projectionMatrix = cam.combined

        initGameObjects()
        initAssets()

        transitionColor = Color()
        prepareTransition(255, 255, 255, .5f)
    }

    private fun initGameObjects() {
        bird = myWorld.bird
        val scroller = myWorld.scroller
        frontGrass = scroller.frontGrass
        backGrass = scroller.backGrass
        pipe1 = scroller.pipe1
        pipe2 = scroller.pipe2
        pipe3 = scroller.pipe3
    }

    private fun initAssets() {
        bg = AssetLoader.bg
        grass = AssetLoader.grass
        birdAnimation = AssetLoader.birdAnimation
        birdMid = AssetLoader.bird
        skullUp = AssetLoader.skullUp
        skullDown = AssetLoader.skullDown
        bar = AssetLoader.bar
        ready = AssetLoader.ready
        zbLogo = AssetLoader.zbLogo
        gameOver = AssetLoader.gameOver
        highScore = AssetLoader.highScore
        scoreboard = AssetLoader.scoreboard
        retry = AssetLoader.retry
        star = AssetLoader.star
        noStar = AssetLoader.noStar
    }

    private fun drawGrass() {
        // Draw the grass
        batcher.draw(
            grass, frontGrass!!.x, frontGrass!!.y,
            frontGrass!!.width.toFloat(), frontGrass!!.height.toFloat()
        )
        batcher.draw(
            grass, backGrass!!.x, backGrass!!.y,
            backGrass!!.width.toFloat(), backGrass!!.height.toFloat()
        )
    }

    private fun drawSkulls() {
        batcher.draw(
            skullUp, pipe1!!.x - 1,
            pipe1!!.y + pipe1!!.height - 14, 24f, 14f
        )
        batcher.draw(
            skullDown, pipe1!!.x - 1,
            pipe1!!.y + pipe1!!.height + 45, 24f, 14f
        )

        batcher.draw(
            skullUp, pipe2!!.x - 1,
            pipe2!!.y + pipe2!!.height - 14, 24f, 14f
        )
        batcher.draw(
            skullDown, pipe2!!.x - 1,
            pipe2!!.y + pipe2!!.height + 45, 24f, 14f
        )

        batcher.draw(
            skullUp, pipe3!!.x - 1,
            pipe3!!.y + pipe3!!.height - 14, 24f, 14f
        )
        batcher.draw(
            skullDown, pipe3!!.x - 1,
            pipe3!!.y + pipe3!!.height + 45, 24f, 14f
        )
    }

    private fun drawPipes() {
        batcher.draw(
            bar, pipe1!!.x, pipe1!!.y, pipe1!!.width.toFloat(),
            pipe1!!.height.toFloat()
        )
        batcher.draw(
            bar, pipe1!!.x, pipe1!!.y + pipe1!!.height + 45,
            pipe1!!.width.toFloat(), (midPointY + 66 - (pipe1!!.height + 45)).toFloat()
        )

        batcher.draw(
            bar, pipe2!!.x, pipe2!!.y, pipe2!!.width.toFloat(),
            pipe2!!.height.toFloat()
        )
        batcher.draw(
            bar, pipe2!!.x, pipe2!!.y + pipe2!!.height + 45,
            pipe2!!.width.toFloat(), (midPointY + 66 - (pipe2!!.height + 45)).toFloat()
        )

        batcher.draw(
            bar, pipe3!!.x, pipe3!!.y, pipe3!!.width.toFloat(),
            pipe3!!.height.toFloat()
        )
        batcher.draw(
            bar, pipe3!!.x, pipe3!!.y + pipe3!!.height + 45,
            pipe3!!.width.toFloat(), (midPointY + 66 - (pipe3!!.height + 45)).toFloat()
        )
    }

    private fun drawBirdCentered(runTime: Float) {
        batcher.draw(
            birdAnimation!!.getKeyFrame(runTime) as TextureRegion, 59f, bird!!.getY() - 15,
            bird!!.width / 2.0f, bird!!.height / 2.0f,
            bird!!.width.toFloat(), bird!!.height.toFloat(), 1f, 1f, bird!!.rotation
        )
    }

    private fun drawBird(runTime: Float) {
        if (bird!!.shouldntFlap()) {
            batcher.draw(
                birdMid, bird!!.getX(), bird!!.getY(),
                bird!!.width / 2.0f, bird!!.height / 2.0f,
                bird!!.width.toFloat(), bird!!.height.toFloat(), 1f, 1f, bird!!.rotation
            )
        } else {
            batcher.draw(
                birdAnimation!!.getKeyFrame(runTime) as TextureRegion, bird!!.getX(),
                bird!!.getY(), bird!!.width / 2.0f,
                bird!!.height / 2.0f, bird!!.width.toFloat(), bird!!.height.toFloat(),
                1f, 1f, bird!!.rotation
            )
        }
    }

    private fun drawMenuUI() {
        batcher.draw(
            zbLogo, 136f / 2 - 56, (midPointY - 50).toFloat(),
            zbLogo!!.regionWidth / 1.2f, zbLogo!!.regionHeight / 1.2f
        )

        for (button in menuButtons) {
            button.draw(batcher)
        }
    }

    private fun drawScoreboard() {
        batcher.draw(scoreboard, 22f, (midPointY - 30).toFloat(), 97f, 37f)

        batcher.draw(noStar, 25f, (midPointY - 15).toFloat(), 10f, 10f)
        batcher.draw(noStar, 37f, (midPointY - 15).toFloat(), 10f, 10f)
        batcher.draw(noStar, 49f, (midPointY - 15).toFloat(), 10f, 10f)
        batcher.draw(noStar, 61f, (midPointY - 15).toFloat(), 10f, 10f)
        batcher.draw(noStar, 73f, (midPointY - 15).toFloat(), 10f, 10f)

        if (myWorld.score > 2) {
            batcher.draw(star, 73f, (midPointY - 15).toFloat(), 10f, 10f)
        }

        if (myWorld.score > 17) {
            batcher.draw(star, 61f, (midPointY - 15).toFloat(), 10f, 10f)
        }

        if (myWorld.score > 50) {
            batcher.draw(star, 49f, (midPointY - 15).toFloat(), 10f, 10f)
        }

        if (myWorld.score > 80) {
            batcher.draw(star, 37f, (midPointY - 15).toFloat(), 10f, 10f)
        }

        if (myWorld.score > 120) {
            batcher.draw(star, 25f, (midPointY - 15).toFloat(), 10f, 10f)
        }

        val length = ("" + myWorld.score).length

        AssetLoader.whiteFont!!.draw(
            batcher, "" + myWorld.score,
            (104 - (2 * length)).toFloat(), (midPointY - 20).toFloat()
        )

        val length2 = ("" + getHighScore()).length
        AssetLoader.whiteFont!!.draw(
            batcher, "" + getHighScore(),
            104 - (2.5f * length2), (midPointY - 3).toFloat()
        )
    }

    private fun drawRetry() {
        batcher.draw(retry, 36f, (midPointY + 10).toFloat(), 66f, 14f)
    }

    private fun drawReady() {
        batcher.draw(ready, 36f, (midPointY - 50).toFloat(), 68f, 14f)
    }

    private fun drawGameOver() {
        batcher.draw(gameOver, 24f, (midPointY - 50).toFloat(), 92f, 14f)
    }

    private fun drawScore() {
        val length = ("" + myWorld.score).length
        AssetLoader.shadow!!.draw(
            batcher, "" + myWorld.score,
            (68 - (3 * length)).toFloat(), (midPointY - 82).toFloat()
        )
        AssetLoader.font!!.draw(
            batcher, "" + myWorld.score,
            (68 - (3 * length)).toFloat(), (midPointY - 83).toFloat()
        )
    }

    private fun drawHighScore() {
        batcher.draw(highScore, 22f, (midPointY - 50).toFloat(), 96f, 14f)
    }

    fun render(delta: Float, runTime: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Draw Background color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1f)
        shapeRenderer.rect(0f, 0f, 136f, (midPointY + 66).toFloat())

        // Draw Grass
        shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1f)
        shapeRenderer.rect(0f, (midPointY + 66).toFloat(), 136f, 11f)

        // Draw Dirt
        shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1f)
        shapeRenderer.rect(0f, (midPointY + 77).toFloat(), 136f, 52f)

        shapeRenderer.end()

        batcher.begin()
        batcher.disableBlending()

        batcher.draw(bg, 0f, (midPointY + 23).toFloat(), 136f, 43f)

        drawPipes()

        batcher.enableBlending()
        drawSkulls()

        if (myWorld.isRunning) {
            drawBird(runTime)
            drawScore()
        } else if (myWorld.isReady) {
            drawBird(runTime)
            drawReady()
        } else if (myWorld.isMenu) {
            drawBirdCentered(runTime)
            drawMenuUI()
        } else if (myWorld.isGameOver) {
            drawScoreboard()
            drawBird(runTime)
            drawGameOver()
            drawRetry()
        } else if (myWorld.isHighScore) {
            drawScoreboard()
            drawBird(runTime)
            drawHighScore()
            drawRetry()
        }

        drawGrass()

        batcher.end()
        drawTransition(delta)
    }

    fun prepareTransition(r: Int, g: Int, b: Int, duration: Float) {
        transitionColor[r / 255.0f, g / 255.0f, b / 255.0f] = 1f
        alpha.value = 1f
        Tween.registerAccessor(AssetValue::class.java, ValueAccessor())
        manager = TweenManager()
        Tween.to(alpha, -1, duration).target(0f)
            .ease(TweenEquations.easeOutQuad).start(manager)
    }

    private fun drawTransition(delta: Float) {
        if (alpha.value > 0) {
            manager!!.update(delta)
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.setColor(
                transitionColor.r, transitionColor.g,
                transitionColor.b, alpha.value
            )
            shapeRenderer.rect(0f, 0f, 136f, 300f)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }
    }
}
