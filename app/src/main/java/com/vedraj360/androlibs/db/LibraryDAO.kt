package com.vedraj360.androlibs.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vedraj360.androlibs.models.Library

@Dao
interface LibraryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(library: Library): Long

    @Query("Select * from libraries")
    fun getAllLibraries(): LiveData<List<Library>>

    @Delete
    suspend fun deleteLibrary(library: Library)

    @Query("SELECT EXISTS(SELECT * FROM libraries WHERE libraryId == :id)")
    suspend fun isSaved(id: String): Boolean
}