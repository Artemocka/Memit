//package com.dracul.feature_edit.ui.components.task
//
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.text.input.clearText
//import androidx.compose.foundation.text.input.rememberTextFieldState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedIconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardCapitalization
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.sp
//import com.dracul.common.aliases.CommonStrings
//import com.dracul.feature_edit.event.EditNoteAction.UpdateTitle
//
//@Composable
//fun TaskEditor(modifier: Modifier = Modifier, onAddClick: (String) -> Unit) {
//    val textFieldState = rememberTextFieldState()
//
//    Row(
//        modifier = modifier
//    ) {
//        OutlinedTextField(
//            value = textFieldState,
//            onValueChange = { component.onEvent(UpdateTitle(it)) },
//            placeholder = { Text(text = stringResource(CommonStrings.title_optional)) },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                imeAction = ImeAction.Done, keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.Sentences
//            ),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                focusedBorderColor = Color.Transparent,
//                unfocusedBorderColor = Color.Transparent,
//            ),
//            textStyle = TextStyle(fontSize = 20.sp)
//        )
//        OutlinedIconButton(
//            modifier = Modifier.weight(1f),
//            onClick = {
//                onAddClick(textFieldState.text.toString())
//                textFieldState.clearText()
//            },
//        ) {
//            Icon(Icons.Default.Add, contentDescription = null)
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun TaskEditorPreview(modifier: Modifier = Modifier) {
//    TaskEditor {
//
//    }
//}