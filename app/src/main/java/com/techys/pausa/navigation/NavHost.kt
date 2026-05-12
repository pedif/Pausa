package com.techys.pausa.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.techys.home.component.HomeScreen
import com.techys.pausa.focus.component.FocusScreen
import com.techys.pausa.quick.component.QuickScreen
import com.techys.settings.screen.SettingsScreen

private fun <T : Any> appTransitionSpec():
        AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    ContentTransform(
        fadeIn(animationSpec = tween(400)),
        initialContentExit = ExitTransition.None,
    )
}

private fun <T : Any> defaultPredictivePopTransitionSpec():
        AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    ContentTransform(
        fadeIn(
            spring(
                dampingRatio = 1.0f, // reflects material3 motionScheme.defaultEffectsSpec()
                stiffness = 1600.0f, // reflects material3 motionScheme.defaultEffectsSpec()
            )
        ),
        scaleOut(targetScale = 0.7f),
    )
}

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
        transitionSpec = appTransitionSpec(),
        popTransitionSpec = defaultPredictivePopTransitionSpec(),
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider {
            entry<NavRoutes.Home> {
                HomeScreen(
                    modifier = modifier,
                    onStartFocusClick = { backStack.add(NavRoutes.Focus) },
                    onStartQuickClick = { backStack.add(NavRoutes.Quick) },
                    onSettingsClick = { backStack.add(NavRoutes.Settings) },
                )
            }
            entry<NavRoutes.Focus> {
                FocusScreen(
                    modifier = modifier,
                    onSettingsClick = { backStack.add(NavRoutes.Settings) },
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }

            entry<NavRoutes.Quick>(
                metadata = DialogSceneStrategy.dialog(
                    DialogProperties()
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

            entry<NavRoutes.Settings> {
                SettingsScreen(
                    modifier = modifier,
                    onBackClick = { backStack.removeLastOrNull() })
            }
        }
    )
}