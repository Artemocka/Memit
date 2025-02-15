package com.dracul.feature_viewer.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.common.utills.debug
import com.dracul.feature_viewer.mvi.ViewerAction
import com.dracul.feature_viewer.nav_component.ViewerComponent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    component: ViewerComponent
) {
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    val images by component.images.collectAsState(emptyList())
    val index = component.index
    var job: Job? = null
    val systemUiController = rememberSystemUiController()
    val pagerState = rememberPagerState(
        pageCount = {
            images.size
        },
        initialPage = index,
    )
    var firstTime by remember { mutableStateOf(true) }
    val centeredIndex by remember {
        derivedStateOf {
            val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val viewportCenter = lazyListState.layoutInfo.viewportEndOffset / 2
                visibleItems.minByOrNull {
                    abs(it.offset + it.size / 2 - viewportCenter)
                }?.index ?: 0
            } else {
                0
            }
        }
    }
    var ignoreSlider by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val widthDP by remember { mutableStateOf(((configuration.screenWidthDp / 2) - 44).dp) }
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        debug("firstTime: $firstTime")
        job = launch {
            lazyListState.scrollToItem(component.state.currentSliderImage + 1)
        }
        job?.invokeOnCompletion {
            firstTime = false
        }
    }
    LaunchedEffect(component.state.currentImage) {
        if (pagerState.pageCount > 0 && component.state.currentImage != pagerState.targetPage) {
            launch {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                pagerState.animateScrollToPage(component.state.currentImage)
            }
        }
    }
    LaunchedEffect(component.state.currentSliderImage) {
        if (lazyListState.layoutInfo.totalItemsCount > 0) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            job?.cancel()
            job = launch {
                ignoreSlider = true
                lazyListState.animateScrollToItemCenter(component.state.currentSliderImage + 1)
            }
            job!!.invokeOnCompletion { ignoreSlider = false }
        }
    }
    LaunchedEffect(lazyListState.isScrollInProgress) {
        component.onAction(ViewerAction.SliderScrollInProgress(lazyListState.isScrollInProgress))
    }
    LaunchedEffect(pagerState.isScrollInProgress) {
        component.onAction(ViewerAction.PagerScrollInProgress(pagerState.isScrollInProgress))
    }
    LaunchedEffect(pagerState.targetPage) {
        component.onAction(ViewerAction.PagerTargetImage(pagerState.targetPage))
    }
    LaunchedEffect(centeredIndex) {
        debug("firstTime: $firstTime")
        if (!firstTime)
            component.onAction(ViewerAction.SlideImage(centeredIndex - 1, pagerState.currentPage, pagerState.targetPage))
    }
    LaunchedEffect(component.state.showUi) {
        if (lazyListState.layoutInfo.totalItemsCount > 0) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            job?.cancel()
            job = launch {
                ignoreSlider = true
                lazyListState.animateScrollToItemCenter(component.state.currentImage + 1)
            }
            job.invokeOnCompletion { ignoreSlider = false }
        }
        systemUiController.isSystemBarsVisible = component.state.showUi
    }
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        paddingValues
        HorizontalPager(
            userScrollEnabled = true, state = pagerState
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(images[it].uri).size(Size.ORIGINAL).memoryCacheKey(images[it].id.hashCode().toString())
                    .diskCacheKey(images[it].id.hashCode().toString()).diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
            )
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState = rememberZoomState(), onTap = {
                        component.onAction(ViewerAction.Click)
                    }), painter = painter, contentDescription = null
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(), visible = component.state.showUi, enter = slideInVertically(
                    initialOffsetY = { +it }, animationSpec = tween(300)
                ) + fadeIn(tween(300)), exit = slideOutVertically(
                    targetOffsetY = { +it }, animationSpec = tween(300)
                ) + fadeOut(tween(300))
            ) {
                LazyRow(
                    state = lazyListState,
                    flingBehavior = flingBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    item { Spacer(modifier = Modifier.width(widthDP)) }
                    items(
                        count = images.size,
                        key = { images[it].id },
                        contentType = { 0 },
                    ) { imageIndex ->
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context).data(images[imageIndex].uri).size(Size.ORIGINAL)
                                .memoryCacheKey(images[imageIndex].id.hashCode().toString()).diskCacheKey(images[imageIndex].id.hashCode().toString())
                                .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED).build()
                        )
                        Image(modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .size(80.dp)
                            .clickable { component.onAction(ViewerAction.SetCurrentImage(imageIndex)) }
                            .border(
                                2.dp,
                                if (imageIndex == component.state.currentImage) MaterialTheme.colorScheme.secondary else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)), painter = painter, contentDescription = null, contentScale = ContentScale.FillBounds)
                    }
                    item { Spacer(modifier = Modifier.width(widthDP)) }
                }
            }
        }
    }
}

suspend fun LazyListState.animateScrollToItemCenter(index: Int) {
    layoutInfo.resolveItemOffsetToCenter(index)?.let {
        animateScrollToItem(index, it)
        return
    }
    scrollToItem(index)
    layoutInfo.resolveItemOffsetToCenter(index)?.let {
        animateScrollToItem(index, it)
    }
}

private fun LazyListLayoutInfo.resolveItemOffsetToCenter(index: Int): Int? {
    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index } ?: return null
    val containerSize = viewportSize.width - beforeContentPadding - afterContentPadding
    return -(containerSize - itemInfo.size) / 2
}