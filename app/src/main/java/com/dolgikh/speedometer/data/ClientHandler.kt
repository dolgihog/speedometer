package com.dolgikh.speedometer.data

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class ClientHandler(listener: Callback) : Handler() {

    interface Callback {
        fun call(speed: Float)
    }

    private val weakReference: WeakReference<Callback> = WeakReference(listener)

    companion object {
        const val MSG_SPEED = 1
        const val KEY_SPEED = "KEY_SPEED"
        const val LOG_TAG = "ClientHandler"
    }

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        if (msg?.what == MSG_SPEED) {
            val speed = msg.data?.getFloat(KEY_SPEED)
            weakReference.get()?.call(speed ?: 0f)
        }
    }
}