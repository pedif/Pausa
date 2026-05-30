package com.techys.pausa.focus.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.techys.designsystem.theme.AppTheme

private const val VISIBLE_ITEM_COUNT = 5
private val ITEM_HEIGHT_DEFAULT = 48.dp
private val ITEM_WIDTH_DEFAULT = 48.dp

@Composable
fun TimerPicker(
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = VISIBLE_ITEM_COUNT,
    itemHeight: Dp = ITEM_HEIGHT_DEFAULT,
    itemWidth: Dp = ITEM_WIDTH_DEFAULT,
    onTimeChanged: (Int) -> Unit = {}
) {
    val minutes = remember {
        (1..120).toList()
    }
    val listener = remember {
        object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                onTimeChanged(item?.toIntOrNull() ?: 1)
            }
        }
    }
    AndroidView(
        factory = { context ->
            WheelView(context).apply {
                setOnWheelViewListener(listener)
            }
        },
        modifier = modifier.clipToBounds(),
        update = { view ->
            view.setItems(minutes.map { it.toString() })
            view.setSelection(selectedIndex)
        }
    )
}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        TimerPicker(2)
    }
}