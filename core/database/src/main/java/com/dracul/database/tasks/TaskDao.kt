package com.dracul.database.tasks


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getById(id: Long): TaskEntity

    @Query("SELECT * FROM NoteEntity ORDER BY id")
    fun getAll(): Flow<List<TaskEntity>>

    @Insert
    fun insert(item: TaskEntity): Long

    @Update
    fun update(item: TaskEntity)

    @Query("UPDATE NoteEntity SET pinned = :pinned  WHERE id = :id")
    fun updatePinnedById(id: Long, pinned: Boolean)

    @Query("UPDATE NoteEntity SET workerId = :workerId , reminderTimeStamp = :reminderTimeStamp WHERE id = :id")
    fun updateWorkerById(id: Long, workerId: String?, reminderTimeStamp: Long?)

    @Delete
    fun delete(item: TaskEntity)

    @Query("DELETE FROM NoteEntity where id=  :id")
    fun deleteById(id: Long)

}