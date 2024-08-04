package com.dracul.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dracul.database.db.DatabaseProviderWrap

class ActivityViewModel(app: Application) : AndroidViewModel(app){
    init {
        DatabaseProviderWrap.createDao(this.getApplication())
    }
}