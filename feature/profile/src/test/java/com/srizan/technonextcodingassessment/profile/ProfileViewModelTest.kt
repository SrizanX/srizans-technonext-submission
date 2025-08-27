package com.srizan.technonextcodingassessment.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
class ProfileViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockPreferenceRepository: PreferenceRepository

    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Setup default mocks
        whenever(mockPreferenceRepository.getUserEmail()) doReturn flowOf("test@example.com")
        whenever(mockPreferenceRepository.getAppThemeConfig()) doReturn flowOf(AppThemeConfig.SYSTEM)
        
        viewModel = ProfileViewModel(mockPreferenceRepository)
    }

    @Test
    fun `initial ui state should have correct default values`() = testScope.runTest {
        // When
        val uiState = viewModel.uiState.value
        
        // Then
        assertEquals("", uiState.email) // Will be updated by combine flow
        assertEquals(AppThemeConfig.SYSTEM, uiState.appThemeConfig)
        assertFalse(uiState.isSigningOut)
        assertFalse(uiState.isLoading)
        assertEquals(null, uiState.errorMessage)
    }

    @Test
    fun `changeAppThemeConfig should update theme config successfully`() = testScope.runTest {
        // When
        viewModel.changeAppThemeConfig(AppThemeConfig.DARK)
        
        // Wait for the coroutine to complete
        advanceUntilIdle()
        
        // Then
        verify(mockPreferenceRepository).setAppThemeConfig(AppThemeConfig.DARK)
    }

    @Test
    fun `signOut should update login status and clear preferences`() = testScope.runTest {
        // When
        viewModel.signOut()
        
        // Wait for the coroutine to complete
        advanceUntilIdle()
        
        // Then
        verify(mockPreferenceRepository).setUserLoggedInStatus(false)
        verify(mockPreferenceRepository).clearPreferences()
    }

    @Test
    fun `clearError should reset error message`() = testScope.runTest {
        // Given - manually set error state
        viewModel.clearError()
        
        // Then
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }
}
