package com.dracul.notes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dracul.notes.CreateNote
import com.dracul.notes.EditNote
import com.dracul.notes.db.Note
import com.dracul.notes.viewmodels.MainViewModel


@Composable
fun MainScreen(
    vm: MainViewModel = viewModel(),
    navController: NavController,
) {
    val notes = vm.notes.collectAsState(initial = emptyList())
   Scaffold(
       floatingActionButton = {
           FloatingActionButton(onClick = {navController.navigate(CreateNote)}) {
               Icon(imageVector = Icons.Default.Add, contentDescription = "add")
           }
       },
   ) {padding->
       LazyVerticalStaggeredGrid(
           columns = StaggeredGridCells.Adaptive(200.dp),
           contentPadding = padding,
       ) {
           items(notes.value.size) {note->
               ItemGrid(item = notes.value[note]){id->
                   navController.navigate(EditNote(id))
               }
           }
       }
   }
}


@Composable
fun ItemGrid(item: Note, onItemClick:(Int)->Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable {
                onItemClick(item.id)
            },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.title)
            Text(text = item.content)
        }
    }
}