package com.srizan.technonextcodingassessment.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.PostRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val posts = MutableStateFlow(emptyList<Post>())

    init {
        viewModelScope.launch {
            postRepository.fetchPosts().collect { result ->
                when (result) {
                    is ApiResult.Error<*> -> {
                    }
                    is ApiResult.Loading<*> -> {
                    }
                    is ApiResult.Success<List<Post>> -> {
                        posts.value = result.data
                    }
                }
            }
        }
    }
}