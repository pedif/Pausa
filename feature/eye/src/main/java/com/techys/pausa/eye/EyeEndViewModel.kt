package com.techys.pausa.eye

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.core.notification.NotificationManager
import com.techys.core.service.PausaService
import com.techys.core.util.TimerConstants
import com.techys.pausa.eye.model.EyeEndState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EyeEndViewModel @Inject constructor(val notificationManager: NotificationManager) :
    ViewModel() {

    private var _state = MutableStateFlow(EyeEndState())
    val state: StateFlow<EyeEndState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            PausaService.state.collect { pausaState ->
                val eyeTimer = pausaState.eyeTimer
                _state.update {
                    it.copy(
                        visibilityDuration = eyeTimer.max,
                        current = eyeTimer.remainingTime,
                        title = eyeTimer.title
                    )
                }
            }
        }
    }

    fun onTimerEnd() {

    }
}