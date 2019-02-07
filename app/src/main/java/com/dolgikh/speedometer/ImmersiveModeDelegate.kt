package com.dolgikh.speedometer

import android.os.Build
import android.view.View
import android.view.Window

class ImmersiveModeDelegate {

    private var immersiveFlags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    private val sdkVersion: Int = android.os.Build.VERSION.SDK_INT


    fun hideNavBar(window: Window) {
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

    fun onWindowFocusChanged(hasFocus: Boolean, window: Window) {
        if (hasFocus) window.decorView.systemUiVisibility = immersiveFlags
    }
}