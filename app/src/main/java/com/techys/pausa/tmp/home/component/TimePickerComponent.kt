package com.techys.pausa.tmp.home.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techys.pausa.ui.theme.AppTheme
import com.techys.pausa.ui.theme.Dimen
import kotlin.math.abs

@Composable
fun TimerPicker(
    modifier: Modifier = Modifier,
    onTimeChanged: (Int) -> Unit = {}
) {
    val minutes = remember {
        (0..120).toList()
    }
    val hours = remember {
        (0..11).toList()
    }
    Row(
        modifier = modifier
            .padding(Dimen.homeCardPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        TimeWheel(items = hours, modifier = Modifier.weight(0.5f))
//        Text(text = ":")
        TimeWheel(
            items = minutes, modifier = Modifier.weight(0.5f),
            onItemSelected = onTimeChanged
        )
    }
}

//@Composable
//fun TimeWheel(
//    items: List<Int>,
//    modifier: Modifier = Modifier,
//    onItemSelected: (Int) -> Unit = {}
//) {
//    val listState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2) // big start index
//
//    LaunchedEffect(listState) {
//        snapshotFlow { listState.firstVisibleItemIndex }
//            .collect { index ->
//                val buffer = 50
//                if (index < buffer || index > Int.MAX_VALUE / 2 + buffer) {
//                    val newIndex = Int.MAX_VALUE / 2
//                    listState.scrollToItem(newIndex)
//                }
//            }
//    }
//
//    val currentIndex by remember {
//        derivedStateOf {
//            listState.firstVisibleItemIndex % items.size
//        }
//    }
//
//    LazyColumn(
//        state = listState,
//        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
//        modifier = Modifier.height(150.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        items(items.size) { index ->
//            Box(
//                modifier = Modifier
//                    .height(50.dp)
//                    .fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = items[index].toString().padStart(2, '0'),
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (index == currentIndex) Color(0xFF2196F3) else Color.Gray
//                )
//            }
//        }
//    }
//}


@Composable
private fun TimeWheel(
    items: List<Int>,
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit = {}
) {

    val paddedItems = mutableListOf<String>("")
    paddedItems.addAll(items.map { it.toString().padStart(2, '0') })
    paddedItems.add("")
    val listState = rememberLazyListState()
    val snappingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val currentIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty())
                return@derivedStateOf 0
            //find the center of the visible area
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            //find the item whose center is closest to center
            visibleItems.minByOrNull { item ->
                val itemCenter = item.offset + (item.size / 2)
                abs(itemCenter - viewportCenter)
            }?.index ?: 0
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snappingBehavior,
            modifier = Modifier.height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(paddedItems.size) { index ->
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = paddedItems[index],
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index == currentIndex) Color(0xFF2196F3) else Color.Gray
                    )
                }
            }
        }
    }
    LaunchedEffect(currentIndex) {
        val index = currentIndex.coerceAtLeast(1)
        onItemSelected(items[index - 1])
    }

}

@Preview
@Composable
private fun PreviewComponent() {
    AppTheme {
        TimerPicker()
    }
}