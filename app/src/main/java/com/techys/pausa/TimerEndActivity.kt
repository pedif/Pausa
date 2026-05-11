package com.techys.pausa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techys.designsystem.theme.AppTheme
import com.techys.pausa.ui.theme.PausaTheme

class TimerEndActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
            }
        }
    }
}


@Composable
fun MessageComponent(modifier: Modifier = Modifier) {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    }

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AppTheme {
        MessageComponent()
    }
}