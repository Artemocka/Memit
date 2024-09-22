package com.dracul.feature_viewer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.feature_viewer.event.ViewerEvent
import com.dracul.feature_viewer.nav_component.ViewerComponent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


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




