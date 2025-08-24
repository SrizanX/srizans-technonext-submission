package com.srizan.technonextcodingassessment.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    val posts = postRepository.getFavouritePosts().stateIn(
        viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList()
    )

    fun toggleFavourite(post: Post) {
        viewModelScope.launch {
            if (post.isFavourite) postRepository.unmarkPostAsFavourite(post.id)
            else postRepository.markPostAsFavourite(post.id)
        }
    }

    fun clearAllFavourites() {
        viewModelScope.launch {
            val favPosts = postRepository.getFavouritePosts()
            favPosts.collect { posts ->
                posts.forEach { post ->
                    postRepository.unmarkPostAsFavourite(post.id)
                }
            }
        }
    }
}