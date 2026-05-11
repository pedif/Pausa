package com.techys.pausa.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.techys.home.component.HomeScreen
import com.techys.pausa.focus.component.FocusScreen
import com.techys.pausa.quick.component.QuickScreen


@Composable
fun NavHost(
    dest: NavRoutes,
    modifier: Modifier = Modifier
) {

    val backStack = remember { mutableStateListOf<Any>(dest) }
    val dialogStrategy: SceneStrategy<Any> = remember { DialogSceneStrategy<Any>() }
    LaunchedEffect(dest) {
        if (dest != NavRoutes.Quick)
            backStack.clear()
        backStack.add(dest)
    }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider {
            entry<NavRoutes.Home> {
                HomeScreen(
                    modifier = modifier,
                    onStartFocusClick = { backStack.add(NavRoutes.Focus) },
                    onStartQuickClick = { backStack.add(NavRoutes.Quick) }

                )
            }
            entry<NavRoutes.Focus> {
                FocusScreen(
                    modifier = modifier
                )
            }

            entry<NavRoutes.Quick>(
                metadata = DialogSceneStrategy.dialog(
                    DialogProperties(windowTitle = "Route B dialog")
                )
            ) {
                QuickScreen() {
                    backStack.removeLastOrNull()
                }
            }
            entry<NavRoutes.QuickDialog>(
            ) {

                val act = LocalActivity.current
                QuickScreen() {
                    act?.finish()
                }
            }
        }
    )
}