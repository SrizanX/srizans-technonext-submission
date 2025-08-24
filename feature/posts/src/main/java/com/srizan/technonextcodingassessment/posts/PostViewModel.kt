package com.srizan.technonextcodingassessment.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val pagedPosts = _uiState.flatMapLatest { query ->
        postRepository.getAllPaginated(pageSize = 5, query = query.searchQuery)
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            // Check DB size once
            val count = postRepository.getPostCount()
            if (count == 0) refreshPosts()
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            postRepository.refreshPosts().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        //insert posts into the database
                        postRepository.cachePosts(result.data)
                        _uiState.update { it.copy(isRefreshing = false) }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isRefreshing = false, errorMessage = result.errorMessage
                            )
                        }
                    }

                    ApiResult.Loading -> {
                        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
                    }
                }
            }
        }
    }

    fun updateSearchQuery(query: String?) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }


    fun toggleFavourite(post: Post) {
        viewModelScope.launch {
            if (post.isFavourite) postRepository.unmarkPostAsFavourite(post.id)
            else postRepository.markPostAsFavourite(post.id)
        }
    }

    fun deleteAllPosts() {
        viewModelScope.launch {
            postRepository.deleteAllPosts()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            preferenceRepository.setUserLoggedInStatus(false)
            preferenceRepository.clearPreferences()
        }
    }

    data class UiState(
        val searchQuery: String? = null,
        val isRefreshing: Boolean = false,
        val errorMessage: String? = null,
    )
}