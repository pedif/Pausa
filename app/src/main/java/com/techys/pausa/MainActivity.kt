package com.techys.pausa

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techys.core.model.TimerType
import com.techys.core.service.PausaService
import com.techys.designsystem.theme.AppTheme
import com.techys.pausa.navigation.Route
import com.techys.home.HomeViewModel
import com.techys.home.component.HomeScreen
import com.techys.pausa.focus.FocusViewModel
import com.techys.pausa.focus.component.FocusScreen
import com.techys.pausa.quick.QuickViewModel
import com.techys.pausa.quick.component.QuickScreen
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private val _state = MutableStateFlow<Route>(Route.Home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val dest by _state.collectAsState()
//                if (activityStarted)
                AppNavigation(dest = dest) {
                    _state.value = it
                }
//                else if (dest == Route.Quick)
//                    DialogNavigation(dest, { finish() })
            }
        }

        checkIntentForDeepLink(intent, false)
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
            _state.value = Route.Focus
        else if (action == TimerType.Quick.id) {
            setContent {
                AppTheme {
                    val dest by _state.collectAsState()
                    DialogNavigation(dest) {
                        finish()
                    }

                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    dest: Route,
    onDestChanged: (Route) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() }) { innerPadding ->
        val viewModel = remember {
            HomeViewModel()
        }
        val fvm = remember {
            FocusViewModel()
        }
        val context = LocalActivity.current?.application?.applicationContext!!
        val qvm = remember {
            QuickViewModel(context)
        }
        when (dest) {
            Route.Focus -> {
                FocusScreen(
                    viewModel = fvm,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }


            Route.Quick -> {
                QuickScreen(
                    viewModel = qvm,
                    onDismissed = { onDestChanged(Route.Home) })
            }

            else -> {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    viewModel = viewModel,
                    onStartFocusClick = { onDestChanged(Route.Focus) },
                    onStartQuickClick = { onDestChanged(Route.Quick) }

                )
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
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
        AppNavigation(Route.Home)
    }
}