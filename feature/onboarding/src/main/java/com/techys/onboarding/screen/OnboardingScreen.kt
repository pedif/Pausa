package com.techys.onboarding.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techys.designsystem.component.PausaButton
import com.techys.designsystem.theme.AppTheme
import com.techys.onboarding.OnboardingViewModel
import com.techys.pausa.core.R
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 3

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val isOnboardingCompleted by viewModel.onboardingCompleted.collectAsState()
    if (isOnboardingCompleted) {
        onFinish()
    }
    Scaffold(
    ) { innerPadding ->
        OnboardingScreen(
            modifier = modifier.padding(innerPadding),
            onFinish = viewModel::completeOnboarding
        )
    }
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit = {}
) {

    var currentPage by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

    // Sync state with pager
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // ViewPager (HorizontalPager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> IntroductionComponent()
                1 -> PostNotificationComponent()
                2 -> BatteryOptimizationComponent()
            }
        }


        // Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Skip Button
            TextButton(onClick = onFinish) {
                Text(
                    stringResource(com.techys.onboarding.R.string.onboarding_action_skip),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(PAGE_COUNT) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.White
                                else Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
            val nextButtonText = if (currentPage == PAGE_COUNT - 1)
                stringResource(com.techys.onboarding.R.string.onboarding_action_finish)
            else
                stringResource(com.techys.onboarding.R.string.onboarding_action_next)
            // Next / Finish Button
            PausaButton(
                text = nextButtonText,
                onClick = {
                    if (currentPage < PAGE_COUNT - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinish()
                    }
                }
            )
        }
    }

}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        OnboardingScreen { }
    }
}