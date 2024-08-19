package com.dracul.notes.domain.repository

interface UpdateWorkerByIdRepo {
    operator fun invoke(id:Long, workerId:String?, reminderTimestamp: Long?)
}