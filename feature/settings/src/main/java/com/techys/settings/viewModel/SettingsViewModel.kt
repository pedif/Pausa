package com.techys.settings.viewModel

import androidx.lifecycle.ViewModel
import com.techys.settings.model.SettingsState
import com.techys.settings.model.SoundItem
import com.techys.settings.util.TimerSoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val soundManager: TimerSoundManager) : ViewModel() {

    private var _state = MutableStateFlow<SettingsState>(SettingsState())
    val state: StateFlow<SettingsState>
        get() = _state.asStateFlow()

    init {
        _state.update {
            val eyeSound = soundManager.getSoundTitle(
                TimerSoundManager.KEY_POMODORO_SOUND
            )

            val focusSound = soundManager.getSoundTitle(
                TimerSoundManager.KEY_ZEN_MODE_SOUND
            )

            val quickSound = soundManager.getSoundTitle(
                TimerSoundManager.KEY_QUICK_TIMER_SOUND
            )

            it.copy(
                sounds = soundManager.getAvailableSounds(),
                eyeSoundItem = SoundItem(title = eyeSound),
                focusSoundItem = SoundItem(title = focusSound),
                quickSoundItem = SoundItem(title = quickSound)
            )
        }
    }
}