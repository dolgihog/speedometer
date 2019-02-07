package com.dolgikh.speedometer.speedproducer

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.dolgikh.speedometer.data.ClientHandler
import kotlin.math.absoluteValue
import kotlin.math.sin

class MockSpeedProducer : Service() {

    companion object {
        const val MAX_SPEED = 180
        private const val LOG_TAG = "MockSpeedProducer"
    }

    private val handler: ServerHandler = ServerHandler()

    @Volatile
    private var isDestroyed: Boolean = false

    private val messenger: Messenger = Messenger(handler)


    override fun onBind(intent: Intent): IBinder = messenger.binder

    override fun onCreate() {
        Log.d(LOG_TAG, "onCreate")
        super.onCreate()
        Thread(Runnable { startFunc() }).start()
    }

    private val bundle: Bundle = Bundle()

    private fun startFunc() {
        while (!isDestroyed) {
            Thread.sleep(1)
            val sin = sin(System.currentTimeMillis() / 10000.toDouble())
            val speed = sin.absoluteValue * MAX_SPEED
            val message = Message.obtain(null, ClientHandler.MSG_SPEED)
            bundle.putFloat(ClientHandler.KEY_SPEED, speed.toFloat())
            message.data = bundle
            handler.sendSpeedMessage(message)
        }
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
        isDestroyed = true
    }
}
