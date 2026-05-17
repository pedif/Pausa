package com.techys.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import com.techys.designsystem.theme.Dimen

@Composable
fun PausaIconButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    imageDescription: String? = null,
    onClick: () -> Unit = {},
    buttonSize: Dp = Dimen.iconButtonSize,
    imageSize: Dp = Dimen.iconImageSize,
    buttonColors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    imageTint: Color = MaterialTheme.colorScheme.primary

) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(buttonSize),
        colors = buttonColors
    ) {
        Icon(
            painter = painter,
            contentDescription = imageDescription,
            tint = imageTint,
            modifier = Modifier.size(imageSize)
        )
    }
}
