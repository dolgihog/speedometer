package com.dolgikh.speedometer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

@SuppressLint("ViewConstructor")
class PointerView(
    context: Context,
    pointerWidthPx: Float,
    @ColorInt private val pointerColor: Int
) : View(context) {

    companion object {
        private const val LOG_TAG = "PointerView"
    }

    private var defPointerHeight: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private val halfOfPointerWidth: Float = pointerWidthPx / 2
    var pointerHeightPx: Float? = null

    private val paint: Paint = Paint().apply {
        color = pointerColor
    }

    var angleDegrees: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val pointerHeightPx = this.pointerHeightPx ?: defPointerHeight
        drawPointer(canvas, centerX = centerX, centerY = centerY, pointerHeightPx = pointerHeightPx)
    }

    private fun drawPointer(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        pointerHeightPx: Float
    ) {
        canvas.rotate(angleDegrees, centerX, centerY)
        canvas.drawRect(
            centerX - halfOfPointerWidth,
            centerY + pointerHeightPx,
            centerX + halfOfPointerWidth,
            centerY,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        defPointerHeight = min(measuredWidth, measuredHeight) / 2f
        centerX = measuredWidth / 2f
        centerY = measuredHeight / 2f
    }
}