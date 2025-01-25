package com.dracul.common.utills

const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

fun getRandomString(length: Int): String = (1..length).map { CHARS.random() }.joinToString("")