package ca.hojat.zbird

import ca.hojat.zbird.screens.SplashScreen
import ca.hojat.zbird.zbhelpers.AssetLoader
import com.badlogic.gdx.Game

class ZombieFlappyBird : Game() {
    override fun create() {
        AssetLoader.load()
        setScreen(SplashScreen(this))
    }

    override fun dispose() {
        super.dispose()
        AssetLoader.dispose()
    }
}