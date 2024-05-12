package ca.hojat.zbird.lwjgl3

import ca.hojat.zbird.ZombieFlappyBird
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.nio.file.FileSystems
import java.util.Locale
import org.lwjgl.system.macosx.LibC

/**
 * Launches the desktop (LWJGL3) application.
 */
fun main() {
    // This handles macOS support and helps on Windows.
    if (startNewJvmIfRequired()) return

    Lwjgl3Application(ZombieFlappyBird(), createDefaultConfiguration())
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
    /*
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

/**
 * Adds some utilities to ensure that the JVM was started with the
 * `-XstartOnFirstThread` argument, which is required on macOS for LWJGL 3
 * to function. Also helps on Windows when users have names with characters from
 * outside the Latin alphabet, a common cause of startup crashes.
 * <br></br>
 * [Based on this java-gaming.org post by kappa](https://jvm-gaming.org/t/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/57547)
 */
private fun startNewJvmIfRequired(redirectOutput: Boolean = true): Boolean {
    val jvmRestartedArgument = "jvmIsRestarted"

    val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
    if (!osName.contains("mac")) {
        if (osName.contains("windows")) {

            /*
             * Here, we are trying to work around an issue with how LWJGL3 loads its
             * extracted .dll files. By default, LWJGL3 extracts to the directory specified
             * by "java.io.tmpdir", which is usually the user's home. If the user's name
             * has non-ASCII (or some non-alphanumeric) characters in it, that would fail.
             * By extracting to the relevant "ProgramData" folder, which is usually
             * "C:\ProgramData", we avoid this.
             */
            System.setProperty(
                "java.io.tmpdir",
                System.getenv("ProgramData") + "/libGDX-temp"
            )
        }
        return false
    }

    // There is no need for -XstartOnFirstThread on GraalVM native image.
    if (System.getProperty("org.graalvm.nativeimage.imagecode", "").isNotEmpty())
        return false

    val pid = LibC.getpid()

    // check whether -XstartOnFirstThread is enabled
    if ("1" == System.getenv("JAVA_STARTED_ON_FIRST_THREAD_$pid"))
        return false

    /*
     * Check whether the JVM was previously restarted; it avoids looping, but most
     * certainly leads to a crash.
     */
    if ("true" == System.getProperty(jvmRestartedArgument)) {
        System.err.println(
            "There was a problem evaluating whether the JVM was started with the -XstartOnFirstThread argument."
        )
        return false
    }

    // Restart the JVM with -XstartOnFirstThread
    val jvmArgs = ArrayList<String>()
    val separator = FileSystems.getDefault().separator
    // The following line is used assuming you target Java 8, the minimum for LWJGL3.
    val javaExecPath =
        System.getProperty("java.home") + separator + "bin" + separator + "java"

    // If targeting Java 9 or higher, you could use the following instead of the above line:
    //String javaExecPath = ProcessHandle.current().info().command().orElseThrow();
    if (!File(javaExecPath).exists()) {
        System.err.println(
            "A Java installation could not be found. If you are distributing this app with a bundled JRE, be sure to set the -XstartOnFirstThread argument manually!"
        )
        return false
    }

    jvmArgs.add(javaExecPath)
    jvmArgs.add("-XstartOnFirstThread")
    jvmArgs.add("-D$jvmRestartedArgument=true")
    jvmArgs.addAll(ManagementFactory.getRuntimeMXBean().inputArguments)
    jvmArgs.add("-cp")
    jvmArgs.add(System.getProperty("java.class.path"))
    var mainClass = System.getenv("JAVA_MAIN_CLASS_$pid")
    if (mainClass == null) {
        val trace = Thread.currentThread().stackTrace
        if (trace.isNotEmpty()) {
            mainClass = trace[trace.size - 1].className
        } else {
            System.err.println("The main class could not be determined.")
            return false
        }
    }
    jvmArgs.add(mainClass)

    try {
        if (!redirectOutput) {
            val processBuilder = ProcessBuilder(jvmArgs)
            processBuilder.start()
        } else {
            val process = ProcessBuilder(jvmArgs)
                .redirectErrorStream(true)
                .start()
            val processOutput = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while ((processOutput.readLine().also { line = it }) != null) {
                println(line)
            }

            process.waitFor()
        }
    } catch (e: Exception) {
        System.err.println("There was a problem restarting the JVM")
        e.printStackTrace()
    }

    return true
}