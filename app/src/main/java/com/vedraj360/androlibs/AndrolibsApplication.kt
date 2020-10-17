package com.vedraj360.androlibs

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndrolibsApplication : Application() {
    init {
        System.loadLibrary("native-lib")
    }
}