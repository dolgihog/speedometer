package com.dolgikh.speedometer.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.dolgikh.speedometer.R

class SpeedometerView : FrameLayout {

    companion object {

        private const val ANGLE_MIN = 45f
        private const val ANGLE_MAX = 315f
        private const val MAX_SPEED = 180f
        private const val MIN_SPEED = 0f
        private const val BIG_SPEED_STEP = 20f
        private const val SMALL_SPEED_STEP = 10f
    }

    private var defSmallBarsWidthPx: Float = 0f
    private var defSmallBarsHeightPx: Float = 0f
    private var defBigBarsWidthPx: Float = 0f
    private var defBigBarsHeightPx: Float = 0f
    private var defAngleMinDegrees: Float = 0f
    private var defAngleMaxDegrees: Float = 0f
    private var defTextSizePx: Float = 0f
    private var defMaxSpeedKmh: Float = 0f
    private var defMinSpeedKmh: Float = 0f
    private var defBigSpeedStepKmh: Float = 0f
    private var defSmallSpeedStepKmh: Float = 0f

    private var defTextColor: Int = 0
    private var defBackgroundColor: Int = 0
    private var defBarsColor: Int = 0

    private var defPointerWidthPx: Float = 0f
    private var defPointerColor: Int = 0


    private val maxSpeed: Float
    private val backgroundView: BackgroundView
    private val pointerView: PointerView
    private var angleMaxDegrees: Float
    private var angleMinDegrees: Float

