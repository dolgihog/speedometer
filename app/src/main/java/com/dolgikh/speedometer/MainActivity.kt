package com.dolgikh.speedometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.absoluteValue
import kotlin.math.sin

class MainActivity : AppCompatActivity(), MockSpeedGenerator.SpeedListener {

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val MAX_SPEED = 300
    }

    private val mockSpeedGenerator: MockSpeedGenerator = MockSpeedGenerator(maxSpeed = MAX_SPEED)

    private lateinit var speedometerView: SpeedometerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speedometerView = SpeedometerView(context = this, maxSpeed = MAX_SPEED)
        rootLayout.addView(speedometerView)
        mockSpeedGenerator.setListener(this)
        mockSpeedGenerator.start()
    }

    override fun onSpeedGenerated(speedKmh: Float) {
        speedometerView.setSpeed(speedKmh)
    }
}
