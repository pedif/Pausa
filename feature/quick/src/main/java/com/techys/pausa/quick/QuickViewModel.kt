package com.techys.pausa.quick

import android.content.Context
import com.techys.core.receiver.PausaServiceReceiver
import com.techys.core.util.TimerConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuickViewModel(val context: Context) {

    private var _state = MutableStateFlow<QuickState>(QuickState())
    val state: StateFlow<QuickState>
        get() = _state.asStateFlow()

    init {

    }

    fun onTitleChanged(title: String) {
        _state.update {
            it.copy(title = title)
        }
    }

    fun onTimePicked(timeIndex: Int) {
        with(state.value) {
            PausaServiceReceiver.sendTimerInfoUpdateBroadcast(
                context = context,
                id = TimerConstants.QUICK_TIMER_ID,
                title = this.title,
                interval = this.timeList[timeIndex]
            )
        }
    }

}