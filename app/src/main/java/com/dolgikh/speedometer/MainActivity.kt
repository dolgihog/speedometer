package com.dolgikh.speedometer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ClientHandler.Callback {

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val MAX_SPEED = 180
    }


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

//    private val mockSpeedGenerator: MockSpeedGenerator = MockSpeedGenerator(maxSpeed = MAX_SPEED)

    private val sdkVersion: Int = android.os.Build.VERSION.SDK_INT
    private var immersiveFlags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavBar()
        setContentView(R.layout.activity_main)
//        mockSpeedGenerator.setListener(this)
//        mockSpeedGenerator.start()
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, MockSpeedProducer::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (bound) unbindService(connection)
        bound = false
    }

    private fun hideNavBar() {
        if (sdkVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = immersiveFlags
            val decorView = window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = immersiveFlags
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) window.decorView.systemUiVisibility = immersiveFlags
    }

    override fun call(speed: Float) {
        speedometerView.setSpeed(speed)
    }
}
