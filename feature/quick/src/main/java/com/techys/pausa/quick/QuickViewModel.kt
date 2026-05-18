package com.techys.pausa.quick

import android.content.Context
import androidx.lifecycle.ViewModel
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.TimerConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.techys.pausa.core.R as coreR

@HiltViewModel
class QuickViewModel @Inject constructor(@param:ApplicationContext val context: Context) :
    ViewModel() {

    private var _state = MutableStateFlow<QuickState>(QuickState())
    val state: StateFlow<QuickState>
        get() = _state.asStateFlow()

    init {
        _state.update {
            it.copy(title = context.getString(coreR.string.quick_timer_default_title))
        }
    }

    fun onTitleChanged(title: String) {
        _state.update {
            it.copy(title = title)
        }
    }

    fun onDurationChanged(duration: String) {
        var errorMessage = ""
        val time = duration.toIntOrNull()
        if (duration.isEmpty())
            errorMessage = context.getString(R.string.quick_error_duration_zero)
        else if (time == null || time > TimerConstants.QUICK_TIMER_MAX_INTERVAL)
            errorMessage = context.getString(R.string.quick_error_duration_too_big)
        else if (time < 1)
            errorMessage = context.getString(R.string.quick_error_duration_zero)

        _state.update {
            if (time != null) {
                it.copy(
                    durationErrorMessage = errorMessage,
                    duration = time.coerceAtLeast(1)
                )
            } else
                it.copy(durationErrorMessage = errorMessage)
        }
    }

    fun onTimePicked(timeInMinutes: Int) {
        with(state.value) {
            PausaServiceReceiver.sendTimerInfoUpdateBroadcast(
                context = context,
                id = TimerConstants.QUICK_TIMER_ID,
                title = this.title.ifEmpty { context.getString(coreR.string.quick_timer_default_title) },
                interval = timeInMinutes * 60,
                shouldStartImmediately = true
            )
        }
    }

}