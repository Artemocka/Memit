package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.domain.repository.task.DeleteTaskByIdRepo

class DeleteTaskByIdImpl : DeleteTaskByIdRepo {

    override fun invoke(id: Long) = DatabaseProviderWrap.taskDao.deleteById(id)

}