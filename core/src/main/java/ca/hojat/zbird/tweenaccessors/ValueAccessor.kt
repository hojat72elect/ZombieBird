package ca.hojat.zbird.tweenaccessors

import aurelienribon.tweenengine.TweenAccessor

class ValueAccessor : TweenAccessor<AssetValue> {
    override fun getValues(target: AssetValue, tweenType: Int, returnValues: FloatArray): Int {
        returnValues[0] = target.value
        return 1
    }

    override fun setValues(target: AssetValue, tweenType: Int, newValues: FloatArray) {
        target.value = newValues[0]
    }
}
