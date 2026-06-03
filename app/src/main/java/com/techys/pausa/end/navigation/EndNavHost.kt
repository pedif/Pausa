package com.techys.pausa.end.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.techys.core.receiver.PausaAlarmReceiver
import com.techys.pausa.eye.component.EyeEndScreen
import com.techys.pausa.focus.component.FocusEndScreen
import com.techys.pausa.quick.component.QuickEndScreen


@Composable
fun EndNavHost(
    dest: EndNavRoute,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current

    when (dest) {
        EndNavRoute.Eye -> {
            EyeEndScreen {
                PausaAlarmReceiver.sendDismissNotification(activity)
            }
        }

        EndNavRoute.Focus -> {
            FocusEndScreen {
                PausaAlarmReceiver.sendDismissNotification(activity)
            }
        }

        EndNavRoute.Quick -> {
            QuickEndScreen {
                PausaAlarmReceiver.sendDismissNotification(activity)
            }
        }
    }
}