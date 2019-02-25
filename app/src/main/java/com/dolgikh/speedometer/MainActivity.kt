package com.dolgikh.speedometer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.dolgikh.speedometer.data.DataRepository
import kotlinx.android.synthetic.main.activity_main.*
import android.util.LogPrinter
import android.util.Log


class MainActivity : AppCompatActivity(), DataRepository.SpeedListener {

    companion object {
        private const val UPDATE_VIEW_CODE = 1
        private const val LOG_TAG = "MainActivity"
    }

    private val dataRepository: DataRepository =
        DataRepository()
    private val immersiveModeDelegate: ImmersiveModeDelegate = ImmersiveModeDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersiveModeDelegate.hideNavBar(window)
        setContentView(R.layout.activity_main)
        val looper = mainLooper
        looper.setMessageLogging(LogPrinter(Log.DEBUG, "Looper"))
    }

    override fun onStart() {
        super.onStart()
        dataRepository.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        dataRepository.unsubscribe()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        immersiveModeDelegate.onWindowFocusChanged(hasFocus, window)
    }

    private val handler = Handler(Looper.getMainLooper()) { msg: Message? ->
        if (msg?.what == UPDATE_VIEW_CODE) {
            val speedKmh = msg.obj as Float
            Log.d(LOG_TAG, "handleMessage: thread = ${Thread.currentThread()}")
            speedometerView.setSpeed(speedKmh)
            return@Handler true
        } else {
            return@Handler false
        }
    }

    override fun onSpeedGenerated(speedKmh: Float) {
        Log.d(LOG_TAG, "onSpeedGenerated: thread = ${Thread.currentThread()}")
        handler.obtainMessage(UPDATE_VIEW_CODE, speedKmh).sendToTarget()
    }
}
