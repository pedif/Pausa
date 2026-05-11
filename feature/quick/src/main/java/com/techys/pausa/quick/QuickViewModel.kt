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

@HiltViewModel
class QuickViewModel @Inject constructor(@param:ApplicationContext val context: Context): ViewModel() {

    private var _state = MutableStateFlow<QuickState>(QuickState())
    val state: StateFlow<QuickState>
        get() = _state.asStateFlow()

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