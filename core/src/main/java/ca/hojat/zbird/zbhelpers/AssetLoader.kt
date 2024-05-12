package ca.hojat.zbird.zbhelpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion

object AssetLoader {
    var texture: Texture? = null
    var logoTexture: Texture? = null
    var logo: TextureRegion? = null

    @JvmField
    var zbLogo: TextureRegion? = null

    @JvmField
    var bg: TextureRegion? = null

    @JvmField
    var grass: TextureRegion? = null

    @JvmField
    var bird: TextureRegion? = null
    var birdDown: TextureRegion? = null
    var birdUp: TextureRegion? = null

    @JvmField
    var skullUp: TextureRegion? = null

    @JvmField
    var skullDown: TextureRegion? = null

    @JvmField
    var bar: TextureRegion? = null
    var playButtonUp: TextureRegion? = null
    var playButtonDown: TextureRegion? = null

    @JvmField
    var ready: TextureRegion? = null

    @JvmField
    var gameOver: TextureRegion? = null

    @JvmField
    var highScore: TextureRegion? = null

    @JvmField
    var scoreboard: TextureRegion? = null

    @JvmField
    var star: TextureRegion? = null

    @JvmField
    var noStar: TextureRegion? = null

    @JvmField
    var retry: TextureRegion? = null

    @JvmField
    var birdAnimation: Animation<TextureRegion>? = null

    @JvmField
    var deathSound: Sound? = null
    var flappingSound: Sound? = null

    @JvmField
    var coinSound: Sound? = null

    @JvmField
    var fallSound: Sound? = null

    @JvmField
    var font: BitmapFont? = null

    @JvmField
    var shadow: BitmapFont? = null

    @JvmField
    var whiteFont: BitmapFont? = null
    private var prefs: Preferences? = null

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

    @JvmStatic
    fun getHighScore(): Int {
        return prefs!!.getInteger("highScore")
    }

    @JvmStatic
    fun setHighScore(`val`: Int) {
        prefs!!.putInteger("highScore", `val`)
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
