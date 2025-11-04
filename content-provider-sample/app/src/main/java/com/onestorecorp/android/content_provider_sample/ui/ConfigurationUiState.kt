package com.onestorecorp.android.content_provider_sample.ui

import com.onestorecorp.android.tracer.data.AppConfiguration

data class ConfigurationUiState(
    val configuration: AppConfiguration = AppConfiguration(),
    val isLoading: Boolean = false
)

