package com.techys.settings.screen

import android.content.Context.MODE_PRIVATE
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.techys.designsystem.theme.CardBackground
import com.techys.designsystem.theme.Dimen
import com.techys.settings.model.SoundItem

@Composable
fun TimerSoundSettings(
    sounds: List<SoundItem>,
    eyeSoundItem: SoundItem,
    focusSoundItem: SoundItem,
    quickSoundItem: SoundItem,
    modifier: Modifier = Modifier,
    onPomodoroSoundChange: (String?) -> Unit = {},
    onQuickTimerSoundChange: (String?) -> Unit = {},
    onZenModeSoundChange: (String?) -> Unit = {}
) {
    var showPomodoroPicker by remember { mutableStateOf(false) }
    var showQuickTimerPicker by remember { mutableStateOf(false) }
    var showZenModePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("timer_settings", MODE_PRIVATE) }
    Card(
        modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier) {
            // Pomodoro Timer Sound
            ListItem(
                headlineContent = { Text("Pomodoro Timer") },
                supportingContent = {
                    Text(
                        eyeSoundItem.title
                    )
                },
                leadingContent = { Icon(Icons.Default.Build, null) },
                modifier = Modifier.clickable { showPomodoroPicker = true },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))
            // Quick Timer Sound
            ListItem(
                headlineContent = { Text("Quick Timer") },
                supportingContent = {
                    Text(
                        quickSoundItem.title
                    )
                },
                leadingContent = { Icon(Icons.Default.Face, null) },
                modifier = Modifier.clickable { showQuickTimerPicker = true },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = Dimen.medium))
            // Zen Mode Sound
            ListItem(
                headlineContent = { Text("Zen Mode") },
                supportingContent = {
                    Text(
                        focusSoundItem.title
                    )
                },
                leadingContent = { Icon(Icons.Default.Call, null) },
                modifier = Modifier.clickable { showZenModePicker = true },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            )
        }
    }

    // ... similar for other pickers
}

@Composable
fun SoundPickerDialog(
    sounds: List<SoundItem>,
    currentSoundUri: Uri?,
    onSoundSelected: (Uri?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Sound") },
        text = {
            LazyColumn {
                // Default option
                item {
                    ListItem(
                        headlineContent = { Text("Default") },
                        supportingContent = { Text("System notification sound") },
                        leadingContent = {
                            RadioButton(
                                selected = currentSoundUri == null,
                                onClick = { onSoundSelected(null) }
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = {
//                                RingtoneManager.getRingtone(
//                                    context,
//                                    soundManager.getDefaultSoundUri()
//                                )?.play()
                            }) {
                                Icon(Icons.Default.PlayArrow, "Preview")
                            }
                        }
                    )
                }

                // Divider
                item { HorizontalDivider() }

                // Available sounds
                items(sounds) { sound ->
                    val uri = Uri.parse(sound.uri)
                    val isSelected = currentSoundUri == uri

                    ListItem(
                        headlineContent = { Text(sound.title) },
                        leadingContent = {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSoundSelected(uri) }
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                RingtoneManager.getRingtone(context, uri)?.play()
                            }) {
                                Icon(Icons.Default.PlayArrow, "Preview")
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}