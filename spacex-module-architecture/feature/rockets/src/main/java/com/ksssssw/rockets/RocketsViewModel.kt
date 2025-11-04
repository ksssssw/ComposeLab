package com.ksssssw.rockets

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.spacex.data.RocketRepository
import com.ksssssw.spacex.model.Rocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RocketsUiState(
    val rockets: List<Rocket> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean
        get() = rockets.isEmpty() && !isLoading && errorMessage == null
}

class RocketsViewModel(
    private val rocketRepository: RocketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RocketsUiState(isLoading = true))
    val uiState: StateFlow<RocketsUiState> = _uiState.asStateFlow()

    init {
        loadRockets()
    }

    private fun loadRockets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                rocketRepository.getRockets()
                    .collect { rockets ->
                        _uiState.update {
                            it.copy(
                                rockets = rockets,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}