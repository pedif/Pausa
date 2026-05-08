package com.techys.pausa.quick.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.quick.QuickState
import com.techys.pausa.quick.QuickViewModel

@Composable
fun QuickScreen(
    viewModel: QuickViewModel,
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()

    QuickScreen(
        state = state,
        modifier = modifier,
        onTimePicked = { index ->
            if (index >= 0)
                viewModel.onTimePicked(index)
            onDismissed()
        },
        onTitleChanged = viewModel::onTitleChanged
    )

}

/**
 * Displays the Quick screen dialog
 * @param onTimePicked returns the index of the selected time. -1 if the dialog was dismissed instead
 */
@Composable
fun QuickScreen(
    state: QuickState,
    modifier: Modifier = Modifier,
    onTimePicked: (Int) -> Unit = {},
    onTitleChanged: (String) -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onTimePicked(-1) }
    ) {
        Surface(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.paddingScreenHorizontal)
            ) {
                Text(
                    text = "Setup a Quick Timer",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Dimen.large))
                TextField(
                    value = state.title,
                    onValueChange = onTitleChanged,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimen.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.timeList.forEachIndexed { index, time ->
                        Button(onClick = { onTimePicked(index) }) {
                            Text("$time")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        QuickScreen(QuickState())
    }
}