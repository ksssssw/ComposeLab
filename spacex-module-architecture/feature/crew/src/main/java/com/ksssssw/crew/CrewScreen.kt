package com.ksssssw.crew

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ksssssw.spacex.model.Crew
import org.koin.androidx.compose.koinViewModel

@Composable
fun CrewRoute(
    modifier: Modifier = Modifier,
    viewModel: CrewViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 상태 변화 로깅
    LaunchedEffect(uiState) {
        Log.d("CrewRoute", "UiState changed - ${uiState.debugInfo}")
        if (uiState.errorMessage != null) {
            Log.e("CrewRoute", "Error: ${uiState.errorMessage}")
        }
    }
    
    CrewScreen(
        uiState = uiState,
        onLoadMore = viewModel::loadMore,
        modifier = modifier
    )
}

@OptIn(FlowPreview::class)
@Composable
internal fun CrewScreen(
    uiState: CrewUiState,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    // 스크롤 위치 감지 with debouncing
    LaunchedEffect(listState, uiState.canLoadMore) {
        snapshotFlow { 
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItems - 3
        }
        .debounce(300) // 300ms 디바운싱
        .collect { isNearEnd ->
            Log.d("CrewScreen", "Scroll position check - nearEnd: $isNearEnd, canLoadMore: ${uiState.canLoadMore}, debugInfo: ${uiState.debugInfo}")
            
            if (isNearEnd && uiState.canLoadMore) {
                Log.d("CrewScreen", "Triggering loadMore")
                onLoadMore()
            }
        }
    }

    when {
        uiState.isLoading && uiState.crewMembers.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        uiState.errorMessage != null && uiState.crewMembers.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        
        else -> {
            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.crewMembers, key = { it.id }) { crew ->
                    CrewItem(crew = crew)
                }
                
                if (uiState.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CrewItem(
    crew: Crew,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = crew.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun CrewScreenPreview() {
    CrewScreen(
        uiState = CrewUiState(
            crewMembers = listOf(
                Crew(
                    id = "1",
                    name = "Robert Behnken",
                    agency = "NASA",
                    image = "",
                    wikipedia = "",
                    launches = emptyList(),
                    status = "active"
                ),
                Crew(
                    id = "2", 
                    name = "Douglas Hurley",
                    agency = "NASA",
                    image = "",
                    wikipedia = "",
                    launches = emptyList(),
                    status = "active"
                )
            )
        ),
        onLoadMore = { }
    )
}