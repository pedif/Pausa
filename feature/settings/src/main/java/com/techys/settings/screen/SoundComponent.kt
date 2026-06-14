package com.techys.settings.screen

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techys.core.model.TimerType
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import com.techys.settings.R
import com.techys.settings.model.SoundItem

@Composable
fun SoundCard(
    modifier: Modifier = Modifier,
    eyeSoundItem: SoundItem,
    focusSoundItem: SoundItem,
    quickSoundItem: SoundItem,
    onEyeSoundChange:(Uri)->Unit = {},
    onFocusSoundChange:(Uri)->Unit = {},
    onQuickSoundChange:(Uri)->Unit = {},
) {

    var ringtoneTarget = TimerType.EyeBreak
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(
                    RingtoneManager.EXTRA_RINGTONE_PICKED_URI,
                    Uri::class.java
                )
            } else {
              result .data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
            if(uri==null)
                return@rememberLauncherForActivityResult
            when(ringtoneTarget){
                TimerType.EyeBreak -> onEyeSoundChange(uri)
                TimerType.Focus-> onFocusSoundChange(uri)
                TimerType.Quick -> onQuickSoundChange(uri)
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val intent = Intent(
        RingtoneManager.ACTION_RINGTONE_PICKER
    ).apply {
        putExtra(
            RingtoneManager.EXTRA_RINGTONE_TYPE,
            RingtoneManager.TYPE_ALARM
        )
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
    }


    Card(
        modifier = modifier, shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(Dimen.medium)
        ) {

            Text(
                text = stringResource(R.string.alarm_section_title),
                style = MaterialTheme.typography.titleMedium
            )

//            RingtoneItem(
//                label = stringResource(R.string.alarm_sound_eye),
//                item = eyeSoundItem) {
//                ringtoneTarget = TimerType.EyeBreak
//                with(intent) {
//                    putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, eyeSoundItem.uri)
//                    launcher.launch(intent)
//                }
//            }
//
//
//            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))


            RingtoneItem(
                label = stringResource(R.string.alarm_sound_focus),
                item = focusSoundItem) {
                ringtoneTarget = TimerType.Focus
                with(intent) {
                    putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, focusSoundItem.uri)
                    launcher.launch(intent)
                }
            }


            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))


            RingtoneItem(
                label = stringResource(R.string.alarm_sound_quick),
                item = quickSoundItem) {
                ringtoneTarget = TimerType.Quick
                with(intent) {
                    putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, quickSoundItem.uri)
                    launcher.launch(intent)
                }
            }

        }
    }
}

@Composable
private fun RingtoneItem(
    modifier: Modifier = Modifier,
    label: String,
    item: SoundItem,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Dimen.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier
        )
        Text(text = item.title)
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    val item = SoundItem()
    AppTheme {
        SoundCard(
            eyeSoundItem = item,
            focusSoundItem = item,
            quickSoundItem = item
        )
    }
}