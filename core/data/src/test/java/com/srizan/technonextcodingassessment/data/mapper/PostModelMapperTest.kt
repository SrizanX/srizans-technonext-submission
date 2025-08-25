package com.srizan.technonextcodingassessment.data.mapper

import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for extension functions used in PostRepositoryImpl
 */
class PostMappingExtensionsTest {

    @Test
    fun `PostEntity toDomainModel should map all fields correctly`() {
        // Given
        val entity = PostEntity(
            id = 42,
            userId = 7,
            title = "Test Title",
            body = "Test Body",
            isFavourite = true
        )

        // When
        val domainModel = entity.toDomainModel()

        // Then
        assertEquals(42, domainModel.id)
        assertEquals(7, domainModel.userId)
        assertEquals("Test Title", domainModel.title)
        assertEquals("Test Body", domainModel.body)
        assertTrue(domainModel.isFavourite)
    }

    @Test
    fun `List of PostEntity toDomainModel should map all entities`() {
        // Given
        val entities = listOf(
            PostEntity(id = 1, userId = 1, title = "Title 1", body = "Body 1", isFavourite = false),
            PostEntity(id = 2, userId = 2, title = "Title 2", body = "Body 2", isFavourite = true)
        )

        // When
        val domainModels = entities.toDomainModel()

        // Then
        assertEquals(2, domainModels.size)
        assertEquals(1, domainModels[0].id)
        assertEquals("Title 1", domainModels[0].title)
        assertFalse(domainModels[0].isFavourite)

        assertEquals(2, domainModels[1].id)
        assertEquals("Title 2", domainModels[1].title)
        assertTrue(domainModels[1].isFavourite)
    }

    @Test
    fun `Flow of PostEntity list toDomainModel should transform flow correctly`() = runTest {
        // Given
        val entitiesFlow = flowOf(listOf(
            PostEntity(id = 1, userId = 1, title = "Flow Title", body = "Flow Body", isFavourite = false)
        ))

        // When
        val result = entitiesFlow.toDomainModel().toList()

        // Then
        assertEquals(1, result.size)
        val posts = result[0]
        assertEquals(1, posts.size)
        assertEquals("Flow Title", posts[0].title)
    }

    @Test
    fun `empty list mapping should return empty list`() {
        // Given
        val emptyEntities = emptyList<PostEntity>()

        // When
        val result = emptyEntities.toDomainModel()

        // Then
        assertTrue(result.isEmpty())
    }
}