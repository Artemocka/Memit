package com.dracul.database.images


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.dracul.database.converters.BitmapConverter
import kotlinx.coroutines.flow.Flow

@Dao
interface ImgageDao {

    @Query("SELECT * FROM ImageEntity WHERE id = :id")
    fun getById(id: Long): ImageEntity

    @Query("SELECT * FROM ImageEntity WHERE parentId = :parentId")
    fun getAllById(parentId: Long): Flow<List<ImageEntity>>

    @Insert
    fun insert(item: ImageEntity)

    @Update
    fun update(item: ImageEntity)

    @Delete
    fun delete(item: ImageEntity)

    @Query("DELETE FROM ImageEntity  where id = :parentId")
    fun deleteByParentId(parentId: Long)

}