package ca.hojat.zbird.gameobjects

/**
 *  When Grass's constructor is invoked, invoke the parent's constructor (Scrollable).
 */
class Grass(
    x: Float,
    y: Float,
    width: Int,
    height: Int,
    scrollSpeed: Float
) : Scrollable(x, y, width, height, scrollSpeed) {

    fun onRestart(x: Float, scrollSpeed: Float) {
        position.x = x
        velocity.x = scrollSpeed
    }
}
