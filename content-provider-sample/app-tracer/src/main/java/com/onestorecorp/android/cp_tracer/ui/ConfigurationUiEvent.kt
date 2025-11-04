package com.onestorecorp.android.cp_tracer.ui

/**
 * UI 이벤트 - View에서 ViewModel로 전달되는 사용자 액션
 */
sealed interface ConfigurationUiEvent {
    data object LoadConfiguration : ConfigurationUiEvent
    data class UpdateServerUrl(val baseUrl: String, val imageBaseUrl: String) : ConfigurationUiEvent
    data class UpdateLogEnabled(val enabled: Boolean) : ConfigurationUiEvent
    data class UpdateCaptureEnabled(val enabled: Boolean) : ConfigurationUiEvent
}

