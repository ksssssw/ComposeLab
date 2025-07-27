package com.ksssssw.rockets

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.spacex.data.RocketRepository
import kotlinx.coroutines.launch

class RocketsViewModel(
    private val rocketRepository: RocketRepository
) : ViewModel() {

    init {
        loadRockets()
    }

    private fun loadRockets() {
        viewModelScope.launch {
            try {
                rocketRepository.getRockets()
                    .collect { rockets ->
                        Log.d("RocketsViewModel", "Rockets: $rockets")
                    }
            } catch (e: Exception) {
                Log.d("RocketsViewModel", "Error: $e")
            }
        }
    }
}