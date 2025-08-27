package com.srizan.technonextcodingassessment.favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.srizan.technonextcodingassessment.domain.repository.PostRepository
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class FavouritesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockPostRepository: PostRepository

    private lateinit var viewModel: FavouritesViewModel
    private val testDispatcher = StandardTestDispatcher()

    // Sample test data
    private val samplePosts = listOf(
        Post(
            userId = 1, id = 1, title = "Test Post 1", body = "Test body 1", isFavourite = true
        ), Post(
            userId = 2, id = 2, title = "Test Post 2", body = "Test body 2", isFavourite = true
        ), Post(
            userId = 3, id = 3, title = "Test Post 3", body = "Test body 3", isFavourite = true
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Setup default mock behavior
        whenever(mockPostRepository.getFavouritePosts()) doReturn flowOf(samplePosts)

        viewModel = FavouritesViewModel(mockPostRepository)
    }

    @Test
    fun `toggleFavourite should unmark post when it is favourite`() = runTest {
        // Given
        val favouritePost = Post(
            userId = 1,
            id = 1,
            title = "Favourite Post",
            body = "This is favourite",
            isFavourite = true
        )

        // When
        viewModel.toggleFavourite(favouritePost)
        advanceUntilIdle()

        // Then
        verify(mockPostRepository).unmarkPostAsFavourite(favouritePost.id)
    }

    @Test
    fun `toggleFavourite should mark post when it is not favourite`() = runTest {
        // Given
        val nonFavouritePost = Post(
            userId = 1,
            id = 1,
            title = "Non-Favourite Post",
            body = "This is not favourite",
            isFavourite = false
        )

        // When
        viewModel.toggleFavourite(nonFavouritePost)
        advanceUntilIdle()

        // Then
        verify(mockPostRepository).markPostAsFavourite(nonFavouritePost.id)
    }

    @Test
    fun `clearAllFavourites should unmark all favourite posts`() = runTest {
        // Given
        val favouritePosts = listOf(
            Post(userId = 1, id = 1, title = "Post 1", body = "Body 1", isFavourite = true),
            Post(userId = 2, id = 2, title = "Post 2", body = "Body 2", isFavourite = true),
            Post(userId = 3, id = 3, title = "Post 3", body = "Body 3", isFavourite = true)
        )
        whenever(mockPostRepository.getFavouritePosts()) doReturn flowOf(favouritePosts)

        // When
        viewModel.clearAllFavourites()
        advanceUntilIdle()

        // Then
        verify(mockPostRepository).unmarkPostAsFavourite(1)
        verify(mockPostRepository).unmarkPostAsFavourite(2)
        verify(mockPostRepository).unmarkPostAsFavourite(3)
    }

    @Test
    fun `clearAllFavourites should handle empty favourite list`() = runTest {
        // Given
        whenever(mockPostRepository.getFavouritePosts()) doReturn flowOf(emptyList())
        val emptyViewModel = FavouritesViewModel(mockPostRepository)

        // When
        emptyViewModel.clearAllFavourites()
        advanceUntilIdle()

        // Then
        // Should not crash and flow should be collected
        // Note: getFavouritePosts might be called multiple times due to StateIn and clearAllFavourites
        verify(mockPostRepository, org.mockito.kotlin.atLeastOnce()).getFavouritePosts()
    }

    @Test
    fun `multiple toggleFavourite calls should work correctly`() = runTest {
        // Given
        val post1 = Post(userId = 1, id = 1, title = "Post 1", body = "Body 1", isFavourite = true)
        val post2 = Post(userId = 2, id = 2, title = "Post 2", body = "Body 2", isFavourite = false)
        val post3 = Post(userId = 3, id = 3, title = "Post 3", body = "Body 3", isFavourite = true)

        // When
        viewModel.toggleFavourite(post1) // Should unmark
        viewModel.toggleFavourite(post2) // Should mark
        viewModel.toggleFavourite(post3) // Should unmark
        advanceUntilIdle()

        // Then
        verify(mockPostRepository).unmarkPostAsFavourite(1)
        verify(mockPostRepository).markPostAsFavourite(2)
        verify(mockPostRepository).unmarkPostAsFavourite(3)
    }
}
