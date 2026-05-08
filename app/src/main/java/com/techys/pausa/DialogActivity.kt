package com.techys.pausa

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.techys.pausa.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow

class DialogActivity : ComponentActivity() {

    private val _state = MutableStateFlow<Route>(Route.Home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startService()
        enableEdgeToEdge()
        setContent {
                val dest by _state.collectAsState()
                DialogNavigation(dest = dest){finish()}
        }
//        checkIntentForDeepLink(intent)
    }

//    private fun startService() {
//        val intent = Intent(this, PausaService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent)
//        } else {
//            startService(intent)
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        checkIntentForDeepLink(intent)
//    }
//
//    private fun checkIntentForDeepLink(intent: Intent) {
//        val action = intent.action ?: return
//        Log.e("tagtag", "actopm new intent is $action")
//        if (action == TimerType.Focus.id)
//            _state.value = Route.Focus
//        else if (action == TimerType.Quick.id)
//            _state.value = Route.Quick
//    }
}

@Composable
fun DialogNavigation(dest: Route,
                     onDialogDismissed:()-> Unit = {}) {
    val context = LocalActivity.current?.application?.applicationContext!!
//    val qvm = remember {
//        QuickViewModel(context)
//        }
//        QuickScreen(
//            viewModel = qvm,
//            onDismissed = onDialogDismissed
//        )
}

