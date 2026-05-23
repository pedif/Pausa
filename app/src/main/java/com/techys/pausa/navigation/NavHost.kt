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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.techys.home.component.HomeScreen
import com.techys.onboarding.screen.OnboardingScreen
import com.techys.pausa.eye.component.EyeEndScreen
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
    dest: NavKey,
    modifier: Modifier = Modifier,
    onDestChanged: (NavRoutes) -> Unit = {}
) {
    val navStack = rememberNavBackStack(dest)
    val dialogStrategy: SceneStrategy<Any> = remember { DialogSceneStrategy<Any>() }
    /**
     * Destination comes from the activity and the custom intents that start the activity
     * this means that we should take different behavior based on the target destination
     * that has been requested
     */
    LaunchedEffect(dest) {
        if (dest == navStack.last())
            return@LaunchedEffect
        if (dest != NavRoutes.Quick)
            navStack.clear()
        navStack.add(dest)
    }

    LaunchedEffect(navStack.size) {
        navStack.lastOrNull()?.let { navRoute ->
            onDestChanged(navRoute as NavRoutes)
        }
    }

    NavDisplay(
        backStack = navStack,
        onBack = { navStack.removeLastOrNull() },
        transitionSpec = appTransitionSpec(),
        popTransitionSpec = defaultPredictivePopTransitionSpec(),
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider {
            entry<NavRoutes.Home> {
                HomeScreen(
                    modifier = modifier,
                    onStartFocusClick = { navStack.add(NavRoutes.Focus) },
                    onStartQuickClick = { navStack.add(NavRoutes.Quick) },
                    onSettingsClick = { navStack.add(NavRoutes.Settings) },
                )
            }
            entry<NavRoutes.Focus> {
                FocusScreen(
                    modifier = modifier,
                    onSettingsClick = { navStack.add(NavRoutes.Settings) },
                    onBackClick = {
                        /**
                         * The focus screen should always navigate back to the home screen but
                         * we can start the focus screen as the starting destination from the
                         * notification so in that case we manually add the home screen
                         * as the backstack screen
                         */
                        if (navStack.size == 1) {
                            navStack.removeLastOrNull()
                            navStack.add(NavRoutes.Home)
                        } else
                            navStack.removeLastOrNull()
                    }
                )
            }

            entry<NavRoutes.Quick>(
                metadata = DialogSceneStrategy.dialog(
                    DialogProperties()
                )
            ) {
                QuickScreen(modifier = modifier) {
                    navStack.removeLastOrNull()
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
                    onBackClick = { navStack.removeLastOrNull() })
            }

            entry<NavRoutes.Onboarding> {
                OnboardingScreen(
                    modifier = modifier,
                    onFinish = {
                        navStack.removeLastOrNull()
                        navStack.add(NavRoutes.Home)
                    }
                )
            }
            entry<NavRoutes.None> { }
        }
    )
}