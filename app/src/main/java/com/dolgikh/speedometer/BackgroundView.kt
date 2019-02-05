package com.dolgikh.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class BackgroundView : View {

    companion object {
        private const val TAG = "BackgroundView"
    }

    private val smallBarsWidth: Float
    private val smallBarsHeight: Float
    private val bigBarsWidth: Float
    private val bigBarsHeight: Float
    private val backgroundPaint: Paint
    private val barsPaint: Paint
    private val angleMaxDegrees: Float
    private val angleMinDegrees: Float
    private val angleStepDegrees: Float
    private val smallBarsAngleOffsetDegrees: Float

    constructor(
        context: Context,
        smallBarsWidthPx: Float,
        smallBarsHeightPx: Float,
        bigBarsWidthPx: Float,
        bigBarsHeightPx: Float,
        @ColorInt backgroundColor: Int,
        @ColorInt barsColor: Int,
        angleMinDegrees: Float,
        angleMaxDegrees: Float,
        angleStepDegrees: Float,
        smallBarsAngleOffsetDegrees: Float
    ) : super(context) {
        this.smallBarsWidth = smallBarsWidthPx
        this.smallBarsHeight = smallBarsHeightPx
        this.bigBarsWidth = bigBarsWidthPx
        this.bigBarsHeight = bigBarsHeightPx
        this.backgroundPaint = Paint().apply { color = backgroundColor }
        this.barsPaint = Paint().apply { color = barsColor }
        this.angleMaxDegrees = angleMaxDegrees
        this.angleMinDegrees = angleMinDegrees
        this.angleStepDegrees = angleStepDegrees
        this.smallBarsAngleOffsetDegrees = smallBarsAngleOffsetDegrees
    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        initDefaults()
//    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val radius = Math.min(width, height) / 2f
        val centerX = width / 2f
        val centerY = height / 2f
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        drawSmallMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - smallBarsHeight, backgroundPaint)
        drawBigMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - bigBarsHeight, backgroundPaint)
        Log.d(TAG, "onDraw")
    }

    private fun drawBigMarks(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float
    ) {
        canvas.save()
        canvas.rotate(angleMinDegrees, centerX, centerY)
        var angle = angleMinDegrees
        while (angle <= angleMaxDegrees) {
            canvas.drawRect(
                centerX - bigBarsWidth / 2,
                centerY + radius,
                centerX + bigBarsWidth / 2,
                centerY,
                barsPaint
            )
            canvas.rotate(angleStepDegrees, centerX, centerY)
            angle += angleStepDegrees
        }
        canvas.restore()
    }

    private fun drawSmallMarks(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float
    ) {
        canvas.save()
        canvas.rotate(angleMinDegrees + smallBarsAngleOffsetDegrees, centerX, centerY)
        var angle = angleMinDegrees + smallBarsAngleOffsetDegrees
        while (angle <= angleMaxDegrees - smallBarsAngleOffsetDegrees) {
            canvas.drawRect(
                centerX - smallBarsWidth / 2,
                centerY + radius,
                centerX + smallBarsWidth / 2,
                centerY,
                barsPaint
            )
            canvas.rotate(angleStepDegrees, centerX, centerY)
            angle += angleStepDegrees
        }
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}