package com.dracul.notes.viewmodels

import androidx.lifecycle.ViewModel
import com.example.myapplication.DatabaseProviderWrap

class MainViewModel:ViewModel() {

    val notes = DatabaseProviderWrap.noteDao.getAll()

}