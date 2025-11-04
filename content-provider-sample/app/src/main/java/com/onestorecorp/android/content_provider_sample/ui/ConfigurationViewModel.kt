package com.onestorecorp.android.content_provider_sample.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onestorecorp.android.content_provider_sample.data.ConfigurationRepository
import com.onestorecorp.android.tracer.data.AppConfiguration
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ConfigurationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = ConfigurationRepository(application)
    
    val configuration: StateFlow<AppConfiguration> = repository.observeConfiguration()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppConfiguration()
        )
}