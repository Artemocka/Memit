//package com.dracul.feature_edit.ui.screen
//
//import android.content.Intent
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.animation.Animatable
//import androidx.compose.animation.core.EaseInOutCubic
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.isImeVisible
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.ColorLens
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material.icons.filled.StarOutline
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardCapitalization
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.dracul.common.aliases.CommonStrings
//import com.dracul.common.utills.copyUriToInternalStorage
//import com.dracul.common.utills.getColor
//import com.dracul.common.utills.getRandomString
//import com.dracul.feature_edit.event.EditNoteEvent
//import com.dracul.feature_edit.event.EditTaskAction.*
//import com.dracul.feature_edit.event.EditTaskEvent
//import com.dracul.feature_edit.nav_component.EditTaskComponent
//import com.dracul.feature_edit.ui.components.ColorPickerDialog
////import com.dracul.feature_edit.ui.components.task.TaskEditor
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
//@Composable
//fun EditTaskScreen(component: EditTaskComponent) {
//    val state = component.state
//    val colorId = state.color
//    val color = getColor(id = colorId)
//    val animatedColor = remember { Animatable(color) }
//    val context = LocalContext.current
//    val events = component.events
//    val coroutineScope = rememberCoroutineScope()
//    LocalDensity.current
//    val isImeVisible = WindowInsets.isImeVisible
//    val focusManager = LocalFocusManager.current
//    val interactionSource = remember { MutableInteractionSource() }
//    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(100)) { uris ->
//        uris.let {
//            for (uri in uris) {
//                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                context.contentResolver.takePersistableUriPermission(uri, flag)
//                coroutineScope.launch(Dispatchers.IO) {
//                    val result = copyUriToInternalStorage(context, uri, getRandomString(8))
//                    result?.let {
//                        component.onEvent(SelectImage(it))
//                    }
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        events.collect {
//            when (it) {
//                EditTaskEvent.ShowMediaRequest -> {
//                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                }
//            }
//        }
//    }
//    LaunchedEffect(color) {
//        animatedColor.animateTo(color, animationSpec = tween(500, easing = EaseInOutCubic))
//    }
//    LaunchedEffect(isImeVisible) {
//        if (!isImeVisible) {
//            component.onEvent(CloseScreen)
//            focusManager.clearFocus()
//        }
//    }
//
//    Scaffold(
//        modifier = Modifier.background(color = Color.Transparent),
//        containerColor = animatedColor.value,
//        topBar = {
//            TopAppBar(
//                title = {
//                Text(text = if (component.state.isCreate) stringResource(CommonStrings.create) else stringResource(CommonStrings.edit))
//            }, navigationIcon = {
//                IconButton({ component.onEvent(Back) }) {
//                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                }
//            }, colors = TopAppBarDefaults.largeTopAppBarColors().copy(
//                containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent
//            ), actions = {
//                IconButton(onClick = { component.onEvent(ShowColorPicker) }) {
//                    Icon(imageVector = Icons.Filled.ColorLens, contentDescription = "Delete")
//                }
//                IconButton(onClick = { component.onEvent(DeleteTask) }) {
//                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
//                }
//                IconButton(onClick = { component.onEvent(SetPinned) }) {
//                    Icon(imageVector = if (state.pinned) Icons.Filled.Star else Icons.Filled.StarOutline, contentDescription = null)
//                }
//            })
//        },
//    ) { padding ->
//        if (component.state.showColorDialog) {
//            ColorPickerDialog(currentColor = colorId, onDismiss = { component.onEvent(HideColorPicker) }) {
//                component.onEvent(SetColor(it))
//            }
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = 8.dp)
//        ) {
//            OutlinedTextField(
//                value = component.state.title,
//                onValueChange = { component.onEvent(UpdateTitle(it)) },
//                placeholder = { Text(text = stringResource(CommonStrings.title_optional)) },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences
//                ),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.Transparent,
//                    unfocusedContainerColor = Color.Transparent,
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                ),
//                textStyle = TextStyle(fontSize = 20.sp)
//            )
//            HorizontalDivider(
//                Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface
//            )
//            TaskEditor(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Transparent),
//            ) {
//
//            }
//        }
//    }
//}
