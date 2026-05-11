package com.techys.pausa

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techys.core.model.TimerType
import com.techys.core.service.PausaService
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.NeonBlue
import com.techys.pausa.navigation.NavHost
import com.techys.pausa.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val _state = MutableStateFlow<NavRoutes>(NavRoutes.Home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        checkIntentForDeepLink(intent, false)
        setContent {
            AppTheme {
                val dest by _state.collectAsState()
                AppNavigation(dest = dest) {
                    _state.value = it
                }
            }
        }

    }

    private fun startService() {
        val intent = Intent(this, PausaService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkIntentForDeepLink(intent, true)
    }

    private fun checkIntentForDeepLink(intent: Intent, activityPresent: Boolean) {

        val action = intent.action ?: return
        if (action == TimerType.Focus.id)
            _state.value = NavRoutes.Focus
        else if (action == TimerType.Quick.id) {
            if (activityPresent)
                _state.value = NavRoutes.Quick
            else
                _state.value = NavRoutes.QuickDialog
        }
    }
}

@Composable
fun AppNavigation(
    dest: NavRoutes,
    onDestChanged: (NavRoutes) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() }) { innerPadding ->
        NavHost(
            dest = dest,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NeonBlue.copy(alpha = 0.1f)),
        title = { Text(text = "Pausa") },
        actions = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        AppNavigation(NavRoutes.Home)
    }
}