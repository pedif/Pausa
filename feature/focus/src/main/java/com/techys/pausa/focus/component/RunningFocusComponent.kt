package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue

@Composable
fun RunningFocusComponent(
    isTimerPaused: Boolean,
    modifier: Modifier = Modifier,
    onTimerPauseClicked: () -> Unit = {},
    onTimerStopClicked: () -> Unit = {},
    onTimerResumeClicked: () -> Unit = {}
) {

    Row(
        modifier = modifier.height(Dimen.FocusActionAreaHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if(isTimerPaused){
            IconButton(
                onClick = onTimerResumeClicked,
                modifier = Modifier.size(80.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.DarkGray)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "",
                    tint = NeonBlue,
                    modifier = Modifier.size(64.dp)
                )
            }
        }else {
            IconButton(
                onClick = onTimerPauseClicked,
                modifier = Modifier.size(80.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.DarkGray)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "",
                    tint = NeonBlue,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(Dimen.large))
        IconButton(
            onClick = onTimerStopClicked,
            modifier = Modifier.size(80.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.DarkGray)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                tint = NeonBlue,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme { RunningFocusComponent(isTimerPaused = false) { } }
}