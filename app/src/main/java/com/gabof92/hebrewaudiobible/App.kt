package com.gabof92.hebrewaudiobible

import android.app.Application
import com.gabof92.hebrewaudiobible.data.database.BibleDatabase

class App : Application() {
    val database: BibleDatabase by lazy { BibleDatabase.Companion.getDatabase(this)}
    override fun onCreate() {
        super.onCreate()
    }
}