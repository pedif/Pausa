package com.techys.onboarding.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.techys.designsystem.theme.Dimen
import com.techys.onboarding.R
import com.techys.onboarding.util.boldIntroductionText

@Composable
fun IntroductionComponent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.onboarding_introduction_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Text(
            text = stringResource(R.string.onboarding_introduction_subtitle),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(Dimen.small))
        Text(
            text = boldIntroductionText(
                stringResource(R.string.onboarding_introduction_eye)
            ),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = boldIntroductionText(
                stringResource(R.string.onboarding_introduction_focus)
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = boldIntroductionText(
                stringResource(R.string.onboarding_introduction_quick)
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(Dimen.small))
        Text(
            text = stringResource(R.string.onboarding_introduction_end),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    Surface {
        IntroductionComponent(modifier = Modifier.padding(Dimen.paddingScreen))
    }
}