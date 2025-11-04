package com.onestorecorp.android.content_provider_sample.ui

import androidx.lifecycle.ViewModel
import com.onestorecorp.android.tracer.data.AppConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigurationViewModel(
    configuration: AppConfiguration
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        ConfigurationUiState(configuration = configuration)
    )
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()
}