package ca.hojat.zbird.screens

import aurelienribon.tweenengine.BaseTween
import aurelienribon.tweenengine.Tween
import aurelienribon.tweenengine.TweenCallback
import aurelienribon.tweenengine.TweenEquations
import aurelienribon.tweenengine.TweenManager
import ca.hojat.zbird.ZombieFlappyBird
import ca.hojat.zbird.tweenaccessors.SpriteAccessor
import ca.hojat.zbird.zbhelpers.AssetLoader
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class SplashScreen(private val game: ZombieFlappyBird) : Screen {

    private lateinit var manager: TweenManager
    private lateinit var batcher: SpriteBatch
    private lateinit var sprite: Sprite


    override fun show() {
        sprite = Sprite(AssetLoader.logo)
        sprite.setColor(1f, 1f, 1f, 0f)
        val width = Gdx.graphics.width
        val height = Gdx.graphics.height
        val desiredWidth = width * .7f
        val scale = desiredWidth / sprite.width
        sprite.setSize(sprite.width * scale, sprite.height * scale)
        sprite.setPosition(
            (width / 2) - (sprite.width / 2), (height / 2)
                    - (sprite.height / 2)
        )
        setupTween()
        batcher = SpriteBatch()
    }

    override fun render(delta: Float) {
        manager.update(delta)
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batcher.begin()
        sprite.draw(batcher)
        batcher.end()
    }

    override fun resize(width: Int, height: Int) {
        // TODO : better be implemented later
    }

    override fun pause() {
        //TODO
    }

    override fun resume() {
        //TODO
    }

    override fun hide() {
        //TODO
    }

    override fun dispose() {
        //TODO
    }


    private fun setupTween() {
        Tween.registerAccessor(Sprite::class.java, SpriteAccessor())
        manager = TweenManager()
        val cb = TweenCallback { _: Int, _: BaseTween<*>? ->
            game.screen = GameScreen()
        }
        Tween.to(sprite, SpriteAccessor.ALPHA, 1.8f)
            .target(1f)
            .ease(TweenEquations.easeInOutQuad)
            .repeatYoyo(1, .9f)
            .setCallback(cb)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .start(manager)
    }
}