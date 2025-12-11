package com.onestorecorp.android.cp_tracer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onestorecorp.android.cp_tracer.data.TracerServerUrls
import com.onestorecorp.android.tracer.data.FeatureConfiguration
import com.onestorecorp.android.tracer.data.ServerConfiguration
import com.onestorecorp.android.tracer.data.ServerType
import com.onestorecorp.android.tracer.data.ServerUrls
import com.onestorecorp.android.tracer.data.UrlConfiguration
import com.onestorecorp.android.tracer.data.UrlTarget
import com.onestorecorp.android.tracer.storage.ConfigurationStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FeatureType {
    LOG, CAPTURE
}

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
                    isServerConfigEnabled = config.serverConfiguration != null,
                    isFeatureConfigEnabled = config.featureConfiguration != null,
                    isLoading = false
                )
            }
        }
    }
    
    fun toggleServerConfiguration(enabled: Boolean) {
        val updatedConfig = if (enabled) {
            _uiState.value.configuration.copy(
                serverConfiguration = ServerConfiguration()
            )
        } else {
            _uiState.value.configuration.copy(
                serverConfiguration = null
            )
        }
        _uiState.update { 
            it.copy(
                configuration = updatedConfig,
                isServerConfigEnabled = enabled
            )
        }
        saveConfiguration()
    }
    
    fun toggleFeatureConfiguration(enabled: Boolean) {
        val updatedConfig = if (enabled) {
            _uiState.value.configuration.copy(
                featureConfiguration = FeatureConfiguration()
            )
        } else {
            _uiState.value.configuration.copy(
                featureConfiguration = null
            )
        }
        _uiState.update { 
            it.copy(
                configuration = updatedConfig,
                isFeatureConfigEnabled = enabled
            )
        }
        saveConfiguration()
    }
    
    fun updateUrlConfig(target: UrlTarget, type: ServerType, url: String = "") {
        val currentServerConfig = _uiState.value.configuration.serverConfiguration ?: return
        
        val newUrl = when (type) {
            ServerType.PROD -> when (target) {
                UrlTarget.BASE -> ServerUrls.BASE_PROD
                UrlTarget.IMAGE -> ServerUrls.IMAGE_PROD
            }
            ServerType.QA -> when (target) {
                UrlTarget.BASE -> TracerServerUrls.BASE_QA
                UrlTarget.IMAGE -> TracerServerUrls.IMAGE_QA
            }
            ServerType.CUSTOM -> url
        }
        
        val updatedServerConfig = when (target) {
            UrlTarget.BASE -> currentServerConfig.copy(
                base = UrlConfiguration(type = type, url = newUrl)
            )
            UrlTarget.IMAGE -> currentServerConfig.copy(
                image = UrlConfiguration(type = type, url = newUrl)
            )
        }
        
        val updatedConfig = _uiState.value.configuration.copy(
            serverConfiguration = updatedServerConfig
        )
        _uiState.update { it.copy(configuration = updatedConfig) }
        saveConfiguration()
    }
    
    fun updateFeatureConfig(feature: FeatureType, enabled: Boolean) {
        val currentFeatureConfig = _uiState.value.configuration.featureConfiguration ?: return
        
        val updatedFeatureConfig = when (feature) {
            FeatureType.LOG -> currentFeatureConfig.copy(isLogEnabled = enabled)
            FeatureType.CAPTURE -> currentFeatureConfig.copy(isCaptureEnabled = enabled)
        }
        
        val updatedConfig = _uiState.value.configuration.copy(
            featureConfiguration = updatedFeatureConfig
        )
        _uiState.update { it.copy(configuration = updatedConfig) }
        saveConfiguration()
    }
    
    private fun saveConfiguration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            storage.saveConfiguration(_uiState.value.configuration)
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}

