package com.srizan.technonextcodingassessment.posts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for PostViewModel.
 * Tests the business logic and state management of the posts feature.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    private lateinit var viewModel: PostViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val samplePosts = listOf(
        Post(id = 1, title = "Test Post 1", body = "Body 1", userId = 1, isFavourite = false),
        Post(id = 2, title = "Test Post 2", body = "Body 2", userId = 1, isFavourite = true)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mock behavior
        runTest {
            whenever(postRepository.getPostCount()).thenReturn(5)
            whenever(postRepository.getAllPaginated(any(), any())).doReturn(flowOf(PagingData.empty()))
        }
        
        viewModel = PostViewModel(postRepository, preferenceRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should refresh posts when database is empty`() = runTest {
        // Given
        whenever(postRepository.getPostCount()).thenReturn(0)
        whenever(postRepository.refreshPosts()).doReturn(flowOf(ApiResult.Success(samplePosts)))

        // When
        viewModel = PostViewModel(postRepository, preferenceRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(postRepository).refreshPosts()
        verify(postRepository).cachePosts(samplePosts)
    }

    @Test
    fun `init should not refresh posts when database has data`() = runTest {
        // Given
        whenever(postRepository.getPostCount()).thenReturn(10)

        // When
        viewModel = PostViewModel(postRepository, preferenceRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(postRepository, never()).refreshPosts()
    }

    @Test
    fun `refreshPosts should update loading state correctly on success`() = runTest {
        // Given
        whenever(postRepository.refreshPosts()).doReturn(flowOf(
            ApiResult.Loading,
            ApiResult.Success(samplePosts)
        ))

        // When
        viewModel.refreshPosts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.first()
        assertFalse(finalState.isRefreshing)
        assertNull(finalState.errorMessage)
        verify(postRepository).cachePosts(samplePosts)
    }

    @Test
    fun `refreshPosts should handle error correctly`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(postRepository.refreshPosts()).doReturn(flowOf(
            ApiResult.Loading,
            ApiResult.Error(errorMessage = errorMessage, code = null)
        ))

        // When
        viewModel.refreshPosts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.first()
        assertFalse(finalState.isRefreshing)
        assertEquals(errorMessage, finalState.errorMessage)
    }

    @Test
    fun `updateSearchQuery should update search query in state`() = runTest {
        // Given
        val searchQuery = "test query"

        // When
        viewModel.updateSearchQuery(searchQuery)

        // Then
        val state = viewModel.uiState.first()
        assertEquals(searchQuery, state.searchQuery)
    }

    @Test
    fun `updateSearchQuery should clear search query when empty string provided`() = runTest {
        // Given
        viewModel.updateSearchQuery("initial query")

        // When
        viewModel.updateSearchQuery("")

        // Then
        val state = viewModel.uiState.first()
        assertNull(state.searchQuery)
    }

    @Test
    fun `updateSearchQuery should clear search query when blank string provided`() = runTest {
        // Given
        viewModel.updateSearchQuery("initial query")

        // When
        viewModel.updateSearchQuery("   ")

        // Then
        val state = viewModel.uiState.first()
        assertNull(state.searchQuery)
    }

    @Test
    fun `toggleFavourite should mark post as favourite when not favourite`() = runTest {
        // Given
        val post = samplePosts[0] // isFavourite = false

        // When
        viewModel.toggleFavourite(post)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(postRepository).markPostAsFavourite(post.id)
        verify(postRepository, never()).unmarkPostAsFavourite(any())
    }

    @Test
    fun `toggleFavourite should unmark post as favourite when favourite`() = runTest {
        // Given
        val post = samplePosts[1] // isFavourite = true

        // When
        viewModel.toggleFavourite(post)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(postRepository).unmarkPostAsFavourite(post.id)
        verify(postRepository, never()).markPostAsFavourite(any())
    }

    @Test
    fun `toggleFavourite should handle error correctly`() = runTest {
        // Given
        val post = samplePosts[0]
        whenever(postRepository.markPostAsFavourite(any())).doThrow(RuntimeException("Database error"))

        // When
        viewModel.toggleFavourite(post)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state.errorMessage?.contains("Failed to update favorite") == true)
    }

    @Test
    fun `deleteAllPosts should call repository deleteAllPosts`() = runTest {
        // When
        viewModel.deleteAllPosts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(postRepository).deleteAllPosts()
    }

    @Test
    fun `deleteAllPosts should handle error correctly`() = runTest {
        // Given
        whenever(postRepository.deleteAllPosts()).doThrow(RuntimeException("Database error"))

        // When
        viewModel.deleteAllPosts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state.errorMessage?.contains("Failed to delete posts") == true)
    }

    @Test
    fun `signOut should update preferences correctly`() = runTest {
        // When
        viewModel.signOut()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(preferenceRepository).setUserLoggedInStatus(false)
        verify(preferenceRepository).clearPreferences()
    }

    @Test
    fun `signOut should handle error correctly`() = runTest {
        // Given
        whenever(preferenceRepository.setUserLoggedInStatus(any())).doThrow(RuntimeException("Preference error"))

        // When
        viewModel.signOut()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertTrue(state.errorMessage?.contains("Failed to sign out") == true)
    }

    @Test
    fun `clearError should clear error message from state`() = runTest {
        // Given - Set an error first
        whenever(postRepository.refreshPosts()).doReturn(flowOf(ApiResult.Error("Test error", code = null)))
        viewModel.refreshPosts()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        val state = viewModel.uiState.first()
        assertNull(state.errorMessage)
    }
}