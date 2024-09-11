package com.dracul.feature_main.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dracul.common.aliases.CommonDrawables
import com.dracul.common.aliases.CommonStrings
import com.dracul.common.models.CircleColor
import com.dracul.common.utills.getColor
import com.dracul.common.utills.noRippleClickable
import com.dracul.feature_main.event.MainAction
import com.dracul.feature_main.event.MainAction.DeleteNoteModal
import com.dracul.feature_main.event.MainAction.EditNoteModal
import com.dracul.feature_main.event.MainAction.HideBottomSheet
import com.dracul.feature_main.event.MainAction.ShareNoteModal
import com.dracul.feature_main.event.MainAction.ShowReminder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ActionsBottomSheet(
    onDismiss: () -> Unit,
    onAction: (action: MainAction) -> Unit,
    onColorClick: (CircleColor) -> Unit,
    colorList: State<List<CircleColor>>,
) {


    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val editLambda = remember<() -> Unit> {
        { onAction(EditNoteModal) }
    }

    val shareLambda = remember<() -> Unit> {
        { onAction(ShareNoteModal) }
    }
    val deleteLambda = remember<() -> Unit> {
        { onAction(DeleteNoteModal) }
    }
    val remindLambda = remember<() -> Unit> {
        {
            onAction(ShowReminder)
        }
    }

    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets(bottom = 0)),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(bottom = 8.dp)
        ) {
            repeat(colorList.value.size) {
                when (it) {
                    0 -> CircleColorItem(
                        modifier = Modifier.padding(start = 8.dp),
                        item = colorList.value[it],
                        onClick = onColorClick
                    )

                    colorList.value.lastIndex -> CircleColorItem(
                        modifier = Modifier.padding(end = 8.dp),
                        item = colorList.value[it],
                        onClick = onColorClick
                    )

                    else -> CircleColorItem(item = colorList.value[it], onClick = onColorClick)
                }
            }
        }
        BottomSheetRow(
            image = Icons.Filled.Edit,
            text = stringResource(CommonStrings.edit),
            onClick = {
                editLambda()
                scope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    onAction(HideBottomSheet)

                }
            })
        BottomSheetRow(
            image = Icons.Filled.Share,
            text = stringResource(CommonStrings.share),
            onClick = {
                shareLambda()
                scope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    onAction(HideBottomSheet)

                }
            })

            BottomSheetRow(image = Icons.Filled.AddAlert,
                text = stringResource(CommonStrings.remind),
                onClick = {
                    remindLambda()
                    scope.launch {
                        modalBottomSheetState.hide()
                    }.invokeOnCompletion {
                        onAction(HideBottomSheet)
                    }
                })

        BottomSheetRow(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            image = Icons.Filled.Delete,
            text = stringResource(id = CommonStrings.delete),
            onClick = {
                deleteLambda()
                scope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    onAction(HideBottomSheet)
                }
            })
    }
}

@Composable
fun BottomSheetRow(
    modifier: Modifier = Modifier, image: Painter, text: String, onClick: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Image(
                image,
                contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = text,
                color = if (text == stringResource(CommonStrings.delete)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun CircleColorItem(
    modifier: Modifier = Modifier, item: CircleColor, onClick: (CircleColor) -> Unit
) {
    val color = getColor(id = item.color)
    AnimatedContent(targetState = item.selected, transitionSpec = {
        scaleIn(
            tween(300, easing = EaseIn), initialScale = 0.6f
        ) togetherWith scaleOut(
            tween(300, easing = EaseOut), targetScale = 0.6f
        )
    }) {
        if (it) {
            Image(painter = painterResource(id = CommonDrawables.ic_selected_circle),
                colorFilter = ColorFilter.tint(color),
                contentDescription = "Color circle",
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .noRippleClickable { onClick(item) })
        } else {
            Image(painter = painterResource(id = CommonDrawables.ic_circle),
                colorFilter = ColorFilter.tint(color),
                contentDescription = "Color circle",
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .noRippleClickable { onClick(item) })
        }
    }
}

@Composable
fun BottomSheetRow(
    modifier: Modifier = Modifier, image: ImageVector, text: String, onClick: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Image(
                image,
                contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = if (text == "Delete") ColorFilter.tint(MaterialTheme.colorScheme.error) else ColorFilter.tint(
                    MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = text,
                color = if (text == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}

