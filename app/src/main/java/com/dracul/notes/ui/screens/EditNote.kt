package com.dracul.notes.ui.screens

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.notes.R
import com.dracul.notes.navigation.EditNoteComponent
import com.dracul.notes.navigation.events.EditNoteEvent.Back
import com.dracul.notes.navigation.events.EditNoteEvent.UpdateContent
import com.dracul.notes.navigation.events.EditNoteEvent.UpdateTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    component:EditNoteComponent
) {
    val title = component.title
    val content = component.content
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Note") },
                navigationIcon = { IconButton({ component.onEvent(Back) }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
            ,

            ) {
            OutlinedTextField(
                placeholder = { Text(text = "Title") },
                value = title.value,
                onValueChange = { component.onEvent(UpdateTitle(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),


                textStyle = TextStyle(fontSize = 24.sp)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))

            OutlinedTextField(
                placeholder = { Text(text = "Content") },
                value = content.value,
                onValueChange = { component.onEvent(UpdateContent(it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0F)
//                    .border(BorderStroke(0.dp, color = Color.Transparent), RectangleShape )
                ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
//                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RectangleShape,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default, keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences),
                textStyle = TextStyle(fontSize = 22.sp),
            )
            Row (
                modifier = Modifier
                    .imePadding()
            ){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.ic_bold), contentDescription ="Bold" )
                }
            }
        }
    }

}