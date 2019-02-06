package com.dolgikh.speedometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.view.View


class MainActivity : AppCompatActivity(), MockSpeedGenerator.SpeedListener {

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val MAX_SPEED = 180
    }

    private val mockSpeedGenerator: MockSpeedGenerator = MockSpeedGenerator(maxSpeed = MAX_SPEED)

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
        mockSpeedGenerator.setListener(this)
        mockSpeedGenerator.start()
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

    override fun onSpeedGenerated(speedKmh: Float) = speedometerView.setSpeed(speedKmh)
}
