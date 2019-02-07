package com.dolgikh.speedometer.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import kotlin.math.sin

@SuppressLint("ViewConstructor")
class BackgroundView(
    context: Context,
    private val smallBarsWidthPx: Float,
    private val smallBarsHeightPx: Float,
    private val bigBarsWidthPx: Float,
    private val bigBarsHeightPx: Float,
    private val angleMinDegrees: Float,
    private val angleMaxDegrees: Float,
    private val maxSpeedKmh: Float,
    private val minSpeedKmh: Float,
    private val bigSpeedStepKmh: Float,
    private val smallSpeedStepKmh: Float,
    textSizePx: Float,
    textColorInt: Int,
    backgroundColorInt: Int,
    bigBarsColorInt: Int,
    smallBarsColorInt: Int
) : View(context) {

    companion object {
        private const val TAG = "BackgroundView"
    }

    private val backgroundPaint: Paint = Paint()
    private val bigBarsPaint: Paint = Paint()
    private val smallBarPaint: Paint = Paint()
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 3f
        textAlign = Paint.Align.CENTER
    }
    private val bigStepAngleDegrees: Float
    private val smallStepAngleDegrees: Float
    private val textRect: Rect = Rect()

    init {
        this.textPaint.textSize = textSizePx
        this.textPaint.color = textColorInt
        this.backgroundPaint.color = backgroundColorInt
        this.bigBarsPaint.color = bigBarsColorInt
        this.smallBarPaint.color = smallBarsColorInt
        val speedInAngle = speedToAngle(this.angleMaxDegrees, this.angleMinDegrees, this.maxSpeedKmh, this.minSpeedKmh)
        this.bigStepAngleDegrees = speedInAngle * this.bigSpeedStepKmh
        this.smallStepAngleDegrees = speedInAngle * this.smallSpeedStepKmh
    }

    private fun speedToAngle(
        angleMaxDegrees: Float,
        angleMinDegrees: Float,
        maxSpeedKmh: Float,
        minSpeedKmh: Float
    ) = (angleMaxDegrees - angleMinDegrees) / (maxSpeedKmh - minSpeedKmh)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val radius = Math.min(width, height) / 2f
        val centerX = width / 2f
        val centerY = height / 2f
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        drawSmallMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - smallBarsHeightPx, backgroundPaint)
        drawBigMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - bigBarsHeightPx, backgroundPaint)
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
        while (angle <= angleMaxDegrees) {
            canvas.save()
            val stringValue = value.toInt().toString()
            textPaint.getTextBounds(stringValue, 0, stringValue.length, textRect)
            val pivotY = centerY + 6 * radius / 7 - textRect.height() / 2
            canvas.rotate(-angle, centerX, pivotY)
            val xPoint = centerX + sin(Math.toRadians(angle.toDouble())).toFloat() * textRect.width()
            val yPoint = pivotY + textRect.height() / 2 + sin(Math.toRadians(angle - 90.toDouble())).toFloat() * textRect.height()
            canvas.drawText(
                stringValue,
                xPoint,
                yPoint,
                textPaint
            )
            canvas.restore()
            canvas.rotate(bigStepAngleDegrees, centerX, centerY)
            angle += bigStepAngleDegrees
            value += bigSpeedStepKmh
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
                centerX - bigBarsWidthPx / 2,
                centerY + radius,
                centerX + bigBarsWidthPx / 2,
                centerY,
                bigBarsPaint
            )
            canvas.rotate(bigStepAngleDegrees, centerX, centerY)
            angle += bigStepAngleDegrees
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
        canvas.rotate(angleMinDegrees, centerX, centerY)
        var angle = angleMinDegrees
        while (angle <= angleMaxDegrees) {
            canvas.drawRect(
                centerX - smallBarsWidthPx / 2,
                centerY + radius,
                centerX + smallBarsWidthPx / 2,
                centerY,
                smallBarPaint
            )
            canvas.rotate(smallStepAngleDegrees, centerX, centerY)
            angle += smallStepAngleDegrees
        }
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}