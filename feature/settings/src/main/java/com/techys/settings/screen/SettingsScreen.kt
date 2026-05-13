package com.techys.settings.screen


import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.designsystem.theme.Dimen
import com.techys.settings.R
import com.techys.settings.model.SoundItem
import com.techys.settings.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            SettingsTopBar(
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        SettingsScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            soundList = state.sounds,
            eyeSoundItem = state.eyeSoundItem,
            focusSoundItem = state.focusSoundItem,
            quickSoundItem = state.quickSoundItem
        )
    }
}

//Why not lazy colum instead of colum with scrollable modifier??
@Composable
private fun SettingsScreen(
    soundList: List<SoundItem>,
    eyeSoundItem: SoundItem,
    focusSoundItem: SoundItem,
    quickSoundItem: SoundItem,
    modifier: Modifier = Modifier
) {

    val scroll = rememberScrollState()
    //Make composable not clip its children to it's padding jsut at the top and bottom
    Column(
        modifier = modifier
            .scrollable(scroll, orientation = Orientation.Vertical)
            .fillMaxSize()
            .padding(
                horizontal = Dimen.paddingScreen,
                vertical = Dimen.paddingScreen
            )
    ) {
        PermissionsComponent(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(Dimen.large))
        TimerSoundSettings(
            sounds = soundList,
            eyeSoundItem = eyeSoundItem,
            focusSoundItem = focusSoundItem,
            quickSoundItem = quickSoundItem
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.version),
            modifier = Modifier.padding(top = Dimen.large)
        )
    }
}

//@Preview
//@Composable
//private fun PreviewScreen() {
//    AppTheme {
//        Surface {
//            SettingsScreen()
//        }
//    }
//}