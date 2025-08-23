package com.srizan.technonextcodingassessment.favourites

import android.util.Log
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

    init {
        Log.d("asd", "Fav vm init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("asd", "Fav vm cleared")
    }


    fun toggleFavourite(post: Post) {
        viewModelScope.launch {
            if (post.isFavourite) postRepository.unmarkPostAsFavourite(post.id)
            else postRepository.markPostAsFavourite(post.id)
        }
    }
}