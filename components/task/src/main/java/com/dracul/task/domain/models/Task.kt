package com.dracul.task.domain.models

import androidx.room.PrimaryKey
import com.dracul.common.models.Card


data class Task(
    @PrimaryKey
    var id: Int,
    var title: String,
    override val color: Int,
    override val pinned: Boolean = false,
    override val workerId: String? = null,
    override val reminderTimeStamp: Long? = null,
) : Card