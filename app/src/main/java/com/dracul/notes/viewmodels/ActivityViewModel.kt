package com.dracul.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.myapplication.DatabaseProviderWrap

class ActivityViewModel(app: Application) : AndroidViewModel(app){
    init {
        DatabaseProviderWrap.createDao(this.getApplication())
    }
}