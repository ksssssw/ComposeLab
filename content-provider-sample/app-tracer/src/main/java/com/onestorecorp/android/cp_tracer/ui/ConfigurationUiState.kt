package com.onestorecorp.android.cp_tracer.ui

import com.onestorecorp.android.tracer.data.AppConfiguration

data class ConfigurationUiState(
    val configuration: AppConfiguration = AppConfiguration(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false
)

