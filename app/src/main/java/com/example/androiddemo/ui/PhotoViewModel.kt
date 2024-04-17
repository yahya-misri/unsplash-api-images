package com.example.androiddemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androiddemo.model.ResponseMain
import com.example.androiddemo.networking.PhotoRespository
import kotlinx.coroutines.flow.Flow

class PhotoViewModel(photoRespository: PhotoRespository):ViewModel() {
    val items: Flow<PagingData<ResponseMain>> = Pager(
        config = PagingConfig(pageSize = 10, 1,enablePlaceholders = false,10),
        pagingSourceFactory = { photoRespository.photoPagingSource() }
    )
        .flow
        // cachedIn allows paging to remain active in the viewModel scope, so even if the UI
        // showing the paged data goes through lifecycle changes, pagination remains cached and
        // the UI does not have to start paging from the beginning when it resumes.
        .cachedIn(viewModelScope)
}