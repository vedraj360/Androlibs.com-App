package com.vedraj360.androlibs.ui.library

import android.os.Parcelable
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vedraj360.androlibs.data.repository.LibraryRepository
import com.vedraj360.androlibs.models.Library
import kotlinx.coroutines.launch

class LibraryViewModel @ViewModelInject constructor(
    private val libraryRepository: LibraryRepository,
    @Assisted state: SavedStateHandle
) :
    ViewModel() {

    var listState: Parcelable? = null
    var isSearched = false
    var searchValue = "main"

    private val saveQuery = "saved_query"


    private val currentQuery = state.getLiveData(saveQuery, "")


    val searchQuery = currentQuery.switchMap { q ->
//        Log.e("TAG", "this is called: $q")
        libraryRepository.getSearchResult(q).cachedIn(viewModelScope)
    }

    fun libraryRes(): LiveData<PagingData<Library>> {
        return libraryRepository.getLibrary().cachedIn(viewModelScope)
    }

    fun search(query: String) {
        currentQuery.value = query
    }


    fun saveLibrary(library: Library) = viewModelScope.launch {
        libraryRepository.upsert(library)
    }


    fun getFavouriteLibrary() = libraryRepository.getSavedLibraries()

    fun deleteLibrary(library: Library) = viewModelScope.launch {
        libraryRepository.deleteArticle(library)
    }

    fun isLibSaved(libraryId: String): MutableLiveData<Boolean> {
        val isSaved = MutableLiveData(false)
        viewModelScope.launch {
            isSaved.postValue(libraryRepository.isLibrarySaved(libraryId))
        }
        return isSaved
    }
}

