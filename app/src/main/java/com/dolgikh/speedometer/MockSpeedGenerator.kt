package com.dolgikh.speedometer

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.lang.ref.WeakReference
import kotlin.math.absoluteValue
import kotlin.math.sin

class MockSpeedGenerator(
    private val maxSpeed: Int = 240
) : Handler() {

    interface SpeedListener {
        fun onSpeedGenerated(speedKmh: Float)
    }

    companion object {
        private const val LOG_TAG = "MockSpeedGenerator"
        private const val KEY_SPEED = "KEY_SPEED"
    }

    private var weakView: WeakReference<SpeedListener>? = null

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        val speedKmh: Float = msg?.data?.getFloat(KEY_SPEED) ?: 0f
        Log.d(LOG_TAG, "thread = ${Thread.currentThread().name}, data = $speedKmh")
        weakView?.let { reference ->
            reference.get()?.onSpeedGenerated(speedKmh)
        }
    }

    fun setListener(listener: SpeedListener) {
        weakView = WeakReference(listener)
    }

    fun start() {
        Thread(Runnable { startFunc() }).start()
    }

    private val bundle: Bundle = Bundle()

    private fun startFunc() {
        while (true) {
            Thread.sleep(1)
            val sin = sin(System.currentTimeMillis() / 10000.toDouble())
            val speed = sin.absoluteValue * maxSpeed
            val message = obtainMessage()
            bundle.putFloat(KEY_SPEED, speed.toFloat())
            message.data = bundle
            sendMessage(message)
        }
    }
}
