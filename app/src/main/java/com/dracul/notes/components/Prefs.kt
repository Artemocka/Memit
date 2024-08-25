package com.dracul.notes.components

import android.content.Context
const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
class Prefs(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("memit_prefs", Context.MODE_PRIVATE)

    val isFirstLaunch = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true).also {
        if(it){
            sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH, false).commit()
        }
    }

}