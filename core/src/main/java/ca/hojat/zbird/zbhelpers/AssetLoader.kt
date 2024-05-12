package ca.hojat.zbird.zbhelpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion

object AssetLoader {
    private var texture: Texture? = null
    private var logoTexture: Texture? = null
    var logo: TextureRegion? = null
    var zbLogo: TextureRegion? = null
    var bg: TextureRegion? = null
    var grass: TextureRegion? = null
    var bird: TextureRegion? = null
    private var birdDown: TextureRegion? = null
    private var birdUp: TextureRegion? = null
    var skullUp: TextureRegion? = null
    var skullDown: TextureRegion? = null
    var bar: TextureRegion? = null
    var playButtonUp: TextureRegion? = null
    var playButtonDown: TextureRegion? = null
    var ready: TextureRegion? = null
    var gameOver: TextureRegion? = null
    var highScore: TextureRegion? = null
    var scoreboard: TextureRegion? = null
    var star: TextureRegion? = null
    var noStar: TextureRegion? = null
    var retry: TextureRegion? = null
    var birdAnimation: Animation<TextureRegion>? = null
    var deathSound: Sound? = null
    var flappingSound: Sound? = null
    var coinSound: Sound? = null
    var fallSound: Sound? = null
    var font: BitmapFont? = null
    var shadow: BitmapFont? = null
    var whiteFont: BitmapFont? = null
    private var prefs: Preferences? = null

    /**
     * load all the required assets for the game.
     */
    fun load() {
        logoTexture = Texture(Gdx.files.internal("logo.png"))
        logoTexture!!.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        logo = TextureRegion(logoTexture, 0, 0, 320, 139)

        texture = Texture(Gdx.files.internal("texture.png"))
        texture!!.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        playButtonUp = TextureRegion(texture, 0, 83, 29, 16)
        playButtonDown = TextureRegion(texture, 29, 83, 29, 16)
        playButtonUp!!.flip(false, true)
        playButtonDown!!.flip(false, true)

        ready = TextureRegion(texture, 59, 83, 34, 7)
        ready!!.flip(false, true)

        retry = TextureRegion(texture, 59, 110, 33, 7)
        retry!!.flip(false, true)

        gameOver = TextureRegion(texture, 59, 92, 46, 7)
        gameOver!!.flip(false, true)

        scoreboard = TextureRegion(texture, 111, 83, 97, 37)
        scoreboard!!.flip(false, true)

        star = TextureRegion(texture, 152, 70, 10, 10)
        noStar = TextureRegion(texture, 165, 70, 10, 10)

        star!!.flip(false, true)
        noStar!!.flip(false, true)

        highScore = TextureRegion(texture, 59, 101, 48, 7)
        highScore!!.flip(false, true)

        zbLogo = TextureRegion(texture, 0, 55, 135, 24)
        zbLogo!!.flip(false, true)

        bg = TextureRegion(texture, 0, 0, 136, 43)
        bg!!.flip(false, true)

        grass = TextureRegion(texture, 0, 43, 143, 11)
        grass!!.flip(false, true)

        birdDown = TextureRegion(texture, 136, 0, 17, 12)
        birdDown!!.flip(false, true)

        bird = TextureRegion(texture, 153, 0, 17, 12)
        bird!!.flip(false, true)

        birdUp = TextureRegion(texture, 170, 0, 17, 12)
        birdUp!!.flip(false, true)

        val birds = arrayOf(birdDown, bird, birdUp)
        birdAnimation = Animation(0.06f, *birds)
        birdAnimation!!.playMode =
            Animation.PlayMode.LOOP_PINGPONG

        skullUp = TextureRegion(texture, 192, 0, 24, 14)
        // Create by flipping existing skullUp
        skullDown = TextureRegion(skullUp)
        skullDown!!.flip(false, true)

        bar = TextureRegion(texture, 136, 16, 22, 3)
        bar!!.flip(false, true)

        deathSound = Gdx.audio.newSound(Gdx.files.internal("dead.wav"))
        flappingSound = Gdx.audio.newSound(Gdx.files.internal("flap.wav"))
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"))
        fallSound = Gdx.audio.newSound(Gdx.files.internal("fall.wav"))

        font = BitmapFont(Gdx.files.internal("text.fnt"))
        font!!.data.setScale(.25f, -.25f)

        whiteFont = BitmapFont(Gdx.files.internal("whitetext.fnt"))
        whiteFont!!.data.setScale(.1f, -.1f)

        shadow = BitmapFont(Gdx.files.internal("shadow.fnt"))
        shadow!!.data.setScale(.25f, -.25f)

        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("ZombieBird")

        if (!prefs!!.contains("highScore")) {
            prefs!!.putInteger("highScore", 0)
        }
    }

    fun getHighScore() = prefs!!.getInteger("highScore")


    fun setHighScore(newHighScore: Int) {
        prefs!!.putInteger("highScore", newHighScore)
        prefs!!.flush()
    }

    fun dispose() {
        // We must dispose of the texture when we are finished.
        texture!!.dispose()

        // Dispose sounds
        deathSound!!.dispose()
        flappingSound!!.dispose()
        coinSound!!.dispose()

        font!!.dispose()
        shadow!!.dispose()
    }
}
