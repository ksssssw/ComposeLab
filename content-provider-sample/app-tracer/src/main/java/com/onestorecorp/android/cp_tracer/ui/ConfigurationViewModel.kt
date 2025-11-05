package com.onestorecorp.android.cp_tracer.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onestorecorp.android.tracer.data.ServerConfiguration
import com.onestorecorp.android.tracer.provider.ConfigurationProvider
import com.onestorecorp.android.tracer.storage.ConfigurationStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigurationViewModel(
    private val storage: ConfigurationStorage
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()
    
    init {
        loadConfiguration()
    }
    
    private fun loadConfiguration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val config = storage.loadConfiguration()
            _uiState.update { 
                it.copy(
                    configuration = config,
                    isLoading = false
                )
            }
        }
    }
    
    fun updateServerUrl(context: Context, baseUrl: String, imageBaseUrl: String) {
        val updatedConfig = _uiState.value.configuration.copy(
            serverConfiguration = ServerConfiguration(
                baseUrl = baseUrl,
                imageBaseUrl = imageBaseUrl
            )
        )
        _uiState.update { it.copy(configuration = updatedConfig) }
        saveConfiguration(context)
    }
    
    fun updateLogEnabled(context: Context, enabled: Boolean) {
        val updatedConfig = _uiState.value.configuration.copy(
            featureConfiguration = _uiState.value.configuration.featureConfiguration.copy(
                isLogEnabled = enabled
            )
        )
        _uiState.update { it.copy(configuration = updatedConfig) }
        saveConfiguration(context)
    }
    
    fun updateCaptureEnabled(context: Context, enabled: Boolean) {
        val updatedConfig = _uiState.value.configuration.copy(
            featureConfiguration = _uiState.value.configuration.featureConfiguration.copy(
                isCaptureEnabled = enabled
            )
        )
        _uiState.update { it.copy(configuration = updatedConfig) }
        saveConfiguration(context)
    }
    
    private fun saveConfiguration(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            storage.saveConfiguration(_uiState.value.configuration)
            // Notify Content Provider observers
            context.contentResolver.notifyChange(
                ConfigurationProvider.CONTENT_URI,
                null
            )
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}

