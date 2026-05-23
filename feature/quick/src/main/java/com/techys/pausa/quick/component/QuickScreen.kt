package com.techys.pausa.quick.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.designsystem.component.PausaButton
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.pausa.quick.QuickState
import com.techys.pausa.quick.QuickViewModel
import com.techys.pausa.quick.R

@Composable
fun QuickScreen(
    modifier: Modifier = Modifier,
    viewModel: QuickViewModel = hiltViewModel(),
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
        onTitleChanged = viewModel::onTitleChanged,
        onDurationChanged = viewModel::onDurationChanged
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
    onTitleChanged: (String) -> Unit = {},
    onDurationChanged: (String) -> Unit = {}
) {
    val isDurationError = state.durationErrorMessage.isNotEmpty()
    Dialog(
        onDismissRequest = { onTimePicked(-1) }
    ) {
        Surface(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.paddingScreen)
            ) {
                Text(
                    text = stringResource(R.string.quick_dialog_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(Dimen.large))
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChanged,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimen.large))
                OutlinedTextField(
                    value = state.duration,
                    onValueChange = {
                        onDurationChanged(it)
                    },
                    singleLine = true,
                    isError = isDurationError,
                    supportingText = {
                        if (isDurationError)
                            Text(text = state.durationErrorMessage)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (isDurationError) ImeAction.None else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (state.duration.toIntOrNull()!! > 0)
                                onTimePicked(state.duration.toIntOrNull()!!)
                        }
                    ),
                    suffix = {
                        Text(text = "minute",
                            style = MaterialTheme.typography.bodySmall)
                    }
                )
                Text(
                    text = stringResource(R.string.quick_duration_support_text),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(Dimen.small))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    state.timeList.forEachIndexed { index, time ->
                        PausaButton(
                            text = "$time",
                            onClick = { onTimePicked(time) })
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