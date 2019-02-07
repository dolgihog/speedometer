package com.dolgikh.speedometer.speedproducer

import android.os.Handler
import android.os.Message
import android.os.Messenger

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