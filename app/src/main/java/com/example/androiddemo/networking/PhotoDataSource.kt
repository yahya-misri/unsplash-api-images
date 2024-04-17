package com.example.androiddemo.networking

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androiddemo.model.ResponseMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDataSource (var clientId:String): PagingSource<Int, ResponseMain>() {
    private val TAG = PhotoDataSource::class.java.simpleName
    var apiHelper = ApiHelper()
    val data = ArrayList<ResponseMain>()
    var nextPageNumber: Int = 1
    var status=false
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseMain> {
        val pageNumber: Int = params.key ?: 1
        return try {
            var output = apiHelper.handleApi<List<ResponseMain>>(
                pageNumber,
                clientId = clientId
            )
            when (output) {
                is NetworkResult.Success -> {
                    status=true
                    data.addAll(output.data)
                     nextPageNumber = if (output.data.isEmpty()) {
                        0
                    } else {
                        nextPageNumber+1
                    }

                   // Log.e(TAG, "Success " + output.data.toString())

                }

                is NetworkResult.Error -> {
                    status=false
                    Log.e(TAG, "Error $output")
                }

                is NetworkResult.Exception -> {
                    status=false
                    Log.e(TAG, "Exception $output")
                }

            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey =nextPageNumber
            )
        } catch (e: Exception) {
            Log.e(TAG,"Exception ${e.printStackTrace()}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResponseMain>): Int? {
        return 1
    }
}