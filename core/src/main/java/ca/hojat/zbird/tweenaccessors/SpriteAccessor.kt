package ca.hojat.zbird.tweenaccessors

import aurelienribon.tweenengine.TweenAccessor
import com.badlogic.gdx.graphics.g2d.Sprite

class SpriteAccessor : TweenAccessor<Sprite> {
    override fun getValues(target: Sprite?, tweenType: Int, returnValues: FloatArray?): Int {
        if (tweenType == ALPHA) {
            returnValues?.set(0, target?.color?.a!!)
            return 1
        }
        return 0
    }

    override fun setValues(target: Sprite?, tweenType: Int, newValues: FloatArray?) {
        if (tweenType == ALPHA) {
            target?.setColor(1f, 1f, 1f, newValues?.get(0)!!)
        }
    }

    companion object {
        const val ALPHA = 1
    }
}