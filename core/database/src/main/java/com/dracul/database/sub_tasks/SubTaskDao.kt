package com.dracul.database.sub_tasks


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {

    @Query("SELECT * FROM SubTaskEntity WHERE id = :id")
    fun getById(id: Long): SubTaskEntity

    @Query("SELECT * FROM SubTaskEntity WHERE parentId = :parentId")
    fun getAllById(parentId: Long): Flow<List<SubTaskEntity>>

    @Insert
    fun insert(item: SubTaskEntity): Long

    @Update
    fun update(item: SubTaskEntity)

    @Delete
    fun delete(item: SubTaskEntity)

    @Query("DELETE FROM SubTaskEntity where id=  :id")
    fun deleteById(id: Long)

}