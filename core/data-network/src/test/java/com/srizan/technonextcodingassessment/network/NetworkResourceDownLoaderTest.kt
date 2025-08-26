package com.srizan.technonextcodingassessment.network

import com.google.gson.Gson
import com.srizan.technonextcodingassessment.model.ApiResult
import com.srizan.technonextcodingassessment.network.model.ApiError
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkResourceDownLoaderTest {

    private lateinit var networkResourceDownLoader: NetworkResourceDownLoader
    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = Gson()
        networkResourceDownLoader = NetworkResourceDownLoader(gson)
    }

    @Test
    fun `safeApiCall should emit Loading then Success for successful response`() = runTest {
        // Given
        val expectedData = "test data"
        val response = Response.success(expectedData)

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Success)
        assertEquals(expectedData, (result[1] as ApiResult.Success).data)
    }

    @Test
    fun `safeApiCall should emit Loading then Error for null response body`() = runTest {
        // Given
        val response = Response.success<String>(null)

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Error)
        assertEquals("Response body is null", (result[1] as ApiResult.Error).errorMessage)
    }

    @Test
    fun `safeApiCall should handle 400 Bad Request properly`() = runTest {
        // Given
        val errorBody = """{"message": "Invalid input parameters"}"""
        val response = Response.error<String>(
            400,
            errorBody.toResponseBody("application/json".toMediaType())
        )

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Invalid input parameters", error.errorMessage)
        assertEquals(400, error.code)
    }

    @Test
    fun `safeApiCall should handle 401 Unauthorized with user-friendly message`() = runTest {
        // Given
        val response = Response.error<String>(
            401,
            "".toResponseBody("application/json".toMediaType())
        )

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Authentication failed. Please sign in again.", error.errorMessage)
        assertEquals(401, error.code)
    }

    @Test
    fun `safeApiCall should handle 500 Internal Server Error`() = runTest {
        // Given
        val response = Response.error<String>(
            500,
            "Internal Server Error".toResponseBody("text/plain".toMediaType())
        )

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Server error. Please try again later.", error.errorMessage)
        assertEquals(500, error.code)
    }

    @Test
    fun `safeApiCall should handle IOException with network error message`() = runTest {
        // Given
        val exception = IOException("Network connection failed")

        // When
        val result = networkResourceDownLoader.safeApiCall<String> { 
            throw exception
        }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Network error. Please check your connection and try again.", error.errorMessage)
        assertEquals(0, error.code)
    }

    @Test
    fun `safeApiCall should handle UnknownHostException with no internet message`() = runTest {
        // Given
        val exception = UnknownHostException("Unable to resolve host")

        // When
        val result = networkResourceDownLoader.safeApiCall<String> { 
            throw exception
        }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("No internet connection. Please check your network settings.", error.errorMessage)
    }

    @Test
    fun `safeApiCall should handle SocketTimeoutException with timeout message`() = runTest {
        // Given
        val exception = SocketTimeoutException("Read timed out")

        // When
        val result = networkResourceDownLoader.safeApiCall<String> { 
            throw exception
        }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Connection timed out. Please try again.", error.errorMessage)
    }

    @Test
    fun `safeApiCall should handle ConnectException with connection failed message`() = runTest {
        // Given
        val exception = ConnectException("Connection refused")

        // When
        val result = networkResourceDownLoader.safeApiCall<String> { 
            throw exception
        }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Connection failed. Please check your internet connection.", error.errorMessage)
    }

    @Test
    fun `safeApiCall should parse ApiError from response body when available`() = runTest {
        // Given
        val apiError = ApiError("Custom error message", "ERROR_001")
        val errorBody = gson.toJson(apiError)
        val response = Response.error<String>(
            422,
            errorBody.toResponseBody("application/json".toMediaType())
        )

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Custom error message", error.errorMessage)
        assertEquals(422, error.code)
    }

    @Test
    fun `safeApiCall should handle malformed JSON in error response`() = runTest {
        // Given
        val malformedJson = """{"message": "incomplete json"""
        val response = Response.error<String>(
            400,
            malformedJson.toResponseBody("application/json".toMediaType())
        )

        // When
        val result = networkResourceDownLoader.safeApiCall { response }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Bad request. Please check your input.", error.errorMessage)
        assertEquals(400, error.code)
    }

    @Test
    fun `safeApiCall should handle timeout with custom timeout message`() = runTest {
        // Given
        val timeoutMillis = 100L

        // When
        val result = networkResourceDownLoader.safeApiCall<String>(timeoutMillis) { 
            kotlinx.coroutines.delay(200L) // Longer than timeout
            Response.success("data")
        }.toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ApiResult.Loading)
        assertTrue(result[1] is ApiResult.Error)
        val error = result[1] as ApiResult.Error
        assertEquals("Request timed out. Please check your connection and try again.", error.errorMessage)
        assertEquals(408, error.code)
    }
}
