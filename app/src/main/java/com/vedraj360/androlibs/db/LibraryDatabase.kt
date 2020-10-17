package com.vedraj360.androlibs.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vedraj360.androlibs.models.Library

@Database(
    entities = [Library::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun getLibraryDao(): LibraryDAO
}