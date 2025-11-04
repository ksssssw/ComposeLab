package com.onestorecorp.android.cp_tracer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onestorecorp.android.cp_tracer.ui.ConfigurationViewModel
import com.onestorecorp.android.cp_tracer.ui.theme.ContentprovidersampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentprovidersampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConfigurationScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfigurationViewModel = viewModel()
) {
    val configuration by viewModel.configuration.collectAsState()
    
    var baseUrl by remember(configuration.serverConfiguration.baseUrl) {
        mutableStateOf(configuration.serverConfiguration.baseUrl)
    }
    var imageBaseUrl by remember(configuration.serverConfiguration.imageBaseUrl) {
        mutableStateOf(configuration.serverConfiguration.imageBaseUrl)
    }
    var isLogEnabled by remember(configuration.featureConfiguration.isLogEnabled) {
        mutableStateOf(configuration.featureConfiguration.isLogEnabled)
    }
    var isCaptureEnabled by remember(configuration.featureConfiguration.isCaptureEnabled) {
        mutableStateOf(configuration.featureConfiguration.isCaptureEnabled)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "CP Tracer",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
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
                
                OutlinedTextField(
                    value = baseUrl,
                    onValueChange = { baseUrl = it },
                    label = { Text("Base URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = imageBaseUrl,
                    onValueChange = { imageBaseUrl = it },
                    label = { Text("Image Base URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "로그 활성화")
                    Switch(
                        checked = isLogEnabled,
                        onCheckedChange = { isLogEnabled = it }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "캡처 활성화")
                    Switch(
                        checked = isCaptureEnabled,
                        onCheckedChange = { isCaptureEnabled = it }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Save Button
        Button(
            onClick = {
                viewModel.updateServerConfiguration(baseUrl, imageBaseUrl)
                viewModel.updateFeatureConfiguration(isLogEnabled, isCaptureEnabled)
                viewModel.saveConfiguration()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("설정 저장")
        }
    }
}