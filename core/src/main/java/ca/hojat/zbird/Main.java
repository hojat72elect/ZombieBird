package ca.hojat.zbird;

import ca.hojat.zbird.zbhelpers.AssetLoader;
import ca.hojat.zbird.screens.SplashScreen;


public class Main extends com.badlogic.gdx.Game {
    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}