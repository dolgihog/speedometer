package com.dolgikh.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

class PointerView : View {

    companion object {
        private const val LOG_TAG = "PointerView"
    }

    private val pointerWidthPx: Float
    private val paint: Paint
    var angleDegrees: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    constructor(
        context: Context,
        @ColorInt pointerColor: Int,
        pointerWidthPx: Float
    ) : super(context) {
        this.pointerWidthPx = pointerWidthPx
        this.paint = Paint().apply { color = pointerColor }
    }

//    constructor(context: Context, attrs: AttributeSet) : super(context) {
//
//    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(LOG_TAG, "onDraw")
        super.onDraw(canvas)
        if (canvas == null) return
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f
        drawPointer(canvas, centerX = centerX, centerY = centerY, radius = radius)
    }

    private fun drawPointer(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        canvas.rotate(angleDegrees, centerX, centerY)
        canvas.drawRect(
            centerX - pointerWidthPx / 2,
            centerY + radius,
            centerX + pointerWidthPx / 2,
            centerY,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}