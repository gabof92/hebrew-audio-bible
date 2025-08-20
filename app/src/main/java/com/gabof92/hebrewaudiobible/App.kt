package com.gabof92.hebrewaudiobible

import android.app.Application
import com.gabof92.hebrewaudiobible.database.BibleDatabase

class App : Application() {
    val database: BibleDatabase by lazy {BibleDatabase.getDatabase(this)}
    override fun onCreate() {
        super.onCreate()
    }
}