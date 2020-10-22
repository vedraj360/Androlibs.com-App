package com.vedraj360.androlibs.data.paging

import android.util.Log
import androidx.paging.PagingSource
import com.vedraj360.androlibs.api.LibraryApi
import com.vedraj360.androlibs.models.AndrolibModel
import com.vedraj360.androlibs.models.Library
import retrofit2.HttpException
import java.io.IOException

const val TAG = "AndroidPagingSource"

/* This class creates paging source for the required API */

class AndrolibsPagingSource(private val libraryApi: LibraryApi, private val query: String = "") :
    PagingSource<String, Library>() {
    private var direction = ""
    var prevKey = ""
    var nextKey = ""

        // It uses suspended function so we can call this in coroutuine or in another suspended function.

       override suspend fun load(params: LoadParams<String>): LoadResult<String, Library> {
        
//        Log.e(TAG, "load:  ${params.key}")
//        Log.e(TAG, "load:  ${params.loadSize}")
//        Log.e(TAG, "direction: $direction ")
           
        if (query.isNotBlank()) {
//            Log.e(TAG, "load: query $query")
            
            /* The API have cursor base implementation of the pages
            
               -> To move forword it have next param    
               -> To move backword it have previous param
               
            */
            
            return try {
                val response: AndrolibModel = if (params.key != null) {
                    libraryApi.getLibraryNext(params.key.toString(), params.loadSize)
                } else {
                    libraryApi.searchLibraries(query)
                }
//                Log.e(TAG, "load: $response")
                val repos = response.libraries
                
                // This check if response have next param or previous param.
                if (response.hasNext) {
                    direction = "next"
                } else if (response.hasPrevious) {
                    direction = "previous"
                }
          
                LoadResult.Page(
                    data = repos,
                    prevKey = if (response.hasPrevious) response.previous else null,
                    nextKey = null
                )
            } catch (exception: IOException) {
                Log.e(TAG, "IOException: $exception")
                LoadResult.Error(exception)
            } catch (exception: HttpException) {
                Log.e(TAG, "HttpException $exception")
                LoadResult.Error(exception)
            }
        } else {
            return try {
                if (params.key.toString() == nextKey) {
                    direction = "next"
                } else if (params.key.toString() == prevKey) {
                    direction = "previous"
                }

                val response: AndrolibModel = if (params.key != null) {
                    if (direction == "previous") {
                        libraryApi.getLibraryPrev(params.key.toString(), params.loadSize)
                    } else {
                        libraryApi.getLibraryNext(params.key.toString(), params.loadSize)
                    }
                } else {
                    libraryApi.getLibraries()
                }
//                Log.e(TAG, "load: xx $response")
                val repos = response.libraries
                prevKey = response.previous
                nextKey = response.next

                LoadResult.Page(
                    data = repos,
                    prevKey = if (response.hasPrevious) response.previous else null,
                    nextKey = if (response.hasNext) response.next else null
                )
            } catch (exception: IOException) {
                Log.e(TAG, "IOException: $exception")
                LoadResult.Error(exception)
            } catch (exception: HttpException) {
                Log.e(TAG, "HttpException $exception")
                LoadResult.Error(exception)
            }
        }

    }

    override val keyReuseSupported: Boolean
        get() = true
}
