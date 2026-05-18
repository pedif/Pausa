package com.techys.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techys.designsystem.theme.AppTheme
import com.techys.designsystem.theme.Dimen
import org.w3c.dom.Text

@Composable
fun PausaButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(Dimen.smallShapeCorner),
        enabled = enabled
    ) {
        Text(text)
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        PausaButton("sample text")
    }
}