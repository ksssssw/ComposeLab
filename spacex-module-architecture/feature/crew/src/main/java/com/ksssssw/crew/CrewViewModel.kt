package com.ksssssw.crew

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksssssw.spacex.data.CrewRepository
import com.ksssssw.spacex.model.Crew
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrewViewModel(
    private val crewRepository: CrewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CrewUiState(isLoading = true))
    val uiState: StateFlow<CrewUiState> = _uiState.asStateFlow()

    init {
        loadCrew()
    }

    fun loadCrew() {
        Log.d("CrewViewModel", "loadCrew() called")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                Log.d("CrewViewModel", "Calling crewRepository.getCrew(page=1, limit=10)")
                val crewPage = crewRepository.getCrew(page = 1, limit = 10)
                Log.d("CrewViewModel", "Received crewPage: ${crewPage.crewMembers.size} members, page=${crewPage.page}, hasNext=${crewPage.hasNextPage}")
                
                _uiState.value = _uiState.value.copy(
                    crewMembers = crewPage.crewMembers,
                    currentPage = crewPage.page,
                    totalPages = crewPage.totalPages,
                    hasNextPage = crewPage.hasNextPage,
                    isLoading = false
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                )
            }
        }
    }

    fun loadMore() {
        Log.d("CrewViewModel", "loadMore() called - canLoadMore: ${_uiState.value.canLoadMore}")
        if (!_uiState.value.canLoadMore) return

        val nextPage = _uiState.value.currentPage + 1
        Log.d("CrewViewModel", "Loading page $nextPage")
        
        // 중복 요청 방지: 이미 해당 페이지를 로딩 중이라면 리턴
        if (_uiState.value.isLoadingMore) {
            Log.d("CrewViewModel", "Already loading more, skipping request")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)

            try {
                val crewPage = crewRepository.getCrew(
                    page = nextPage,
                    limit = 10
                )
                Log.d("CrewViewModel", "LoadMore received ${crewPage.crewMembers.size} more members")
                
                _uiState.value = _uiState.value.copy(
                    crewMembers = _uiState.value.crewMembers + crewPage.crewMembers,
                    currentPage = crewPage.page,
                    totalPages = crewPage.totalPages,
                    hasNextPage = crewPage.hasNextPage,
                    isLoadingMore = false
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    errorMessage = exception.message
                )
            }
        }
    }

}

data class CrewUiState(
    val crewMembers: List<Crew> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val totalPages: Int = 0
) {
    val isEmpty: Boolean
        get() = crewMembers.isEmpty() && !isLoading && errorMessage == null

    val canLoadMore: Boolean
        get() = hasNextPage && !isLoading && !isLoadingMore
    
    // 디버깅 정보
    val debugInfo: String
        get() = "Page: $currentPage/$totalPages, HasNext: $hasNextPage, Loading: $isLoading, LoadingMore: $isLoadingMore, Items: ${crewMembers.size}"
}