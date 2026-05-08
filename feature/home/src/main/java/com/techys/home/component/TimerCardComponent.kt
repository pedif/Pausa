package com.techys.home.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techys.designsystem.theme.CardBackground
import com.techys.designsystem.theme.Dimen

@Composable
fun TimerCardComponent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimen.medium, horizontal = Dimen.large),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(Dimen.homeCardCorner),
        content = content
    )
}