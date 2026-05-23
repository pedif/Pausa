package com.techys.pausa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.core.data.UserPreferencesManager
import com.techys.pausa.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val userPreferencesManager: UserPreferencesManager) : ViewModel() {


    private val _state = MutableStateFlow<NavRoutes>(NavRoutes.None)
    val state: StateFlow<NavRoutes>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val hasFinishedOnboarding = userPreferencesManager.onboardingCompleted.first()
            if(!hasFinishedOnboarding)
                _state.value = NavRoutes.Onboarding
            else
                _state.value = NavRoutes.Home
        }
    }
    fun changeRoute(route: NavRoutes){
        _state.value = route
    }
}