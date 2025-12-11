package com.gabof92.hebrewaudiobible.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object DefaultValues {
    const val BOOK: Int = 19
    const val CHAPTER: Int = 1
    const val SHOW_HEBREW: Boolean = true
    const val SHOW_TRANSLITERATION: Boolean = true
    const val SHOW_ENGLISH: Boolean = true
}

object PreferencesKeys{
    val BOOK = intPreferencesKey("book")
    val CHAPTER = intPreferencesKey("chapter")
    val SHOW_HEBREW = booleanPreferencesKey("show_hebrew")
    val SHOW_TRANSLITERATION = booleanPreferencesKey("show_transliteration")
    val SHOW_ENGLISH = booleanPreferencesKey("show_english")
}