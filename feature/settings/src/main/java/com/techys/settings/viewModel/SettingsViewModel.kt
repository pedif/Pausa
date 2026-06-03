package com.techys.settings.viewModel

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.core.data.UserPreferencesManager
import com.techys.settings.model.SettingsState
import com.techys.settings.model.SoundItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    val context: Context
        get() = preferencesManager.context
    private var _state = MutableStateFlow<SettingsState>(SettingsState())
    val state: StateFlow<SettingsState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.eyeTimerEndSound,
                preferencesManager.focusTimerEndSound,
                preferencesManager.quickTimerEndSound
            ) { eyeSound, focusSound, quickSound ->
                _state.update {
                    it.copy(
                        eyeSoundItem = getSoundItemFromPath(eyeSound),
                        focusSoundItem = getSoundItemFromPath(focusSound),
                        quickSoundItem = getSoundItemFromPath(quickSound)
                    )
                }
            }.stateIn(viewModelScope)
        }
    }

    fun setEyeSound(uri: Uri) = viewModelScope.launch {
        preferencesManager.setEyeTimerEndSound(uri.toString())
    }

    fun setFocusSound(uri: Uri) = viewModelScope.launch {
        preferencesManager.seFocusTimerEndSound(uri.toString())
    }


    fun setQuickSound(uri: Uri) = viewModelScope.launch {
        preferencesManager.setQuickTimerEndSound(uri.toString())
    }

    private fun getSoundItemFromPath(path: String): SoundItem {
        val title = RingtoneManager.getRingtone(context, path.toUri()).getTitle(context)
        return SoundItem(
            uri = path.toUri(),
            title = title ?: " Default"
        )
    }

}