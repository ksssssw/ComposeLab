package com.ksssssw.wepray.ui.scene.deeplinker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.ui.components.*
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable

@Serializable 
internal data object DeepLinker: NavKey

internal fun EntryProviderScope<NavKey>.deepLinkerScene() {
    entry<DeepLinker> {
        DeepLinkerScene()
    }
}

@Composable
fun DeepLinkerScene() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = WePrayTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
    ) {
        // Deep Link Input Section
        WePrayCard {
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Deep Link URL",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.textPrimary
                )
                
                var deepLinkUrl by remember { mutableStateOf("") }
                WePrayLabeledTextField(
                    value = deepLinkUrl,
                    onValueChange = { deepLinkUrl = it },
                    label = "URL",
                    placeholder = "myapp://screen/detail?id=123",
                    leadingIcon = androidx.compose.material.icons.Icons.Outlined.Link
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                ) {
                    WePrayPrimaryButton(
                        text = "Test Link",
                        onClick = { /* TODO: Test deep link */ },
                        modifier = Modifier.weight(1f),
                        enabled = deepLinkUrl.isNotEmpty()
                    )
                    WePraySecondaryButton(
                        text = "Clear",
                        onClick = { deepLinkUrl = "" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        
        // Quick Links Section
        WePrayCard {
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Text(
                    text = "Quick Links",
                    style = WePrayTheme.typography.headlineMedium,
                    color = WePrayTheme.colors.textPrimary
                )
                
                val categories = listOf(
                    "Home Screen", 
                    "Detail Screen", 
                    "Settings", 
                    "Profile",
                    "Notifications"
                )
                
                var selectedCategory by remember { mutableStateOf<String?>(null) }
                WePrayDropdown(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    items = categories,
                    placeholder = "Select a screen"
                )
                
                if (selectedCategory != null) {
                    WePrayPrimaryButton(
                        text = "Open $selectedCategory",
                        onClick = { /* TODO: Open screen */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        // Test Results Section
        WePrayCard {
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Tests",
                        style = WePrayTheme.typography.headlineMedium,
                        color = WePrayTheme.colors.textPrimary
                    )
                    WePrayPillBadge(
                        text = "0 Tests",
                        variant = BadgeVariant.Neutral
                    )
                }
                
                Text(
                    text = "No tests performed yet",
                    style = WePrayTheme.typography.bodyMedium,
                    color = WePrayTheme.colors.textSecondary
                )
            }
        }
    }
}
