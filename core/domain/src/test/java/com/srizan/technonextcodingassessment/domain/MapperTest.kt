package com.srizan.technonextcodingassessment.domain

import com.srizan.technonextcodingassessment.model.ApiResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for domain layer components
 */
class MapperTest {

    // Test data classes
    data class SourceData(val value: String, val number: Int)
    data class TargetData(val transformedValue: String, val doubledNumber: Int)

    // Test mapper implementation
    class TestMapper : Mapper<SourceData, TargetData> {
        override fun map(type: SourceData): TargetData {
            return TargetData(
                transformedValue = "transformed_${type.value}", doubledNumber = type.number * 2
            )
        }
    }

    @Test
    fun `mapper interface mapApiResponse should transform Success results correctly`() = runTest {
        // Given
        val mapper = TestMapper()
        val sourceData = SourceData("test", 5)
        val apiResultFlow = flow {
            emit(ApiResult.Success(sourceData))
        }

        // When
        val result = mapper.mapApiResponse(apiResultFlow).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ApiResult.Success)
        val successResult = result[0] as ApiResult.Success
        assertEquals("transformed_test", successResult.data.transformedValue)
        assertEquals(10, successResult.data.doubledNumber)
    }

    @Test
    fun `mapper interface mapApiResponse should preserve Error results`() = runTest {
        // Given
        val mapper = TestMapper()
        val apiResultFlow = flow {
            emit(ApiResult.Error("Network error", 500))
        }

        // When
        val result = mapper.mapApiResponse(apiResultFlow).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ApiResult.Error)
        val errorResult = result[0] as ApiResult.Error
        assertEquals("Network error", errorResult.errorMessage)
        assertEquals(500, errorResult.code)
    }

    @Test
    fun `mapper interface mapApiResponse should preserve Loading results`() = runTest {
        // Given
        val mapper = TestMapper()
        val apiResultFlow = flow {
            emit(ApiResult.Loading)
        }

        // When
        val result = mapper.mapApiResponse(apiResultFlow).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ApiResult.Loading)
    }

    @Test
    fun `standalone mapApiResponse function should transform Success results correctly`() =
        runTest {
            // Given
            val mapper = TestMapper()
            val sourceData = SourceData("standalone", 3)
            val apiResultFlow = flow {
                emit(ApiResult.Success(sourceData))
            }

            // When
            val result = mapApiResponse(apiResultFlow, mapper).toList()

            // Then
            assertEquals(1, result.size)
            assertTrue(result[0] is ApiResult.Success)
            val successResult = result[0] as ApiResult.Success
            assertEquals("transformed_standalone", successResult.data.transformedValue)
            assertEquals(6, successResult.data.doubledNumber)
        }

    @Test
    fun `standalone mapApiResponse function should preserve Error results`() = runTest {
        // Given
        val mapper = TestMapper()
        val apiResultFlow = flow {
            emit(ApiResult.Error("Timeout error", 408))
        }

        // When
        val result = mapApiResponse(apiResultFlow, mapper).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ApiResult.Error)
        val errorResult = result[0] as ApiResult.Error
        assertEquals("Timeout error", errorResult.errorMessage)
        assertEquals(408, errorResult.code)
    }

    @Test
    fun `standalone mapApiResponse function should preserve Loading results`() = runTest {
        // Given
        val mapper = TestMapper()
        val apiResultFlow = flow {
            emit(ApiResult.Loading)
        }

        // When
        val result = mapApiResponse(apiResultFlow, mapper).toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ApiResult.Loading)
    }

    @Test
    fun `mapApiResponse should handle multiple emissions correctly`() = runTest {
        // Given
        val mapper = TestMapper()
        val apiResultFlow = flow {
            emit(ApiResult.Loading)
            emit(ApiResult.Success(SourceData("first", 1)))
            emit(ApiResult.Error("Some error", 400))
            emit(ApiResult.Success(SourceData("second", 2)))
        }

        // When
        val result = mapper.mapApiResponse(apiResultFlow).toList()

        // Then
        assertEquals(4, result.size)

        // First emission - Loading
        assertTrue(result[0] is ApiResult.Loading)

        // Second emission - Success
        assertTrue(result[1] is ApiResult.Success)
        val firstSuccess = result[1] as ApiResult.Success
        assertEquals("transformed_first", firstSuccess.data.transformedValue)
        assertEquals(2, firstSuccess.data.doubledNumber)

        // Third emission - Error
        assertTrue(result[2] is ApiResult.Error)
        val error = result[2] as ApiResult.Error
        assertEquals("Some error", error.errorMessage)
        assertEquals(400, error.code)

        // Fourth emission - Success
        assertTrue(result[3] is ApiResult.Success)
        val secondSuccess = result[3] as ApiResult.Success
        assertEquals("transformed_second", secondSuccess.data.transformedValue)
        assertEquals(4, secondSuccess.data.doubledNumber)
    }
}