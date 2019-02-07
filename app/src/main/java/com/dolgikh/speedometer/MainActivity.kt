package com.dolgikh.speedometer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dolgikh.speedometer.data.DataRepository
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DataRepository.SpeedListener {

    companion object {
        private const val LOG_TAG = "MainActivity"
    }

    private val dataRepository: DataRepository =
        DataRepository()
    private val immersiveModeDelegate: ImmersiveModeDelegate = ImmersiveModeDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersiveModeDelegate.hideNavBar(window)
        setContentView(R.layout.activity_main)
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

    override fun onSpeedGenerated(speedKmh: Float) = speedometerView.setSpeed(speedKmh)
}
