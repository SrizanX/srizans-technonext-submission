package com.srizan.technonextcodingassessment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier,
    type: LoadingIndicatorType = LoadingIndicatorType.CIRCULAR
) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (type) {
            LoadingIndicatorType.LINEAR -> LinearProgressIndicator()
            LoadingIndicatorType.CIRCULAR -> CircularProgressIndicator()
        }
    }
}

enum class LoadingIndicatorType {
    LINEAR, CIRCULAR
}