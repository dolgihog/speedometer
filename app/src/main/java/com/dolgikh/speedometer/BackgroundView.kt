package com.dolgikh.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import java.lang.StringBuilder
import kotlin.math.absoluteValue

class BackgroundView : View {

    companion object {
        private const val TAG = "BackgroundView"
        private const val SPACE_LETTER = " "
        private const val MAX_TEXT = "000"
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
    private val textPaint: Paint
    private val textRect: Rect = Rect()
    private val stringBuilder: StringBuilder = StringBuilder()


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
        smallBarsAngleOffsetDegrees: Float,
        @ColorInt textColor: Int,
        textSizePx: Float
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
        this.textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textSize = textSizePx
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3f
            textAlign = Paint.Align.CENTER
        }
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
        drawText(canvas, centerX, centerY, radius)
    }

    private fun drawText(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float
    ) {
        canvas.save()
        canvas.rotate(angleMinDegrees, centerX, centerY)
        var angle = angleMinDegrees
        var value = 0f
        textPaint.getTextBounds(MAX_TEXT, 0, MAX_TEXT.length, textRect)
        val pivotY = centerY + 6 * radius / 7 - textRect.height() / 2
        val pivotX = centerX
        while (angle <= angleMaxDegrees) {
            canvas.save()
            canvas.rotate(-angle, pivotX, pivotY)
            stringBuilder.clear()
            stringBuilder.append(value.toInt())
            while (stringBuilder.length < 3) {
                stringBuilder.append(SPACE_LETTER)
            }
            canvas.drawText(
                stringBuilder.toString(),
                pivotX,
                pivotY + textRect.height() / 2,
                textPaint
            )
            canvas.restore()
            canvas.rotate(angleStepDegrees, centerX, centerY)
            angle += angleStepDegrees
            value += angleStepDegrees
        }
        canvas.restore()
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