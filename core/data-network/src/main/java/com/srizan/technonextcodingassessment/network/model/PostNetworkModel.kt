package com.srizan.technonextcodingassessment.network.model

import androidx.annotation.Keep
import com.srizan.technonextcodingassessment.domain.Mapper
import com.srizan.technonextcodingassessment.model.Post
import javax.inject.Inject

@Keep
data class PostNetworkModel(
    val userId: Int, val id: Int, val title: String, val body: String
)


class PostApiMapper @Inject constructor() : Mapper<List<PostNetworkModel>, List<Post>> {
    override fun map(type: List<PostNetworkModel>): List<Post> {
        return type.map {
            Post(
                userId = it.userId,
                id = it.id,
                title = it.title,
                body = it.body,
                isFavourite = false
            )
        }
    }
}