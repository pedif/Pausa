package com.techys.pausa

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techys.core.model.TimerType
import com.techys.core.service.PausaService
import com.techys.designsystem.theme.AppTheme
import com.techys.pausa.navigation.NavHost
import com.techys.pausa.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        checkIntentForDeepLink(intent, false)
        setContent {
            AppTheme {
                val dest by viewModel.state.collectAsState()
                AppNavigation(
                    dest = dest,
                    onDestChanged = {
                        viewModel.changeRoute(it)
                    }
                )
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
        var route: NavRoutes? = null
        if (action == TimerType.Focus.id)
            route = NavRoutes.Focus
        else if (action == TimerType.Quick.id) {
            route = if (activityPresent)
                NavRoutes.Quick
            else
                NavRoutes.QuickDialog
        }
        route?.let { viewModel.changeRoute(route) }
    }
}

@Composable
fun AppNavigation(
    dest: NavRoutes,
    onDestChanged: (NavRoutes) -> Unit = {}
) {
    NavHost(
        dest = dest,
        modifier = Modifier
            .fillMaxSize(),
        onDestChanged = onDestChanged
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        AppNavigation(NavRoutes.Home)
    }
}