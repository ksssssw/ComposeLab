package com.onestorecorp.android.cp_tracer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onestorecorp.android.tracer.data.AppConfiguration
import com.onestorecorp.android.tracer.data.FeatureConfiguration
import com.onestorecorp.android.tracer.data.ServerConfiguration
import com.onestorecorp.android.tracer.provider.ConfigurationProvider
import com.onestorecorp.android.tracer.storage.ConfigurationStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfigurationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val storage = ConfigurationStorage(application)
    
    private val _configuration = MutableStateFlow(AppConfiguration())
    val configuration: StateFlow<AppConfiguration> = _configuration.asStateFlow()
    
    init {
        loadConfiguration()
    }
    
    private fun loadConfiguration() {
        viewModelScope.launch {
            _configuration.value = storage.loadConfiguration()
        }
    }
    
    fun updateServerConfiguration(baseUrl: String, imageBaseUrl: String) {
        val updatedConfig = _configuration.value.copy(
            serverConfiguration = ServerConfiguration(
                baseUrl = baseUrl,
                imageBaseUrl = imageBaseUrl
            )
        )
        _configuration.value = updatedConfig
    }
    
    fun updateFeatureConfiguration(isLogEnabled: Boolean, isCaptureEnabled: Boolean) {
        val updatedConfig = _configuration.value.copy(
            featureConfiguration = FeatureConfiguration(
                isLogEnabled = isLogEnabled,
                isCaptureEnabled = isCaptureEnabled
            )
        )
        _configuration.value = updatedConfig
    }
    
    fun saveConfiguration() {
        viewModelScope.launch {
            storage.saveConfiguration(_configuration.value)
            // Notify Content Provider observers
            getApplication<Application>().contentResolver.notifyChange(
                ConfigurationProvider.CONTENT_URI,
                null
            )
        }
    }
}

