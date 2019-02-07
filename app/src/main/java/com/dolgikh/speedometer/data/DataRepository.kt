package com.dolgikh.speedometer.data

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.dolgikh.speedometer.speedproducer.MockSpeedProducer

class DataRepository : ClientHandler.Callback {

    companion object {
        private const val LOG_TAG = "DataRepository"
    }

    interface SpeedListener {
        fun onSpeedGenerated(speedKmh: Float)
    }

    private var client: Activity? = null
    private var speedListener: SpeedListener? = null
    private var bound: Boolean = false

    private var messenger: Messenger = Messenger(ClientHandler(this))

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val message = Message.obtain()
            message.replyTo = messenger
            Messenger(service).send(message)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(LOG_TAG, "Service has unexpectedly disconnected")
        }
    }

    fun <T> subscribe(client: T) where T : Activity, T : SpeedListener {
        this.client = client
        this.speedListener = client
        client.bindService(Intent(client, MockSpeedProducer::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    fun unsubscribe() {
        if (bound) client?.unbindService(connection)
        client = null
        speedListener = null
        bound = false
    }

    override fun call(speed: Float) {
        speedListener?.onSpeedGenerated(speed)
    }
}