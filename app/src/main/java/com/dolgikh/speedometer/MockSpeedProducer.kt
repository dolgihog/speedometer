package com.dolgikh.speedometer

import android.app.Service
import android.content.Intent
import android.os.*
import kotlin.math.absoluteValue
import kotlin.math.sin

class MockSpeedProducer : Service() {

    companion object {
        const val MAX_SPEED = 180
    }

    class ServerHandler : Handler() {

        private var clientMessenger: Messenger? = null

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            clientMessenger = msg?.replyTo
        }

        fun sendSpeedMessage(speedMessage: Message) {
            clientMessenger?.send(speedMessage)
        }
    }

    private val handler: ServerHandler = ServerHandler()

    @Volatile
    private var isDestroyed: Boolean = false

    private val messenger: Messenger = Messenger(handler)


    override fun onBind(intent: Intent): IBinder = messenger.binder

    override fun onCreate() {
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
            message.data = bundle
            bundle.putFloat(ClientHandler.KEY_SPEED, speed.toFloat())
            message.data = bundle
            handler.sendSpeedMessage(message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
    }
}
