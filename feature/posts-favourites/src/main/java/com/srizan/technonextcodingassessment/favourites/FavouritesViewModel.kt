package com.srizan.technonextcodingassessment.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.PostRepository
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class FavouritePostsUiState(
    val posts: List<Post> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    val posts = MutableStateFlow(
        FavouritePostsUiState(
            posts = emptyList(), isRefreshing = false, errorMessage = null
        )
    )

    init {
        viewModelScope.launch {
            postRepository.getFavouritePosts().collect { result ->
                posts.update { it.copy(posts = result) }
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