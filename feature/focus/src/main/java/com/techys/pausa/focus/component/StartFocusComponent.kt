package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ozcanalasalvar.wheelview.WheelView
import com.techys.designsystem.component.FVerticalWheelPicker
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue


@Composable
fun StartFocusComponent(modifier: Modifier = Modifier,
                        onTimeChanged: (Int) -> Unit = {},
                        onTimerStartedClicked:()->Unit = {}) {
    Row(
        modifier = modifier.height(Dimen.FocusActionAreaHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onTimerStartedClicked,
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
        Spacer(modifier = Modifier.width(Dimen.medium))
        TimerPicker(
            modifier = Modifier,
            onTimeChanged = onTimeChanged
        )
        Spacer(modifier = Modifier.width(Dimen.small))
        Text(text = "Minutes",
            color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        StartFocusComponent {  }
    }
}