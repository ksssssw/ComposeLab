package com.ksssssw.wepray.ui.scene.deeplinker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ksssssw.wepray.domain.model.DeepLinkCategory
import com.ksssssw.wepray.domain.model.DeepLinkHistoryItem
import com.ksssssw.wepray.ui.components.*
import com.ksssssw.wepray.ui.components.getContrastColor
import com.ksssssw.wepray.ui.theme.WePrayTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable 
internal data object DeepLinker: NavKey

internal fun EntryProviderScope<NavKey>.deepLinkerScene() {
    entry<DeepLinker> {
        DeepLinkerScene()
    }
}

// Data classes (to be connected with real data later)
data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Android,
    val isFavorite: Boolean = false,
    val isRecent: Boolean = false,
    val isSystem: Boolean = false
)

data class CategoryInfo(
    val id: String,
    val name: String,
    val color: Color
)

data class DeepLinkHistory(
    val id: String,
    val url: String,
    val appInfo: AppInfo,
    val category: CategoryInfo?,
    val timestamp: String
)

@Composable
fun DeepLinkerScene(
    viewModel: DeepLinkerViewModel = koinViewModel()
) {
    // Local UI State
    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }
    var deepLinkUrl by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<DeepLinkCategory?>(null) }
    var excludeSystemApps by remember { mutableStateOf(true) }
    
    // Dialog states
    var showAppSelector by remember { mutableStateOf(false) }
    var showCategoryManager by remember { mutableStateOf(false) }
    var historyToEdit by remember { mutableStateOf<DeepLinkHistoryItem?>(null) }
    
    // ViewModel state
    val uiState by viewModel.uiState.collectAsState()
    
    // Show loading state
    if (uiState is DeepLinkerUiState.Loading) {
        // TODO: Show loading indicator
        return
    }
    
    // Extract success state
    val state = (uiState as? DeepLinkerUiState.Success) ?: return
    
    // Load installed apps when device changes
    LaunchedEffect(state.selectedDevice) {
        if (state.selectedDevice != null && state.apps.isEmpty()) {
            viewModel.loadInstalledApps()
        }
    }
    
    // Convert InstalledAppInfo to UI AppInfo model
    val apps = remember(state.apps) {
        state.apps.map { installedApp ->
            AppInfo(
                name = installedApp.appName,
                packageName = installedApp.packageName,
                icon = Icons.Outlined.Android,
                isFavorite = installedApp.isFavorite,
                isRecent = installedApp.isRecent,
                isSystem = installedApp.packageName.startsWith("com.android") || 
                          installedApp.packageName.startsWith("com.google.android")
            )
        }
    }
    
    // Show error/success messages
    state.errorMessage?.let { error ->
        // TODO: Show snackbar or toast
        LaunchedEffect(error) {
            println("Error: $error")
            viewModel.clearError()
        }
    }
    
    state.successMessage?.let { success ->
        // TODO: Show snackbar or toast
        LaunchedEffect(success) {
            println("Success: $success")
            viewModel.clearSuccess()
        }
    }
    
    // Show dialogs
    if (showAppSelector) {
        AppSelectorDialog(
            apps = apps,
            selectedApp = selectedApp,
            excludeSystemApps = excludeSystemApps,
            onExcludeSystemAppsChange = { excludeSystemApps = it },
            onAppSelected = { 
                selectedApp = it
                showAppSelector = false
            },
            onDismiss = { showAppSelector = false },
            onFavoriteToggle = { app ->
                viewModel.toggleFavorite(app.packageName, app.name)
            }
        )
    }
    
    if (showCategoryManager) {
        CategoryManagerDialog(
            categories = state.categories,
            onCategoriesUpdated = { updatedCategories ->
                // Categories are automatically updated via Flow
            },
            onCategoryAdd = { name, colorValue ->
                viewModel.addCategory(name, colorValue)
            },
            onCategoryDelete = { id ->
                viewModel.deleteCategory(id)
            },
            onDismiss = { showCategoryManager = false }
        )
    }
    
    historyToEdit?.let { history ->
        EditHistoryDialog(
            history = history,
            apps = apps,
            categories = state.categories,
            onSave = { updatedHistory ->
                viewModel.updateHistory(updatedHistory)
                historyToEdit = null
            },
            onDismiss = { historyToEdit = null }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = WePrayTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
    ) {
        // Top area - Input card
        WePrayCard {
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
            ) {
                // 2x2 Grid layout with uniform spacing (5:5 ratio)
                Column(
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                ) {
                    // First row: Target App | Category
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                    ) {
                        // Target app selection (50%)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                        ) {
                            Text(
                                text = "TARGET APP",
                                style = WePrayTheme.typography.overline,
                                color = WePrayTheme.colors.textSecondary
                            )
                            
                            // App selector button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(WePrayTheme.shapes.input)
                                    .background(WePrayTheme.colors.surface)
                                    .border(
                                        width = 1.dp,
                                        color = WePrayTheme.colors.border,
                                        shape = WePrayTheme.shapes.input
                                    )
                                    .clickable { showAppSelector = true }
                                    .padding(WePrayTheme.spacing.inputPadding)
                            ) {
                                if (selectedApp == null) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Search,
                                            contentDescription = null,
                                            modifier = Modifier.size(WePrayTheme.iconSize.default),
                                            tint = WePrayTheme.colors.textSecondary
                                        )
                                        Text(
                                            text = "Select target app",
                                            style = WePrayTheme.typography.bodyMedium,
                                            color = WePrayTheme.colors.textSecondary
                                        )
                                    }
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = selectedApp?.icon ?: Icons.Outlined.Error,
                                            contentDescription = null,
                                            modifier = Modifier.size(WePrayTheme.iconSize.default),
                                            tint = WePrayTheme.colors.primary
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = selectedApp?.name ?: "Unknown",
                                                style = WePrayTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                color = WePrayTheme.colors.textPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = selectedApp?.packageName ?: "Unknown",
                                                style = WePrayTheme.typography.bodySmall,
                                                color = WePrayTheme.colors.textTertiary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Category selection (50%)
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                            ) {
                                Text(
                                    text = "CATEGORY",
                                    style = WePrayTheme.typography.overline,
                                    color = WePrayTheme.colors.textSecondary
                                )
                                WePrayDropdown(
                                    value = selectedCategory,
                                    onValueChange = { selectedCategory = it },
                                    items = state.categories,
                                    placeholder = "Optional",
                                    itemLabel = { it.name }
                                )
                            }
                            
                            Column(
                                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                            ) {
                                Spacer(modifier = Modifier.height(20.dp)) // Label space
                                WePrayIconButton(
                                    icon = Icons.Outlined.Settings,
                                    contentDescription = "Manage Categories",
                                    onClick = { showCategoryManager = true },
                                    size = IconButtonSize.Default
                                )
                            }
                        }
                    }
                    
                    // Second row: URL | Send Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Deep link URL input (50%)
                        WePrayLabeledTextField(
                            value = deepLinkUrl,
                            onValueChange = { deepLinkUrl = it },
                            label = "DEEP LINK URL",
                            placeholder = "myapp://screen/detail?id=123",
                            leadingIcon = Icons.Outlined.Link,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Send button
                        WePrayPrimaryButton(
                            text = "Send Deep Link",
                            onClick = {
                                selectedApp?.let { app ->
                                    viewModel.sendDeepLink(
                                        url = deepLinkUrl,
                                        packageName = app.packageName,
                                        appName = app.name,
                                        categoryId = selectedCategory?.id
                                    )
                                }
                            },
                            enabled = selectedApp != null && deepLinkUrl.isNotEmpty() && state.selectedDevice != null,
                            icon = Icons.Outlined.Send
                        )
                    }
                }
            }
        }
        
        // History area
        WePrayCard(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "History",
                        style = WePrayTheme.typography.headlineMedium,
                        color = WePrayTheme.colors.textPrimary
                    )
                    WePrayPillBadge(
                        text = "${state.history.size} items",
                        variant = BadgeVariant.Neutral
                    )
                }
                
                if (state.history.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(vertical = WePrayTheme.spacing.xxxl),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.History,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = WePrayTheme.colors.textTertiary
                            )
                            Text(
                                text = "No history yet",
                                style = WePrayTheme.typography.bodyLarge,
                                color = WePrayTheme.colors.textSecondary
                            )
                            Text(
                                text = "Sent deep links will appear here",
                                style = WePrayTheme.typography.bodySmall,
                                color = WePrayTheme.colors.textTertiary,
                            )
                        }
                    }
                } else {
                    // History list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
                    ) {
                        items(state.history) { historyItem ->
                            val historyApp = AppInfo(
                                name = historyItem.appName,
                                packageName = historyItem.packageName,
                                icon = Icons.Outlined.Android
                            )
                            val historyCategory = state.categories.firstOrNull { it.id == historyItem.categoryId }
                            
                            DeepLinkHistoryItemView(
                                historyItem = historyItem,
                                category = historyCategory,
                                onClick = {
                                    selectedApp = historyApp
                                    deepLinkUrl = historyItem.url
                                    selectedCategory = historyCategory
                                },
                                onEdit = { historyToEdit = historyItem },
                                onDelete = { viewModel.deleteHistory(historyItem.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// App Selector Dialog
@Composable
private fun AppSelectorDialog(
    apps: List<AppInfo>,
    selectedApp: AppInfo?,
    excludeSystemApps: Boolean,
    onExcludeSystemAppsChange: (Boolean) -> Unit,
    onAppSelected: (AppInfo) -> Unit,
    onDismiss: () -> Unit,
    onFavoriteToggle: (AppInfo) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Filtered app list
    val filteredApps = remember(apps, excludeSystemApps, searchQuery) {
        apps
            .filter { !excludeSystemApps || !it.isSystem }
            .filter { 
                searchQuery.isEmpty() || 
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.packageName.contains(searchQuery, ignoreCase = true)
            }
    }
    
    val favoriteApps = filteredApps.filter { it.isFavorite }
    val recentApps = filteredApps.filter { it.isRecent && !it.isFavorite }
    val otherApps = filteredApps.filter { !it.isFavorite && !it.isRecent }
    
    WePrayDialog(
        onDismissRequest = onDismiss,
        title = "Select Target App",
        modifier = Modifier.width(550.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            // Search and filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WePrayTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search by name or package",
                    leadingIcon = Icons.Outlined.Search,
                    modifier = Modifier.weight(1f)
                )
            }
            
            WePrayCheckbox(
                checked = excludeSystemApps,
                onCheckedChange = onExcludeSystemAppsChange,
                label = "Exclude System Apps"
            )
            
            // App list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)  // Fixed height for stable UI
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                if (filteredApps.isEmpty()) {
                    Text(
                        text = "No apps found",
                        style = WePrayTheme.typography.bodyMedium,
                        color = WePrayTheme.colors.textSecondary,
                        modifier = Modifier.padding(WePrayTheme.spacing.md)
                    )
                } else {
                    // Favorite apps
                    if (favoriteApps.isNotEmpty()) {
                        AppSection(
                            title = "Favorites",
                            icon = Icons.Outlined.Star,
                            apps = favoriteApps,
                            onAppClick = onAppSelected,
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                    
                    // Recent apps
                    if (recentApps.isNotEmpty()) {
                        if (favoriteApps.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = WePrayTheme.spacing.sm),
                                color = WePrayTheme.colors.border
                            )
                        }
                        AppSection(
                            title = "Recent",
                            icon = Icons.Outlined.Schedule,
                            apps = recentApps,
                            onAppClick = onAppSelected,
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                    
                    // All apps
                    if (otherApps.isNotEmpty()) {
                        if (favoriteApps.isNotEmpty() || recentApps.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = WePrayTheme.spacing.sm),
                                color = WePrayTheme.colors.border
                            )
                        }
                        AppSection(
                            title = "All Apps",
                            icon = Icons.Outlined.Apps,
                            apps = otherApps,
                            onAppClick = onAppSelected,
                            onFavoriteToggle = onFavoriteToggle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onFavoriteToggle: (AppInfo) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = WePrayTheme.spacing.sm, vertical = WePrayTheme.spacing.xs)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = WePrayTheme.colors.textTertiary
            )
            Text(
                text = title,
                style = WePrayTheme.typography.labelSmall,
                color = WePrayTheme.colors.textTertiary
            )
        }
        
        apps.forEach { app ->
            AppMenuItem(
                app = app,
                onClick = { onAppClick(app) },
                onFavoriteToggle = { onFavoriteToggle(app) }
            )
        }
    }
}

@Composable
private fun AppMenuItem(
    app: AppInfo,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(WePrayTheme.shapes.default)
            .clickable { onClick() }
            .padding(WePrayTheme.spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = null,
                modifier = Modifier.size(WePrayTheme.iconSize.default),
                tint = WePrayTheme.colors.primary
            )
            Column {
                Text(
                    text = app.name,
                    style = WePrayTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = WePrayTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.packageName,
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        WePrayIconButton(
            icon = if (app.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder,
            contentDescription = "즐겨찾기",
            onClick = onFavoriteToggle,
            size = IconButtonSize.Small,
            variant = if (app.isFavorite) IconButtonVariant.Primary else IconButtonVariant.Default
        )
    }
}

// Category Manager Dialog
@Composable
private fun CategoryManagerDialog(
    categories: List<DeepLinkCategory>,
    onCategoriesUpdated: (List<DeepLinkCategory>) -> Unit,
    onCategoryAdd: (String, Long) -> Unit,  // Color를 Long으로 변경
    onCategoryDelete: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryColor by remember { mutableStateOf(Color(0xFF3B82F6)) }
    
    WePrayDialog(
        onDismissRequest = onDismiss,
        title = "Manage Categories",
        modifier = Modifier.width(700.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xl)
        ) {
            // Add new category
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Add New Category",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.textPrimary
                )
                
                WePrayTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    placeholder = "Category name"
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
                ) {
                    // Left - Preview badge
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        Text(
                            text = "Preview",
                            style = WePrayTheme.typography.labelSmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                        WePrayBadge(
                            text = newCategoryName.ifEmpty { "Category Name" },
                            backgroundColor = newCategoryColor,
                            textColor = getContrastColor(newCategoryColor)
                        )
                    }
                    
                    // Right - Color picker
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                    ) {
                        Text(
                            text = "Background Color",
                            style = WePrayTheme.typography.labelSmall,
                            color = WePrayTheme.colors.textSecondary
                        )
                        
                        WePrayColorPicker(
                            selectedColor = newCategoryColor,
                            onColorSelected = { newCategoryColor = it }
                        )
                    }
                }
            }
            
            HorizontalDivider(color = WePrayTheme.colors.border)
            
            // Existing categories
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Existing Categories",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.textPrimary
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
                ) {
                    categories.forEach { category ->
                        val categoryColor = Color(category.colorValue.toULong())
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(WePrayTheme.shapes.default)
                                .background(WePrayTheme.colors.surfaceVariant)
                                .padding(horizontal = WePrayTheme.spacing.xl, vertical = WePrayTheme.spacing.md),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WePrayBadge(
                                text = category.name,
                                backgroundColor = categoryColor,
                                textColor = getContrastColor(categoryColor)
                            )
                            
                            WePrayIconButton(
                                icon = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                onClick = {
                                    onCategoryDelete(category.id)
                                },
                                variant = IconButtonVariant.Danger,
                                size = IconButtonSize.Small
                            )
                        }
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                WePraySecondaryButton(
                    text = "Close",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                )

                WePrayPrimaryButton(
                    text = "Add Category",
                    onClick = {
                        if (newCategoryName.isNotEmpty()) {
                            // Convert Color to Long in UI layer
                            onCategoryAdd(newCategoryName, newCategoryColor.value.toLong())
                            newCategoryName = ""
                            newCategoryColor = Color(0xFF3B82F6)
                        }
                    },
                    enabled = newCategoryName.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

// Edit History Dialog
@Composable
private fun EditHistoryDialog(
    history: DeepLinkHistoryItem,
    apps: List<AppInfo>,
    categories: List<DeepLinkCategory>,
    onSave: (DeepLinkHistoryItem) -> Unit,
    onDismiss: () -> Unit
) {
    var editedUrl by remember { mutableStateOf(history.url) }
    var editedApp by remember { mutableStateOf(
        AppInfo(
            name = history.appName,
            packageName = history.packageName,
            icon = Icons.Outlined.Android
        )
    ) }
    var editedCategory by remember { mutableStateOf(categories.firstOrNull { it.id == history.categoryId }) }
    var showAppSelector by remember { mutableStateOf(false) }
    
    if (showAppSelector) {
        AppSelectorDialog(
            apps = apps,
            selectedApp = editedApp,
            excludeSystemApps = true,
            onExcludeSystemAppsChange = {},
            onAppSelected = { 
                editedApp = it
                showAppSelector = false
            },
            onDismiss = { showAppSelector = false },
            onFavoriteToggle = {}
        )
    }
    
    WePrayDialog(
        onDismissRequest = onDismiss,
        title = "Edit History",
        modifier = Modifier.width(500.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            // URL
            WePrayLabeledTextField(
                value = editedUrl,
                onValueChange = { editedUrl = it },
                label = "DEEP LINK URL",
                placeholder = "myapp://screen/detail?id=123",
                leadingIcon = Icons.Outlined.Link
            )
            
            // App
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
            ) {
                Text(
                    text = "TARGET APP",
                    style = WePrayTheme.typography.overline,
                    color = WePrayTheme.colors.textSecondary
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(WePrayTheme.shapes.input)
                        .background(WePrayTheme.colors.surface)
                        .border(
                            width = 1.dp,
                            color = WePrayTheme.colors.border,
                            shape = WePrayTheme.shapes.input
                        )
                        .clickable { showAppSelector = true }
                        .padding(WePrayTheme.spacing.inputPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = editedApp.icon,
                            contentDescription = null,
                            modifier = Modifier.size(WePrayTheme.iconSize.default),
                            tint = WePrayTheme.colors.primary
                        )
                        Column {
                            Text(
                                text = editedApp.name,
                                style = WePrayTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = WePrayTheme.colors.textPrimary
                            )
                            Text(
                                text = editedApp.packageName,
                                style = WePrayTheme.typography.bodySmall,
                                color = WePrayTheme.colors.textTertiary
                            )
                        }
                    }
                }
            }
            
            // Category
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm)
            ) {
                Text(
                    text = "CATEGORY (OPTIONAL)",
                    style = WePrayTheme.typography.overline,
                    color = WePrayTheme.colors.textSecondary
                )
                WePrayDropdown(
                    value = editedCategory,
                    onValueChange = { editedCategory = it },
                    items = categories,
                    placeholder = "Select category",
                    itemLabel = { it.name }
                )
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                WePraySecondaryButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                )
                WePrayPrimaryButton(
                    text = "Save",
                    onClick = {
                        onSave(
                            DeepLinkHistoryItem(
                                id = history.id,
                                url = editedUrl,
                                packageName = editedApp.packageName,
                                appName = editedApp.name,
                                categoryId = editedCategory?.id,
                                timestamp = history.timestamp
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = editedUrl.isNotEmpty()
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DeepLinkHistoryItemView(
    historyItem: DeepLinkHistoryItem,
    category: DeepLinkCategory?,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuPosition by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
    
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val formattedDate = remember(historyItem.timestamp) {
        dateFormatter.format(Date(historyItem.timestamp))
    }
    
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(WePrayTheme.shapes.default)
                .background(WePrayTheme.colors.surface)
                .border(
                    width = 1.dp,
                    color = WePrayTheme.colors.border,
                    shape = WePrayTheme.shapes.default
                )
                .clickable { onClick() }
                .onPointerEvent(PointerEventType.Press) { event ->
                    if (event.button == PointerButton.Secondary) {
                        contextMenuPosition = event.changes.first().position
                        showContextMenu = true
                        event.changes.first().consume()
                    }
                }
                .padding(WePrayTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
        ) {
            // URL (Most important - prominent display)
            Row(
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = historyItem.url,
                    style = WePrayTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = WePrayTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Secondary info - App, Category, Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Android,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = WePrayTheme.colors.textTertiary
                    )
                    Text(
                        text = historyItem.appName,
                        style = WePrayTheme.typography.bodySmall,
                        color = WePrayTheme.colors.textSecondary
                    )
                }
                
                // Category badge
                category?.let { cat ->
                    val badgeColor = Color(cat.colorValue.toULong())  // Convert Long to Color in UI layer

                    WePrayBadge(
                        text = cat.name,
                        backgroundColor = badgeColor,
                        textColor = getContrastColor(badgeColor)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Time
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = WePrayTheme.colors.textTertiary
                    )
                    Text(
                        text = formattedDate,
                        style = WePrayTheme.typography.bodySmall,
                        color = WePrayTheme.colors.textTertiary
                    )
                }
            }
        }
        
        // Context menu at mouse position
        if (showContextMenu) {
            Popup(
                onDismissRequest = { showContextMenu = false },
                properties = PopupProperties(focusable = true),
                offset = androidx.compose.ui.unit.IntOffset(
                    x = contextMenuPosition.x.toInt(),
                    y = contextMenuPosition.y.toInt()
                )
            ) {
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(WePrayTheme.shapes.default)
                        .background(WePrayTheme.colors.surface)
                        .border(
                            width = 1.dp,
                            color = WePrayTheme.colors.border,
                            shape = WePrayTheme.shapes.default
                        )
                        .padding(WePrayTheme.spacing.xs)
                ) {
                    // Edit option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(WePrayTheme.shapes.default)
                            .clickable {
                                showContextMenu = false
                                onEdit()
                            }
                            .padding(WePrayTheme.spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = WePrayTheme.colors.primary
                        )
                        Text(
                            text = "Edit",
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.textPrimary
                        )
                    }
                    
                    // Delete option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(WePrayTheme.shapes.default)
                            .clickable {
                                showContextMenu = false
                                onDelete()
                            }
                            .padding(WePrayTheme.spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = WePrayTheme.colors.error
                        )
                        Text(
                            text = "Delete",
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.error
                        )
                    }
                }
            }
        }
    }
}
