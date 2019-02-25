package com.dolgikh.speedometer.speedproducer

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.dolgikh.speedometer.IMyAidlCallback
import com.dolgikh.speedometer.IMyAidlInterface
import com.dolgikh.speedometer.data.ClientHandler
import java.util.concurrent.atomic.AtomicReference
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
    private val atomicReference: AtomicReference<IMyAidlCallback> = AtomicReference()


    override fun onBind(intent: Intent): IBinder = object : IMyAidlInterface.Stub() {
        override fun subscribe(callback: IMyAidlCallback?) {
            atomicReference.set(callback)
            Log.d(LOG_TAG, "subscribe: thread = ${Thread.currentThread()}")
        }
    }

    override fun onCreate() {
        Log.d(LOG_TAG, "onCreate")
        super.onCreate()
        Thread(Runnable { startFunc() }).start()
    }

    private val bundle: Bundle = Bundle()

    private fun startFunc() {
        while (!isDestroyed) {
            Thread.sleep(100)
            val sin = sin(System.currentTimeMillis() / 10000.toDouble())
            val speed = sin.absoluteValue * MAX_SPEED
            atomicReference.get()?.handleSpeed(speed.toFloat())
        }
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
        isDestroyed = true
    }
}
