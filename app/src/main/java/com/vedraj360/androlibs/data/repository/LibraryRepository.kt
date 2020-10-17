package com.vedraj360.androlibs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vedraj360.androlibs.api.LibraryApi
import com.vedraj360.androlibs.data.paging.AndrolibsPagingSource
import com.vedraj360.androlibs.db.LibraryDAO
import com.vedraj360.androlibs.models.Library
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private val libraryApi: LibraryApi,
    private val db: LibraryDAO
) {


    fun getLibrary() = Pager(
        config = PagingConfig(
            pageSize = 15,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { AndrolibsPagingSource(libraryApi) }
    ).liveData

    fun getSearchResult(query: String) = Pager(
        config = PagingConfig(
            pageSize = 15,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { AndrolibsPagingSource(libraryApi, query) }
    ).liveData

    suspend fun upsert(library: Library) = db.upsert(library)

    fun getSavedLibraries() = db.getAllLibraries()

    suspend fun deleteArticle(delete: Library) = db.deleteLibrary(delete)

    suspend fun isLibrarySaved(libraryId: String) = db.isSaved(libraryId)

}