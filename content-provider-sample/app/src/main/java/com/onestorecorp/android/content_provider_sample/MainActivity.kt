package com.onestorecorp.android.content_provider_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onestorecorp.android.content_provider_sample.ui.ConfigurationViewModel
import com.onestorecorp.android.content_provider_sample.ui.theme.ContentprovidersampleTheme
import com.onestorecorp.android.tracer.data.AppConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentprovidersampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConfigurationDisplayScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ConfigurationDisplayScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfigurationViewModel = viewModel()
) {
    val configuration by viewModel.configuration.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "CP Main",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "현재 설정 (읽기 전용)",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        
        // Server Configuration Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "서버 설정",
                    style = MaterialTheme.typography.titleMedium
                )
                
                ConfigItem(
                    label = "Base URL",
                    value = configuration.serverConfiguration.baseUrl.ifEmpty { "(설정 없음)" }
                )
                
                ConfigItem(
                    label = "Image Base URL",
                    value = configuration.serverConfiguration.imageBaseUrl.ifEmpty { "(설정 없음)" }
                )
            }
        }
        
        // Feature Configuration Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "기능 설정",
                    style = MaterialTheme.typography.titleMedium
                )
                
                ConfigItem(
                    label = "로그 활성화",
                    value = if (configuration.featureConfiguration.isLogEnabled) "ON" else "OFF"
                )
                
                ConfigItem(
                    label = "캡처 활성화",
                    value = if (configuration.featureConfiguration.isCaptureEnabled) "ON" else "OFF"
                )
            }
        }
    }
}

@Composable
fun ConfigItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}