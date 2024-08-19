package com.dracul.database.notes


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getById(id: Long): NoteEntity

    @Query("SELECT * FROM NoteEntity ORDER BY id")
    fun getAll(): Flow<List<NoteEntity>>

    @Insert
    fun insert(item: NoteEntity)

    @Update
    fun update(item: NoteEntity)

    @Query("UPDATE NoteEntity SET pinned = :pinned  WHERE id = :id")
    fun updatePinnedById(id: Long, pinned: Boolean)

    @Query("UPDATE NoteEntity SET workerId = :workerId , reminderTimeStamp = :reminderTimeStamp WHERE id = :id")
    fun updateWorkerById(id: Long, workerId: String?, reminderTimeStamp: Long?)

    @Delete
    fun delete(item: NoteEntity)

    @Query("DELETE FROM NoteEntity where id=  :id")
    fun deleteById(id: Long)

}