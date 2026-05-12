package com.techys.settings.screen


import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.settings.R
import com.techys.settings.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    SettingsScreen(modifier = modifier)
}

//Why not lazy colum instead of colum with scrollable modifier??
@Composable
private fun SettingsScreen(modifier: Modifier = Modifier) {
    val scroll = rememberScrollState()
    //Make composable not clip its children to it's padding jsut at the top and bottom
    Column(
        modifier = modifier.scrollable(scroll, orientation = Orientation.Vertical)
            .fillMaxSize()
            .padding(
                horizontal = Dimen.paddingScreen,
                vertical = Dimen.paddingScreen
            )
    ) {
        PermissionsComponent(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.version),
            modifier = Modifier.padding(top = Dimen.large))
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        Surface {
            SettingsScreen()
        }
    }
}