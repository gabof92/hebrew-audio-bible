package com.gabof92.hebrewaudiobible.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//Extension property
private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

data class UserPreferences(
    val book: Int,
    val chapter: Int,
    val showHebrew: Boolean,
    val showTransliteration: Boolean,
    val showEnglish: Boolean,
)

class PreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore
    val userPreferences: Flow<UserPreferences> = dataStore.data.map { data ->
        val book = data[PreferencesKeys.BOOK] ?: DefaultValues.BOOK
        val chapter = data[PreferencesKeys.CHAPTER] ?: DefaultValues.CHAPTER
        val showHebrew = data[PreferencesKeys.SHOW_HEBREW] ?: DefaultValues.SHOW_HEBREW
        val showTransliteration = data[PreferencesKeys.SHOW_TRANSLITERATION] ?: DefaultValues.SHOW_TRANSLITERATION
        val showEnglish = data[PreferencesKeys.SHOW_ENGLISH] ?: DefaultValues.SHOW_ENGLISH
        UserPreferences(book, chapter, showHebrew, showTransliteration, showEnglish)
    }

    suspend fun updateChapter(book: Int, chapter: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BOOK] = book
            preferences[PreferencesKeys.CHAPTER] = chapter
        }
    }
    suspend fun updateTextVisibility(showHebrew: Boolean?, showTransliteration: Boolean?, showEnglish: Boolean?){
        showHebrew?.let { updateShowHebrew(it) }
        showTransliteration?.let { updateShowTransliteration(it) }
        showEnglish?.let { updateShowEnglish(it) }
    }

    suspend fun updateShowHebrew(showHebrew: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SHOW_HEBREW] = showHebrew }
    }
    suspend fun updateShowTransliteration(showTransliteration: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SHOW_TRANSLITERATION] = showTransliteration }
    }
    suspend fun updateShowEnglish(showEnglish: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SHOW_ENGLISH] = showEnglish }
    }
}