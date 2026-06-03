package com.techys.pausa.end

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.techys.core.model.TimerType
import com.techys.core.service.PausaTimerEndService
import com.techys.designsystem.theme.AppTheme
import com.techys.pausa.end.navigation.EndNavHost
import com.techys.pausa.end.navigation.EndNavRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimerEndActivity : ComponentActivity() {

    private var _state = MutableStateFlow<EndNavRoute>(EndNavRoute.Eye)
    private val state: StateFlow<EndNavRoute>
        get() = _state.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        checkIntentForDeepLink(intent)
        observeEndState()
        setContent {
            AppTheme {
                val dest by state.collectAsState()
                MessageComponent(dest)
            }
        }
    }

    private fun observeEndState() {
        lifecycleScope.launch {
            PausaTimerEndService.runningState.collect { isRunning ->
                if (!isRunning)
                    this@TimerEndActivity.finish()
            }
        }
    }

    private fun checkIntentForDeepLink(intent: Intent) {
        val action = intent.action ?: return
        when (action) {
            TimerType.Focus.id -> _state.value = EndNavRoute.Focus
            TimerType.Quick.id -> _state.value = EndNavRoute.Quick
            TimerType.EyeBreak.id -> _state.value == EndNavRoute.Eye
            else -> finish()
        }
    }
}


@Composable
fun MessageComponent(dest: EndNavRoute, modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        EndNavHost(
            dest = dest,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AppTheme {
        MessageComponent(EndNavRoute.Eye)
    }
}