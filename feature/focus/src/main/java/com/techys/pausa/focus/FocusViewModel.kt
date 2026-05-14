package com.techys.pausa.focus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.core.model.TimerStateType
import com.techys.core.service.PausaService
import com.techys.core.util.TimeUtil
import com.techys.pausa.focus.model.FocusState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class FocusViewModel @Inject constructor() : ViewModel() {

    val _state = MutableStateFlow<FocusState>(FocusState())

    val state: StateFlow<FocusState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            PausaService.state.map { it.focusTimer }.distinctUntilChanged().collect { focusTimer ->
                    _state.update {
                        it.copy(
                            time = focusTimer.remainingTime,
                            progress = focusTimer.progress,
                            timeLabel = TimeUtil.getElapsedTimeLabel(focusTimer.remainingTime),
                            runningState = focusTimer.state
                        )
                    }
            }
        }
    }

    fun onTimeChanged(minutes: Int) {
        val timerLabel = TimeUtil.getElapsedTimeLabel(minutes * 60)
        _state.update {
            it.copy(
                time = minutes * 60,
                timeLabel = timerLabel
            )
        }
    }
}