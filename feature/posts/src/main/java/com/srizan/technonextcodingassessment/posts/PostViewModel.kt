package com.srizan.technonextcodingassessment.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val posts = MutableStateFlow(
        PostUiState(
            posts = emptyList(), isRefreshing = false, errorMessage = null
        )
    )

    init {
        viewModelScope.launch {
            postRepository.getPosts().collectIndexed { index, result ->
                posts.update { it.copy(posts = result) }

                if (index == 0 && result.isEmpty()) {
                    // If the first emission is empty, we can trigger a refresh
                    refreshPosts()
                }
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            posts.update { it.copy(isRefreshing = true, errorMessage = null) }
            postRepository.refreshPosts().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        //insert posts into the database
                        postRepository.cachePosts(result.data)
                        posts.update { it.copy(isRefreshing = false) }
                    }

                    is ApiResult.Error -> {
                        posts.update {
                            it.copy(
                                isRefreshing = false, errorMessage = result.errorMessage
                            )
                        }
                    }

                    ApiResult.Loading -> {
                        posts.update { it.copy(isRefreshing = true, errorMessage = null) }
                    }
                }
            }
        }
    }


    fun toggleFavourite(post: Post) {
        viewModelScope.launch {
            if (post.isFavourite) postRepository.unmarkPostAsFavourite(post.id)
            else postRepository.markPostAsFavourite(post.id)
        }
    }
}