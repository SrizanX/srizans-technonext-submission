package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.PostRemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDatasource: PostRemoteDatasource, private val postDao: PostDao
) : PostRepository {
    override fun getPosts(): Flow<List<Post>> {
        return postDao.getAll().toDomainModel()
    }

    override fun getFavouritePosts(): Flow<List<Post>> {
        return postDao.getAllFavourites().toDomainModel()
    }

    override suspend fun refreshPosts(): Flow<ApiResult<List<Post>>> {
        return postRemoteDatasource.fetchPosts()
    }

    override suspend fun cachePosts(data: List<Post>) {
        val postEntities = data.map { post ->
            PostEntity(
                id = post.id, userId = post.userId, title = post.title, body = post.body
            )
        }
        postDao.insertAll(postEntities)
    }

    override suspend fun markPostAsFavourite(postId: Int) {
        postDao.markPostAsFavourite(postId)
    }

    override suspend fun unmarkPostAsFavourite(postId: Int) {
        postDao.unmarkPostAsFavourite(postId)
    }

    override suspend fun deletePost(postId: Int) {
        postDao.deletePost(postId)
    }

    override suspend fun deleteAllPosts() {
        postDao.deleteAll()
    }
}

fun Flow<List<PostEntity>>.toDomainModel(): Flow<List<Post>> {
    return this.map { postEntities ->
        postEntities.toDomainModel()
    }
}

fun List<PostEntity>.toDomainModel(): List<Post> {
    return this.map { postEntity ->
        postEntity.toDomainModel()
    }
}

fun PostEntity.toDomainModel(): Post {
    return Post(
        id = this.id,
        userId = this.userId,
        title = this.title,
        body = this.body,
        isFavourite = this.isFavourite
    )
}