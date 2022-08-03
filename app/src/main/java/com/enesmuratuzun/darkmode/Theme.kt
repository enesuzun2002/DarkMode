package com.enesmuratuzun.darkmode

import android.app.Application
import com.google.android.material.color.DynamicColors

class Theme: Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}