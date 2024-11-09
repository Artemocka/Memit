package com.dracul.feature_viewer.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.feature_viewer.event.ViewerEvent
import com.dracul.feature_viewer.nav_component.ViewerComponent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.abs


@Composable
fun ViewerScreen(
    component: ViewerComponent
) {
    val context = LocalContext.current
    val images by component.images.collectAsState(emptyList())
    val index = component.index
    val scrollEnabled = remember { mutableStateOf(true) }
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.isSystemBarsVisible = false
        component.events.collect{
            when(it){
                ViewerEvent.OnExit -> {
                    Log.e(null,"ads")
                    systemUiController.isSystemBarsVisible = true
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        paddingValues
        val pagerState = rememberPagerState(
            pageCount = {
                images.size
            },
            initialPage = index,
        )
        HorizontalPager(
            userScrollEnabled = scrollEnabled.value, state = pagerState
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(images[it].uri).size(Size.ORIGINAL)
                    .memoryCacheKey(images[it].id.hashCode().toString())
                    .diskCacheKey(images[it].id.hashCode().toString())
                    .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
            )
            Image(
                modifier = Modifier.fillMaxSize().zoomable(rememberZoomState()),
                painter = painter,
                contentDescription = null
            )
        }
    }
}




@Composable
internal fun TimeColumnPicker(
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialValue)

    // Генерация списка значений времени.
    val list by remember {
        mutableStateOf(mutableListOf<String>().apply {
            (1..(countOfVisibleItemsInPicker / 2)).forEach { _ -> add("") }
            for (i in range) add(i.getTimeDefaultStr())
            (1..(countOfVisibleItemsInPicker / 2)).forEach { _ -> add("") }
        })
    }
    val offset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
    var selectedValue by remember { mutableIntStateOf(initialValue) }
    var firstIndex by remember { mutableStateOf(0) }
    var lastIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier.height(listHeight.dp),
        contentAlignment = Alignment.Center
    ) {
        Border(itemHeight = itemHeight.dp, color = MaterialTheme.colorScheme.outline)

        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            itemsIndexed(items = list) { index, it ->
                Box(
                    modifier = Modifier.fillParentMaxHeight(1f / countOfVisibleItemsInPicker),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    )
                }
            }
        }
    }
}

fun Int.getTimeDefaultStr(): String =  "${if (this <= 9) "0" else ""}$this"

// Количество видимых элементов в столбце
internal const val countOfVisibleItemsInPicker = 5

// Высота одного элемента
internal const val itemHeight = 35f

// Высота списка
internal const val listHeight = countOfVisibleItemsInPicker * itemHeight

@Composable
internal fun Border(itemHeight: Dp, color: Color) {
    val width = 2.dp
    val strokeWidthPx = with(LocalDensity.current) { width.toPx() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .drawBehind {
                drawLine(
                    color = color,
                    strokeWidth = strokeWidthPx,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                )

                drawLine(
                    color = color,
                    strokeWidth = strokeWidthPx,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height)
                )
            }
    ) {}
}

fun Int.pixelsToDp(context: Context): Float {
    val densityDpi = context.resources.displayMetrics.densityDpi
    return this / (densityDpi / 160f)
}
internal fun LazyListState.itemForScrollTo(context: Context): Int {
    val offset = firstVisibleItemScrollOffset.pixelsToDp(context)
    return when {
        offset == 0f -> firstVisibleItemIndex
        offset % itemHeight >= itemHeight / 2 -> firstVisibleItemIndex + 1
        else -> firstVisibleItemIndex
    }  // здесь можно конвертировать в простой if, но для наглядности оставлю
}

internal fun calculateScaleX(listState: LazyListState, index: Int): Float {
    // Получаем информацию о текущем состоянии компоновки списка
    val layoutInfo = listState.layoutInfo
    // Извлекаем индексы видимых элементов
    val visibleItems = layoutInfo.visibleItemsInfo.map { it.index }
    // Если элемент не виден, возвращаем масштаб 1 (нормальный)
    if (!visibleItems.contains(index)) return 1f
    // Находим информацию о конкретном элементе по индексу
    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return 1f
    // Вычисляем центр видимой области
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    // Вычисляем расстояние от центра до середины элемента
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    // Максимальное расстояние до центра для расчета масштаба
    val maxDistance = layoutInfo.viewportEndOffset / 2f
    // Сжимаем элемент до половины при максимальном расстоянии
    return 1f - (distance / maxDistance) * 0.5f
}

internal fun calculateScaleY(listState: LazyListState, index: Int): Float {
    // Получаем информацию о текущем состоянии компоновки списка
    val layoutInfo = listState.layoutInfo
    // Извлекаем индексы видимых элементов
    val visibleItems = layoutInfo.visibleItemsInfo.map { it.index }
    // Если элемент не виден, возвращаем масштаб 1 (нормальный)
    if (!visibleItems.contains(index)) return 1f
    // Находим информацию о конкретном элементе по индексу
    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return 1f
    // Вычисляем центр видимой области
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    // Вычисляем расстояние от центра до середины элемента
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    // Максимальное расстояние до центра для расчета масштаба
    val maxDistanceY = layoutInfo.viewportEndOffset / 2f
    // Сжимаем элемент полностью при максимальном расстоянии
    return 1f - (distance / maxDistanceY)
}

internal fun calculateAlpha(index: Int, listState: LazyListState): Float {
    // Получаем информацию о текущем состоянии компоновки списка
    val layoutInfo = listState.layoutInfo
    // Извлекаем индексы видимых элементов
    val visibleItems = layoutInfo.visibleItemsInfo.map { it.index }
    // Если нет видимых элементов, возвращаем максимальную непрозрачность
    if (visibleItems.isEmpty()) return 1f
    // Вычисляем центр видимой области
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    // Находим информацию о конкретном элементе по индексу
    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return 1f
    // Вычисляем расстояние от центра до середины элемента
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    // Максимальное расстояние для расчета прозрачности
    val maxDistance = layoutInfo.viewportEndOffset / 2f
    // Уменьшаем прозрачность до 0.3 при максимальном расстоянии
    return 1f - (distance / maxDistance) * 0.7f
}