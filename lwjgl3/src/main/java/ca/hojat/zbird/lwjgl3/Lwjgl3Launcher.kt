package ca.hojat.zbird.lwjgl3

import ca.hojat.zbird.ZombieFlappyBird
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/**
 * Launches the desktop (LWJGL3) application.
 */
object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        if (StartupHelper.startNewJvmIfRequired()) return  // This handles macOS support and helps on Windows.

        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(ZombieFlappyBird(), createDefaultConfiguration())
    }

    private fun createDefaultConfiguration(): Lwjgl3ApplicationConfiguration {

        val windowWidth = 640
        val windowHeight = 480
        val icon128 = "libgdx128.png"
        val icon64 = "libgdx64.png"
        val icon32 = "libgdx32.png"
        val icon16 = "libgdx16.png"

        val configuration =
            Lwjgl3ApplicationConfiguration()

        configuration.setTitle("ZombieBird")
        configuration.useVsync(true)
        /**
         * Limits FPS to the refresh rate of the currently active monitor.
         *
         * If you remove the line below and set Vsync to false, you can get unlimited FPS, which can be
         * useful for testing performance, but can also be very stressful to some hardware.
         * You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
         */
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate)
        configuration.setWindowedMode(windowWidth, windowHeight)
        configuration.setWindowIcon(
            icon128,
            icon64,
            icon32,
            icon16
        )
        return configuration
    }


}