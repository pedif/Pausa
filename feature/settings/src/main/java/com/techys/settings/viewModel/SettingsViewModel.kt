package com.techys.settings.viewModel

import androidx.lifecycle.ViewModel
import com.techys.settings.model.SettingsState
import com.techys.settings.model.SoundItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private var _state = MutableStateFlow<SettingsState>(SettingsState())
    val state: StateFlow<SettingsState>
        get() = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                eyeSoundItem = SoundItem(title = "eyeSound"),
                focusSoundItem = SoundItem(title = "focusSound"),
                quickSoundItem = SoundItem(title = "quickSound")
            )
        }
    }
}