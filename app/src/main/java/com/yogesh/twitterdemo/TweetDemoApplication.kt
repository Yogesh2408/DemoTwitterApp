package com.yogesh.twitterdemo

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class TweetDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}