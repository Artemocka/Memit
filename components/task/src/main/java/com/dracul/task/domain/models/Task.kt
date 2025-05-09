package com.dracul.task.domain.models

import com.dracul.common.models.Card


data class Task(
    var id: Long,
    var title: String,
    override val color: Int,
    override val pinned: Boolean = false,
    override val workerId: String? = null,
    override val reminderTimeStamp: Long? = null,
) : Card