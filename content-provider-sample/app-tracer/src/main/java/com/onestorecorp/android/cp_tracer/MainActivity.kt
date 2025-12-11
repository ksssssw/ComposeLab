package com.onestorecorp.android.cp_tracer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.onestorecorp.android.cp_tracer.data.TracerServerUrls
import com.onestorecorp.android.cp_tracer.ui.ConfigurationViewModel
import com.onestorecorp.android.cp_tracer.ui.FeatureType
import com.onestorecorp.android.cp_tracer.ui.theme.ContentprovidersampleTheme
import com.onestorecorp.android.tracer.data.ServerType
import com.onestorecorp.android.tracer.data.ServerUrls
import com.onestorecorp.android.tracer.data.UrlTarget
import org.koin.androidx.compose.koinViewModel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfigurationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val serverConfig = uiState.configuration.serverConfiguration
    
    var showServerSettingSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    
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
        
        Text(
            text = "설정 변경 시 자동 저장됩니다",
            style = MaterialTheme.typography.bodySmall,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "서버 설정",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = uiState.isServerConfigEnabled,
                        onCheckedChange = { viewModel.toggleServerConfiguration(it) }
                    )
                }
                
                if (uiState.isServerConfigEnabled && serverConfig != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showServerSettingSheet = true }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Base URL [${
                                    when (serverConfig.base.type) {
                                        ServerType.PROD -> "PROD"
                                        ServerType.QA -> "QA"
                                        ServerType.CUSTOM -> "CUSTOM"
                                    }
                                }]: ${serverConfig.base.url.ifEmpty { "(설정 없음)" }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Image URL [${
                                    when (serverConfig.image.type) {
                                        ServerType.PROD -> "PROD"
                                        ServerType.QA -> "QA"
                                        ServerType.CUSTOM -> "CUSTOM"
                                    }
                                }]: ${serverConfig.image.url.ifEmpty { "(설정 없음)" }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "서버 설정 변경",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (!uiState.isServerConfigEnabled) {
                    Text(
                        text = "서버 설정이 비활성화되었습니다. :app에서 기본값(PROD)이 사용됩니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
        
        if (showServerSettingSheet) {
            ModalBottomSheet(
                onDismissRequest = { 
                    showServerSettingSheet = false
                },
                sheetState = sheetState
            ) {
                ServerSettingBottomSheet(
                    serverConfig = serverConfig!!,
                    onUrlConfigChange = { target, type, url ->
                        viewModel.updateUrlConfig(target, type, url)
                    },
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                            showServerSettingSheet = false
                        }
                    }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "기능 설정",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = uiState.isFeatureConfigEnabled,
                        onCheckedChange = { viewModel.toggleFeatureConfiguration(it) }
                    )
                }
                
                if (uiState.isFeatureConfigEnabled) {
                    val featureConfig = uiState.configuration.featureConfiguration
                    if (featureConfig != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "로그 활성화")
                            Switch(
                                checked = featureConfig.isLogEnabled,
                                onCheckedChange = { 
                                    viewModel.updateFeatureConfig(FeatureType.LOG, it)
                                }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "캡처 활성화")
                            Switch(
                                checked = featureConfig.isCaptureEnabled,
                                onCheckedChange = { 
                                    viewModel.updateFeatureConfig(FeatureType.CAPTURE, it)
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "기능 설정이 비활성화되었습니다. :app에서 기본값(false)이 사용됩니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ServerSettingBottomSheet(
    serverConfig: com.onestorecorp.android.tracer.data.ServerConfiguration,
    onUrlConfigChange: (UrlTarget, ServerType, String) -> Unit,
    onDismiss: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var customBaseUrl by remember(serverConfig.base.url) {
        mutableStateOf(
            if (serverConfig.base.type == ServerType.CUSTOM) serverConfig.base.url else ""
        )
    }
    var customImageBaseUrl by remember(serverConfig.image.url) {
        mutableStateOf(
            if (serverConfig.image.type == ServerType.CUSTOM) serverConfig.image.url else ""
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "서버 설정",
            style = MaterialTheme.typography.headlineSmall
        )
        
        // Base URL Type Selection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Base URL 타입",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            ServerType.entries.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = serverConfig.base.type == type,
                            onClick = { onUrlConfigChange(UrlTarget.BASE, type, customBaseUrl) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = serverConfig.base.type == type,
                        onClick = null
                    )
                    Column {
                        Text(
                            text = when (type) {
                                ServerType.PROD -> "Production"
                                ServerType.QA -> "QA"
                                ServerType.CUSTOM -> "Custom"
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (type != ServerType.CUSTOM) {
                            Text(
                                text = when (type) {
                                    ServerType.PROD -> ServerUrls.BASE_PROD
                                    ServerType.QA -> TracerServerUrls.BASE_QA
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Custom Base URL Field
        if (serverConfig.base.type == ServerType.CUSTOM) {
            OutlinedTextField(
                value = customBaseUrl,
                onValueChange = { customBaseUrl = it },
                label = { Text("Custom Base URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onUrlConfigChange(UrlTarget.BASE, ServerType.CUSTOM, customBaseUrl)
                    }
                )
            )
        }
        
        // Image Base URL Type Selection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Image Base URL 타입",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            ServerType.entries.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = serverConfig.image.type == type,
                            onClick = { onUrlConfigChange(UrlTarget.IMAGE, type, customImageBaseUrl) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = serverConfig.image.type == type,
                        onClick = null
                    )
                    Column {
                        Text(
                            text = when (type) {
                                ServerType.PROD -> "Production"
                                ServerType.QA -> "QA"
                                ServerType.CUSTOM -> "Custom"
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (type != ServerType.CUSTOM) {
                            Text(
                                text = when (type) {
                                    ServerType.PROD -> ServerUrls.IMAGE_PROD
                                    ServerType.QA -> TracerServerUrls.IMAGE_QA
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Custom Image Base URL Field
        if (serverConfig.image.type == ServerType.CUSTOM) {
            OutlinedTextField(
                value = customImageBaseUrl,
                onValueChange = { customImageBaseUrl = it },
                label = { Text("Custom Image Base URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onUrlConfigChange(UrlTarget.IMAGE, ServerType.CUSTOM, customImageBaseUrl)
                    }
                )
            )
        }
        
        // Current Applied URLs
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "현재 적용된 URL",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Base URL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = serverConfig.base.url.ifEmpty { "(설정 없음)" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Image Base URL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = serverConfig.image.url.ifEmpty { "(설정 없음)" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("완료")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}