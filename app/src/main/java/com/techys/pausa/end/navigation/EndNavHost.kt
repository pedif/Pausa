package com.techys.pausa.end.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.techys.home.component.HomeScreen
import com.techys.pausa.eye.component.EyeEndScreen
import com.techys.pausa.focus.component.FocusScreen
import com.techys.pausa.quick.component.QuickEndScreen
import com.techys.pausa.quick.component.QuickScreen


@Composable
fun EndNavHost(
    backStack: SnapshotStateList<EndNavRoute>,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<EndNavRoute.Eye> {
                EyeEndScreen{activity?.finish()}
            }
            entry<EndNavRoute.Focus> {
                FocusScreen(
                    modifier = modifier
                )
            }

            entry<EndNavRoute.Quick> {
                QuickEndScreen { activity?.finish() }
            }
        }
    )
}