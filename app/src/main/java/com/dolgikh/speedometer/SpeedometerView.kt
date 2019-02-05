package com.dolgikh.speedometer

import android.content.Context
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class SpeedometerView: FrameLayout {

    companion object {

        private const val ANGLE_MIN = 0f
        private const val ANGLE_MAX = 270f
        private const val ANGLE_STEP = 10f
        private const val ANGLE_OFFSET_FOR_SMALL_BARS = 5f
    }

    private val maxSpeed: Int
    private val backgroundView: BackgroundView
    private val pointerView: PointerView

    constructor(
        context: Context,
        maxSpeed: Int
    ): super(context) {
        this.maxSpeed = maxSpeed
        this.backgroundView = BackgroundView(
            context = context,
            smallBarsWidthPx = resources.getDimension(R.dimen.small_bar_width),
            smallBarsHeightPx = resources.getDimension(R.dimen.small_bar_height),
            bigBarsWidthPx = resources.getDimension(R.dimen.big_bar_width),
            bigBarsHeightPx = resources.getDimension(R.dimen.big_bar_height),
            backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark),
            barsColor = ContextCompat.getColor(context, R.color.colorAccent),
            angleMinDegrees = ANGLE_MIN,
            angleMaxDegrees = ANGLE_MAX,
            angleStepDegrees = ANGLE_STEP,
            smallBarsAngleOffsetDegrees = ANGLE_OFFSET_FOR_SMALL_BARS
        )
        this.pointerView = PointerView(
            context = context,
            pointerColor = ContextCompat.getColor(context, R.color.pointerColor),
            pointerWidthPx = resources.getDimension(R.dimen.pointer_width)
        )
        addView(backgroundView)
        addView(pointerView)
    }
//
//    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
//
//    }

    fun setSpeed(speedKmh: Float) {
        pointerView.angleDegrees = speedToAngle(speedKmh)
    }

    private fun speedToAngle(speedKmh: Float): Float {
        return (ANGLE_MAX - ANGLE_MIN) * speedKmh / maxSpeed + ANGLE_MIN
    }
}