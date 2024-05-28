package com.dracul.notes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dracul.notes.viewmodels.EditViewModel

@Composable
fun EditNoteScreen(
    id:Long,
    navController: NavController,
) {

    Scaffold {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
//            TextField(value = title , onValueChange = {title = it} , modifier = Modifier.fillMaxWidth())
//            TextField(value = content , onValueChange = {content = it}, modifier = Modifier.fillMaxSize() )
        }
    }

}