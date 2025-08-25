package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.PostDao
import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import com.srizan.technonextcodingassessment.data.mapper.toDomainModel
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.network.PostRemoteDatasource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*

/**
 * Tests for PostRepositoryImpl - The actual repository implementation
 * These tests are meaningful because they test real business logic and data coordination
 */
class PostRepositoryImplTest {

    private val mockPostDao = mock<PostDao>()
    private val mockPostRemoteDatasource = mock<PostRemoteDatasource>()
    
    private val repository = PostRepositoryImpl(mockPostRemoteDatasource, mockPostDao)

    @Test
    fun `getPosts should return mapped domain models from dao`() = runTest {
        // Given
        val postEntities = listOf(
            PostEntity(id = 1, userId = 1, title = "Title 1", body = "Body 1", isFavourite = false),
            PostEntity(id = 2, userId = 1, title = "Title 2", body = "Body 2", isFavourite = true)
        )
        whenever(mockPostDao.getAll()).thenReturn(flowOf(postEntities))

        // When
        val result = repository.getPosts().toList()

        // Then
        assertEquals(1, result.size)
        val posts = result[0]
        assertEquals(2, posts.size)
        
        assertEquals(1, posts[0].id)
        assertEquals("Title 1", posts[0].title)
        assertEquals("Body 1", posts[0].body)
        assertFalse(posts[0].isFavourite)
        
        assertEquals(2, posts[1].id)
        assertEquals("Title 2", posts[1].title)
        assertEquals("Body 2", posts[1].body)
        assertTrue(posts[1].isFavourite)
    }

    @Test
    fun `getFavouritePosts should return only favourite posts from dao`() = runTest {
        // Given
        val favouriteEntities = listOf(
            PostEntity(id = 2, userId = 1, title = "Fav Title", body = "Fav Body", isFavourite = true)
        )
        whenever(mockPostDao.getAllFavourites()).thenReturn(flowOf(favouriteEntities))

        // When
        val result = repository.getFavouritePosts().toList()

        // Then
        assertEquals(1, result.size)
        val favourites = result[0]
        assertEquals(1, favourites.size)
        assertEquals(2, favourites[0].id)
        assertTrue(favourites[0].isFavourite)
    }

    @Test
    fun `refreshPosts should delegate to remote datasource`() = runTest {
        // Given
        val mockFlow = flow {
            emit(ApiResult.Loading)
            emit(ApiResult.Success(listOf(
                Post(id = 1, userId = 1, title = "Remote Post", body = "Remote Body", isFavourite = false)
            )))
        }
        whenever(mockPostRemoteDatasource.fetchPosts()).thenReturn(mockFlow)

        // When
        val result = repository.refreshPosts().toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Success)
        
        val successResult = result[1] as ApiResult.Success
        assertEquals(1, successResult.data.size)
        assertEquals("Remote Post", successResult.data[0].title)
        
        verify(mockPostRemoteDatasource).fetchPosts()
    }

    @Test
    fun `cachePosts should convert domain models to entities and insert to dao`() = runTest {
        // Given
        val posts = listOf(
            Post(id = 1, userId = 1, title = "Post 1", body = "Body 1", isFavourite = false),
            Post(id = 2, userId = 2, title = "Post 2", body = "Body 2", isFavourite = true)
        )

        // When
        repository.cachePosts(posts)

        // Then
        val expectedEntities = listOf(
            PostEntity(id = 1, userId = 1, title = "Post 1", body = "Body 1"),
            PostEntity(id = 2, userId = 2, title = "Post 2", body = "Body 2")
        )
        verify(mockPostDao).insertAll(expectedEntities)
    }

    @Test
    fun `markPostAsFavourite should call dao with correct post id`() = runTest {
        // Given
        val postId = 123

        // When
        repository.markPostAsFavourite(postId)

        // Then
        verify(mockPostDao).markPostAsFavourite(123)
    }

    @Test
    fun `unmarkPostAsFavourite should call dao with correct post id`() = runTest {
        // Given
        val postId = 456

        // When
        repository.unmarkPostAsFavourite(postId)

        // Then
        verify(mockPostDao).unmarkPostAsFavourite(456)
    }

    @Test
    fun `deletePost should call dao with correct post id`() = runTest {
        // Given
        val postId = 789

        // When
        repository.deletePost(postId)

        // Then
        verify(mockPostDao).deletePost(789)
    }

    @Test
    fun `deleteAllPosts should call dao deleteAll`() = runTest {
        // When
        repository.deleteAllPosts()

        // Then
        verify(mockPostDao).deleteAll()
    }

    @Test
    fun `cachePosts should handle empty list correctly`() = runTest {
        // Given
        val emptyPosts = emptyList<Post>()

        // When
        repository.cachePosts(emptyPosts)

        // Then
        verify(mockPostDao).insertAll(emptyList())
    }

    @Test
    fun `cachePosts should preserve isFavourite as false for new posts`() = runTest {
        // Given - Domain posts might have isFavourite = true, but cache should reset to false for new posts
        val posts = listOf(
            Post(id = 1, userId = 1, title = "New Post", body = "Body", isFavourite = true)
        )

        // When
        repository.cachePosts(posts)

        // Then - Entity should have isFavourite = false (default value) for new cached posts
        val expectedEntity = PostEntity(id = 1, userId = 1, title = "New Post", body = "Body")
        verify(mockPostDao).insertAll(listOf(expectedEntity))
    }
}


