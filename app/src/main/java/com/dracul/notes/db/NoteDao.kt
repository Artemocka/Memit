package com.dracul.notes.db;

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getById(id: Long): Note

    @Query("SELECT * FROM Note ORDER BY id")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT COUNT(*) FROM Note")
    fun count(): Int

    @Query("DELETE FROM Note")
    fun clear()

    @Insert
    fun insert(item: Note): Long

    @Update
    fun update(item: Note)

    @Delete
    fun delete(item: Note)
}