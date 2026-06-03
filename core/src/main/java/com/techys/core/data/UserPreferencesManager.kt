package com.techys.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Extension to create DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesManager @Inject constructor(@param:ApplicationContext val context: Context) {

    companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val EYE_TIMER_END_SOUND = stringPreferencesKey("end_sound_eye")
        private val FOCUS_TIMER_END_SOUND = stringPreferencesKey("end_sound_focus")
        private val QUICK_TIMER_END_SOUND = stringPreferencesKey("end_sound_quick")
    }

    // Read: Flow<Boolean> - emits value whenever preferences change
    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

    val eyeTimerEndSound: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[EYE_TIMER_END_SOUND] ?: ""
        }
    val focusTimerEndSound: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[FOCUS_TIMER_END_SOUND] ?: ""
        }
    val quickTimerEndSound: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[QUICK_TIMER_END_SOUND] ?: ""
        }

    suspend fun setEyeTimerEndSound(uri: String){
        context.dataStore.edit { preferences ->
            preferences[EYE_TIMER_END_SOUND] = uri
        }
    }
    suspend fun seFocusTimerEndSound(uri: String){
        context.dataStore.edit { preferences ->
            preferences[FOCUS_TIMER_END_SOUND] = uri
        }
    } suspend fun setQuickTimerEndSound(uri: String){
        context.dataStore.edit { preferences ->
            preferences[QUICK_TIMER_END_SOUND] = uri
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun resetOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = false
        }
    }
}