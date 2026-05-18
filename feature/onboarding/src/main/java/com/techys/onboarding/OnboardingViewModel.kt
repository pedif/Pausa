package com.techys.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techys.core.data.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(val preferencesManager: UserPreferencesManager): ViewModel() {

    val onboardingCompleted: StateFlow<Boolean> = preferencesManager.onboardingCompleted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesManager.setOnboardingCompleted()
        }
    }
}