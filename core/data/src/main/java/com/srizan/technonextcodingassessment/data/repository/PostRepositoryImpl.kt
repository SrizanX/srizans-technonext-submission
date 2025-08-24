package com.srizan.technonextcodingassessment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import com.srizan.technonextcodingassessment.data.mapper.toDomainModel
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.PostRemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDatasource: PostRemoteDatasource,
    private val postDao: PostDao,
) : PostRepository {
    override fun getPosts(): Flow<List<Post>> {
        return postDao.getAll().toDomainModel()
    }

    override fun getAllPaginated(query: String?, pageSize: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { postDao.getPostsPagingSource(query) }).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }

    override fun getFavouritePosts(): Flow<List<Post>> {
        return postDao.getAllFavourites().toDomainModel()
    }

    override suspend fun getPostCount(): Int {
        return postDao.getPostCount()
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