package com.dracul.common.models

interface Card {
    val color: Int
    val pinned: Boolean
    val workerId: String?
    val reminderTimeStamp: Long?
}