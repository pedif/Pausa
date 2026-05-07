package com.techys.pausa

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.techys.pausa.navigation.Route
import com.techys.pausa.tmp.focus.FocusViewModel
import com.techys.pausa.tmp.focus.component.FocusScreen
import com.techys.pausa.tmp.home.HomeViewModel
import com.techys.pausa.tmp.home.component.HomeScreen
import com.techys.pausa.tmp.model.TimerStateType
import com.techys.pausa.tmp.model.TimerType
import com.techys.pausa.tmp.quick.QuickViewModel
import com.techys.pausa.tmp.quick.component.QuickScreen
import com.techys.pausa.tmp.receiver.PausaServiceReceiver
import com.techys.pausa.tmp.service.PausaService
import com.techys.pausa.tmp.util.TimerConstants
import com.techys.pausa.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class DialogActivity : ComponentActivity() {

    private val _state = MutableStateFlow<Route>(Route.Home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()
        enableEdgeToEdge()
        setContent {
                val dest by _state.collectAsState()
                DialogNavigation(dest = dest){finish()}
        }
        checkIntentForDeepLink(intent)
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
        checkIntentForDeepLink(intent)
    }

    private fun checkIntentForDeepLink(intent: Intent) {
        val action = intent.action ?: return
        Log.e("tagtag", "actopm new intent is $action")
        if (action == TimerType.Focus.id)
            _state.value = Route.Focus
        else if (action == TimerType.Quick.id)
            _state.value = Route.Quick
    }
}

@Composable
fun DialogNavigation(dest: Route,
                     onDialogDismissed:()-> Unit = {}) {
    val context = LocalActivity.current?.application?.applicationContext!!
    val qvm = remember {
        QuickViewModel(context)
    }
//    Box(modifier = Modifier.background(Color.Green)) {
        QuickScreen(
            viewModel = qvm,
            onDismissed = onDialogDismissed
        )
//    }
}

