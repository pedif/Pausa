package com.techys.pausa.tmp.focus

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.pausa.tmp.focus.model.FocusState
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.service.PausaService
import com.techys.pausa.tmp.util.getElapsedTimeLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FocusViewModel : ViewModel() {

    val _state = MutableStateFlow<FocusState>(FocusState())

    val state: StateFlow<FocusState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            PausaService.state.collect { pausaState ->
                val focusTimer = pausaState.focusTimer
                if (focusTimer.state == TimerStateType.STOPPED)
                    _state.update {
                        FocusState()
                    }
                else
                    _state.update {
                        it.copy(
                            time = focusTimer.current,
                            progress = focusTimer.progress,
                        )
                    }
            }
        }
    }

    fun onTimeChanged(minutes: Int) {
        val timerLabel = getElapsedTimeLabel(minutes * 60L)
        _state.update {
            it.copy(
                time = minutes * 60,
                timeLabel = timerLabel
            )
        }
    }
}