package com.dracul.notes.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class ScreenBComponent(
    val text:String,
    componentContext: ComponentContext,
    private val onGoBack:()->Unit,
):ComponentContext by componentContext {
    fun goBack(){
        onGoBack()
    }
}