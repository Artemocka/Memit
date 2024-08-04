package com.dracul.common.utills

import android.util.Log

fun Any.poop(s:Any){
    Log.e("techtask","[${javaClass.simpleName}] $s")
}