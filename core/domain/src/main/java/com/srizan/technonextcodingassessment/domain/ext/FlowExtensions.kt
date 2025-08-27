package com.srizan.technonextcodingassessment.domain.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Extension functions for better Flow handling and error management.
 * These utilities promote cleaner, more readable code throughout the application.
 */

/**
 * Maps a Flow to provide a default value when the original flow emits null.
 * Useful for handling nullable DataStore values.
 * 
 * @param defaultValue The value to emit when the original flow emits null
 * @return Flow<T> A flow that never emits null
 */
fun <T : Any> Flow<T?>.orDefault(defaultValue: T): Flow<T> {
    return this.map { it ?: defaultValue }
}

/**
 * Handles errors in a Flow by emitting a fallback value.
 * Provides graceful error recovery for UI-facing flows.
 * 
 * @param fallbackValue The value to emit when an error occurs
 * @return Flow<T> A flow that handles errors gracefully
 */
fun <T> Flow<T>.withFallback(fallbackValue: T): Flow<T> {
    return this.catch { emit(fallbackValue) }
}

/**
 * Combines null safety and error handling for robust data flows.
 * Perfect for DataStore flows that might emit null or encounter errors.
 * 
 * @param defaultValue The value to use for null emissions and errors
 * @return Flow<T> A safe, never-null flow
 */
fun <T : Any> Flow<T?>.safeWithDefault(defaultValue: T): Flow<T> {
    return this.orDefault(defaultValue).withFallback(defaultValue)
}
