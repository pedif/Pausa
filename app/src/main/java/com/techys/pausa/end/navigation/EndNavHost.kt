package com.techys.pausa.end.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.techys.home.component.HomeScreen
import com.techys.pausa.eye.component.EyeEndScreen
import com.techys.pausa.focus.component.FocusEndScreen
import com.techys.pausa.focus.component.FocusScreen
import com.techys.pausa.quick.component.QuickEndScreen
import com.techys.pausa.quick.component.QuickScreen


@Composable
fun EndNavHost(
    dest: EndNavRoute,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current

    when (dest) {
        EndNavRoute.Eye -> {
            EyeEndScreen { activity?.finish() }
        }

        EndNavRoute.Focus -> {
            FocusEndScreen { activity?.finish() }
        }

        EndNavRoute.Quick -> {
            QuickEndScreen { activity?.finish() }
        }

        else -> {activity?.finish()}
    }
}