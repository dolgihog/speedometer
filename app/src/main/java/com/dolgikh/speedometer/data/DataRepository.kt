package com.dolgikh.speedometer.data

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.dolgikh.speedometer.IMyAidlCallback
import com.dolgikh.speedometer.speedproducer.MockSpeedProducer
import com.dolgikh.speedometer.IMyAidlInterface
import java.util.concurrent.atomic.AtomicReference


class DataRepository {

    companion object {
        private const val LOG_TAG = "DataRepository"
    }

    interface SpeedListener {
        fun onSpeedGenerated(speedKmh: Float)
    }

    private var client: Activity? = null
    private val atomicReference: AtomicReference<SpeedListener> = AtomicReference()
    private var bound: Boolean = false

    private var serviceApi: IMyAidlInterface? = null
    private var listener: IMyAidlCallback? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceApi = IMyAidlInterface.Stub.asInterface(service)
            listener = object : IMyAidlCallback.Stub() {
                override fun handleSpeed(speed: Float) {
                    Log.d(LOG_TAG, "handleSpeed: thread = ${Thread.currentThread().name}")
                    atomicReference.get()?.onSpeedGenerated(speed)
                }
            }
            bound = true
            serviceApi?.subscribe(listener)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.d(LOG_TAG, "onServiceDisconnected: reconnect")
            client?.let { client ->
                client.bindService(Intent(client, MockSpeedProducer::class.java), this, Context.BIND_AUTO_CREATE)
            }
        }
    }

    fun <T> subscribe(client: T) where T : Activity, T : SpeedListener {
        this.client = client
        atomicReference.set(client)
        client.bindService(Intent(client, MockSpeedProducer::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    fun unsubscribe() {
        if (bound) client?.unbindService(connection)
        client = null
        atomicReference.set(null)
        bound = false
    }
}