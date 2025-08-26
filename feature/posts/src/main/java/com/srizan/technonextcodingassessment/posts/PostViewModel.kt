package com.srizan.technonextcodingassessment.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Posts feature that manages the UI state and business logic.
 * 
 * This ViewModel handles:
 * - Loading and displaying paginated posts
 * - Search functionality with debouncing
 * - Post refresh from remote source
 * - Favorite/unfavorite posts
 * - Delete all posts
 * - User sign out
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Paginated posts flow with search functionality.
     * Debounces search queries to avoid excessive API calls.
     */
    val pagedPosts = _uiState
        .map { it.searchQuery }
        .debounce(300) // Debounce search queries
        .distinctUntilChanged()
        .flatMapLatest { query ->
            postRepository.getAllPaginated(pageSize = 10, query = query)
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            // Check DB size once and refresh if empty
            val count = postRepository.getPostCount()
            if (count == 0) {
                // No cached data, need to fetch from remote
                _uiState.update { it.copy(hasCheckedCache = true) }
                refreshPosts()
            } else {
                // We have cached data, no need for initial loading
                _uiState.update { 
                    it.copy(
                        isInitialLoading = false,
                        hasCheckedCache = true
                    ) 
                }
            }
        }
    }

    /**
     * Refreshes posts from the remote source and caches them locally.
     */
    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            
            try {
                postRepository.refreshPosts().collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            postRepository.cachePosts(result.data)
                            _uiState.update { 
                                it.copy(
                                    isRefreshing = false,
                                    isInitialLoading = false
                                ) 
                            }
                        }
                        is ApiResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isRefreshing = false,
                                    isInitialLoading = false,
                                    errorMessage = result.errorMessage
                                )
                            }
                        }
                        ApiResult.Loading -> {
                            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isInitialLoading = false,
                        errorMessage = "Failed to refresh posts: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Updates the search query and triggers a new search.
     * 
     * @param query The search query string, null or empty to clear search
     */
    fun updateSearchQuery(query: String?) {
        _uiState.update {
            it.copy(searchQuery = query?.takeIf { it.isNotBlank() })
        }
    }

    /**
     * Toggles the favorite status of a post.
     * 
     * @param post The post to toggle favorite status for
     */
    fun toggleFavourite(post: Post) {
        viewModelScope.launch {
            try {
                if (post.isFavourite) {
                    postRepository.unmarkPostAsFavourite(post.id)
                } else {
                    postRepository.markPostAsFavourite(post.id)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to update favorite: ${e.message}")
                }
            }
        }
    }

    /**
     * Deletes all posts from local storage.
     */
    fun deleteAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.deleteAllPosts()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete posts: ${e.message}")
                }
            }
        }
    }

    /**
     * Signs out the current user and clears preferences.
     */
    fun signOut() {
        viewModelScope.launch {
            try {
                preferenceRepository.setUserLoggedInStatus(false)
                preferenceRepository.clearPreferences()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to sign out: ${e.message}")
                }
            }
        }
    }

    /**
     * Clears any error message from the UI state.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Called when paging data is loaded for the first time to stop initial loading state.
     */
    fun onDataLoaded() {
        _uiState.update { it.copy(isInitialLoading = false) }
    }

    /**
     * UI state for the Posts screen.
     * 
     * @param searchQuery Current search query, null if no search is active
     * @param isRefreshing Whether posts are currently being refreshed
     * @param errorMessage Error message to display, null if no error
     * @param isInitialLoading Whether the screen is loading for the first time
     * @param hasCheckedCache Whether we've checked the cache on initialization
     */
    data class UiState(
        val searchQuery: String? = null,
        val isRefreshing: Boolean = false,
        val errorMessage: String? = null,
        val isInitialLoading: Boolean = true,
        val hasCheckedCache: Boolean = false,
    )
}