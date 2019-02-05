package com.dolgikh.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import kotlin.math.sin

class SpeedometerView : View {

    companion object {
        private const val KEY_ANGLE = "KEY_ANGLE"
        private const val TAG = "SpeedometerView"
        private const val ANGLE_MIN = 60f
        private const val ANGLE_MAX = 300f
        private const val ANGLE_STEP = 20f
        private const val ANGLE_OFFSET_FOR_SMALL_BARS = 10f
    }

    class MyHandler : Handler() {

        companion object {
            private val TAG = "MyHandler"
        }

        private var weakView: WeakReference<SpeedometerView>? = null

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Log.d(TAG, "thread = ${Thread.currentThread().name}, data = ${msg?.data?.getFloat(KEY_ANGLE)}")
            weakView?.let { reference ->
                reference.get()?.let { speedometerView ->
                    speedometerView.pointerAngle = msg?.data?.getFloat(KEY_ANGLE) ?: 0f
                    speedometerView.invalidate()
                }
            }
        }

        fun attachView(view: SpeedometerView) {
            weakView = WeakReference(view)
        }
    }

    private var smallBarsWidth: Float = 0f
    private var smallBarsHeight: Float = 0f
    private var bigBarsWidth: Float = 0f
    private var bigBarsHeight: Float = 0f
    private var isBackgroundDrawn: Boolean = false
    var pointerAngle: Float = 0f

    private val backgroundPaint: Paint = Paint()
    private val barsPaint: Paint = Paint()
    private val pointerPaint: Paint = Paint()
    private val myHandler: MyHandler = MyHandler()

    constructor(context: Context) : super(context) {
        initDefaults()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initDefaults()
    }

    private fun initDefaults() {
        smallBarsWidth = resources.getDimension(R.dimen.small_marks_width)
        smallBarsHeight = resources.getDimension(R.dimen.small_marks_height)
        bigBarsWidth = resources.getDimension(R.dimen.big_marks_width)
        bigBarsHeight = resources.getDimension(R.dimen.big_marks_height)
        backgroundPaint.apply {
            color = ContextCompat.getColor(context, android.R.color.holo_red_dark)
        }
        barsPaint.apply {
            color = ContextCompat.getColor(context, android.R.color.black)
        }
        pointerPaint.apply {
            color = ContextCompat.getColor(context, android.R.color.holo_green_dark)
        }

        Thread(Runnable { startFunc() }).start()
        myHandler.attachView(this)
    }

    private fun startFunc() {
        while (true) {
            Thread.sleep(1)
            val angle = System.currentTimeMillis() / 10000.toDouble() % 360
            val sin = sin(angle)
            Log.d(TAG, "time = ${System.currentTimeMillis().toFloat()}")
            Log.d(TAG, "angle = $angle, sin = $sin")
            val message = myHandler.obtainMessage()
            val bundle = Bundle().apply {
                putFloat(KEY_ANGLE, sin.toFloat() * 360)
            }
            message.data = bundle
            myHandler.sendMessage(message)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val radius = Math.min(width, height) / 2f
        val centerX = width / 2f
        val centerY = height / 2f
//        if (!isBackgroundDrawn) {
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        drawSmallMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - smallBarsHeight, backgroundPaint)
        drawBigMarks(canvas, centerX, centerY, radius)
        canvas.drawCircle(centerX, centerY, radius - bigBarsHeight, backgroundPaint)
//            isBackgroundDrawn = true
//        }
        drawPointer(canvas, centerX, centerY, radius)
    }

    private fun drawPointer(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val angle = if (pointerAngle < ANGLE_MIN) {
            ANGLE_MIN
        } else if (pointerAngle > ANGLE_MAX) {
            ANGLE_MAX
        } else {
            pointerAngle
        }
        canvas.rotate(angle, centerX, centerY)
        canvas.drawRect(
            centerX - bigBarsWidth / 2,
            centerY + radius,
            centerX + bigBarsWidth / 2,
            centerY,
            pointerPaint
        )
    }

    private fun drawBigMarks(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float
    ) {
        canvas.save()
        canvas.rotate(ANGLE_MIN, centerX, centerY)
        var angle = ANGLE_MIN
        while (angle <= ANGLE_MAX) {
            canvas.drawRect(
                centerX - bigBarsWidth / 2,
                centerY + radius,
                centerX + bigBarsWidth / 2,
                centerY,
                barsPaint
            )
            canvas.rotate(ANGLE_STEP, centerX, centerY)
            angle += ANGLE_STEP
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
        canvas.rotate(ANGLE_MIN + ANGLE_OFFSET_FOR_SMALL_BARS, centerX, centerY)
        var angle = ANGLE_MIN + ANGLE_OFFSET_FOR_SMALL_BARS
        while (angle <= ANGLE_MAX - ANGLE_OFFSET_FOR_SMALL_BARS) {
            canvas.drawRect(
                centerX - smallBarsWidth / 2,
                centerY + radius,
                centerX + smallBarsWidth / 2,
                centerY,
                barsPaint
            )
            canvas.rotate(ANGLE_STEP, centerX, centerY)
            angle += ANGLE_STEP
        }
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}