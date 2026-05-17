package com.techys.pausa.focus.component

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.techys.designsystem.theme.AppTheme

private const val VISIBLE_ITEM_COUNT = 5
private val ITEM_HEIGHT_DEFAULT = 48.dp
private val ITEM_WIDTH_DEFAULT = 48.dp

@Composable
fun TimerPicker(
    modifier: Modifier = Modifier,
    visibleItemCount: Int = VISIBLE_ITEM_COUNT,
    itemHeight: Dp = ITEM_HEIGHT_DEFAULT,
    itemWidth: Dp = ITEM_WIDTH_DEFAULT,
    onTimeChanged: (Int) -> Unit = {}
) {
    val minutes = remember {
        (1..120).toList()
    }
    var selectedIndex by remember { mutableIntStateOf(2) }

    WheelPicker(
        items = minutes,
        selectedIndex = selectedIndex,
        onSelectedIndexChange = {
            selectedIndex = it
            onTimeChanged(minutes[it])
        },
        visibleItemsCount = visibleItemCount,
        itemHeight = itemHeight,
        modifier = modifier.width(itemWidth)
    )
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        TimerPicker()
    }
}