    constructor(
        context: Context,
        smallBarsWidthPx: Float? = null,
        smallBarsHeightPx: Float? = null,
        bigBarsWidthPx: Float? = null,
        bigBarsHeightPx: Float? = null,
        angleMinDegrees: Float? = null,
        angleMaxDegrees: Float? = null,
        textSizePx: Float? = null,
        maxSpeedKmh: Float? = null,
        minSpeedKmh: Float? = null,
        bigSpeedStepKmh: Float? = null,
        smallSpeedStepKmh: Float? = null,
        textColorInt: Int? = null,
        backgroundColorInt: Int? = null,
        bigBarsColorInt: Int? = null,
        pointerWidthPx: Float? = null,
        pointerColorInt: Int? = null,
        smallBarsColorInt: Int? = null
    ) : super(context) {
        initDefaults(context)
        this.angleMaxDegrees = angleMaxDegrees ?: defAngleMaxDegrees
        this.angleMinDegrees = angleMinDegrees ?: defAngleMinDegrees
        this.maxSpeed = maxSpeedKmh ?: defMaxSpeedKmh
        this.backgroundView = BackgroundView(
            context = context,
            smallBarsHeightPx = smallBarsHeightPx ?: defSmallBarsHeightPx,
            smallBarsWidthPx = smallBarsWidthPx ?: defSmallBarsWidthPx,
            bigBarsHeightPx = bigBarsHeightPx ?: defBigBarsHeightPx,
            bigBarsWidthPx = bigBarsWidthPx ?: defBigBarsWidthPx,
            angleMaxDegrees = angleMaxDegrees ?: defAngleMaxDegrees,
            angleMinDegrees = angleMinDegrees ?: defAngleMinDegrees,
            textSizePx = textSizePx ?: defTextSizePx,
            maxSpeedKmh = maxSpeedKmh ?: defMaxSpeedKmh,
            minSpeedKmh = minSpeedKmh ?: defMinSpeedKmh,
            bigSpeedStepKmh = bigSpeedStepKmh ?: defBigSpeedStepKmh,
            smallSpeedStepKmh = smallSpeedStepKmh ?: defSmallSpeedStepKmh,
            textColorInt = textColorInt ?: defTextColor,
            backgroundColorInt = backgroundColorInt ?: defBackgroundColor,
            bigBarsColorInt = bigBarsColorInt ?: defBarsColor,
            smallBarsColorInt = smallBarsColorInt ?: defBarsColor
        )
        this.pointerView = PointerView(
            context = context,
            pointerWidthPx = pointerWidthPx ?: defPointerWidthPx,
            pointerColor = pointerColorInt ?: defPointerColor
        )
        addView(backgroundView)
        addView(pointerView)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initDefaults(context)
        context.theme.obtainStyledAttributes(attrs, R.styleable.SpeedometerView, 0, 0).apply {
            try {
                val smallBarsWidth = getDimension(R.styleable.SpeedometerView_smallBarsWidth, defSmallBarsWidthPx)
                val smallBarsHeight = getDimension(R.styleable.SpeedometerView_smallBarsHeight, defSmallBarsHeightPx)
                val bigBarsWidth = getDimension(R.styleable.SpeedometerView_bigBarsWidth, defBigBarsWidthPx)
                val bigBarsHeight = getDimension(R.styleable.SpeedometerView_bigBarsHeight, defBigBarsHeightPx)
                angleMaxDegrees = getFloat(R.styleable.SpeedometerView_angleMaxDegrees, defAngleMaxDegrees)
                angleMinDegrees = getFloat(R.styleable.SpeedometerView_angleMinDegrees, defAngleMinDegrees)
                val bigSpeedStepKmh = getFloat(R.styleable.SpeedometerView_bigSpeedStepKmh, defBigSpeedStepKmh)
                val smallSpeedStepKmh = getFloat(R.styleable.SpeedometerView_smallSpeedStepKmh, defSmallSpeedStepKmh)
                val minSpeedKmh = getFloat(R.styleable.SpeedometerView_minSpeedKmh, defMinSpeedKmh)
                val maxSpeedKmh = getFloat(R.styleable.SpeedometerView_maxSpeedKmh, defMaxSpeedKmh)
                val backgroundColor = getColor(R.styleable.SpeedometerView_backgroundColor, defBackgroundColor)
                val barsColor = getColor(R.styleable.SpeedometerView_bigBarsColor, defBarsColor)
                val textPaintColor = getColor(R.styleable.SpeedometerView_textColor, defTextColor)
                val textSize = getDimension(R.styleable.SpeedometerView_textSize, defTextSizePx)
                val smallBarsColor = getColor(R.styleable.SpeedometerView_smallBarsColor, defBarsColor)
                backgroundView = BackgroundView(
                    context = context,
                    smallBarsWidthPx = smallBarsWidth,
                    smallBarsHeightPx = smallBarsHeight,
                    bigBarsWidthPx = bigBarsWidth,
                    bigBarsHeightPx = bigBarsHeight,
                    angleMaxDegrees = angleMaxDegrees,
                    angleMinDegrees = angleMinDegrees,
                    bigSpeedStepKmh = bigSpeedStepKmh,
                    smallSpeedStepKmh = smallSpeedStepKmh,
                    minSpeedKmh = minSpeedKmh,
                    maxSpeedKmh = maxSpeedKmh,
                    backgroundColorInt = backgroundColor,
                    bigBarsColorInt = barsColor,
                    textColorInt = textPaintColor,
                    textSizePx = textSize,
                    smallBarsColorInt = smallBarsColor
                )
                val pointerColor = getColor(R.styleable.SpeedometerView_pointerColor, defPointerColor)
                val pointerWidthPx = getDimension(R.styleable.SpeedometerView_pointerWidth, defPointerWidthPx)
                pointerView = PointerView(
                    context,
                    pointerWidthPx = pointerWidthPx,
                    pointerColor = pointerColor
                )
                getDimension(R.styleable.SpeedometerView_pointerHeight, -1f).let { pHeight ->
                    if (pHeight != -1f) {
                        pointerView.pointerHeightPx = pHeight
                    }
                }
                maxSpeed = maxSpeedKmh
                addView(backgroundView)
                addView(pointerView)
            } finally {
                recycle()
            }
        }
    }


    private fun initDefaults(context: Context) {
        defSmallBarsWidthPx = resources.getDimension(R.dimen.small_bar_width)
        defSmallBarsHeightPx = resources.getDimension(R.dimen.small_bar_height)
        defBigBarsWidthPx = resources.getDimension(R.dimen.big_bar_width)
        defBigBarsHeightPx = resources.getDimension(R.dimen.big_bar_height)
        defAngleMinDegrees = ANGLE_MIN
        defAngleMaxDegrees = ANGLE_MAX
        defTextSizePx = resources.getDimension(R.dimen.speedometer_text_size)
        defMaxSpeedKmh = MAX_SPEED
        defMinSpeedKmh = MIN_SPEED
        defBigSpeedStepKmh = BIG_SPEED_STEP
        defSmallSpeedStepKmh = SMALL_SPEED_STEP
        defTextColor = ContextCompat.getColor(context, R.color.colorAccent)
        defBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        defBarsColor = Color.GREEN
        defPointerColor = ContextCompat.getColor(context, R.color.pointerColor)
        defPointerWidthPx = resources.getDimension(R.dimen.pointer_width)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    fun setSpeed(speedKmh: Float) {
        pointerView.angleDegrees = speedToAngle(speedKmh)
    }

    private fun speedToAngle(speedKmh: Float): Float {
        return (angleMaxDegrees - angleMinDegrees) * speedKmh / maxSpeed + angleMinDegrees
    }
}