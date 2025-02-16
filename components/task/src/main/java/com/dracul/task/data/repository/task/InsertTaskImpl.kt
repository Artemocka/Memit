package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toData
import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.InsertTaskRepo

class InsertTaskImpl : InsertTaskRepo {

    override fun invoke(item: Task): Long =
        DatabaseProviderWrap.taskDao.insert(item.toData())

}