package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme

/**
 * WePray Search Bar
 * Based on HTML design with icon and clear button
 */
@Composable
fun WePraySearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) WePrayTheme.elevation.sm else 0.dp,
                shape = WePrayTheme.shapes.input
            )
            .clip(WePrayTheme.shapes.input)
            .background(WePrayTheme.colors.surfaceVariant)
            .border(
                width = 1.dp,
                color = if (isFocused) 
                    WePrayTheme.colors.primary 
                else 
                    WePrayTheme.colors.border,
                shape = WePrayTheme.shapes.input
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        enabled = enabled,
        singleLine = true,
        textStyle = WePrayTheme.typography.bodyMedium.copy(
            color = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
        ),
        cursorBrush = SolidColor(WePrayTheme.colors.primary),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WePrayTheme.spacing.inputPadding),
                horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search icon
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(WePrayTheme.iconSize.default),
                    tint = if (isFocused) 
                        WePrayTheme.colors.primary 
                    else 
                        WePrayTheme.colors.textSecondary
                )
                
                // Text field with placeholder
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = WePrayTheme.typography.bodyMedium,
                            color = WePrayTheme.colors.textSecondary
                        )
                    }
                    innerTextField()
                }
                
                // Clear button (shown when there's text)
                if (value.isNotEmpty()) {
                    WePrayIconButton(
                        icon = Icons.Outlined.Close,
                        contentDescription = "Clear",
                        onClick = { onValueChange("") },
                        size = IconButtonSize.Small
                    )
                }
            }
        }
    )
}

/**
 * WePray Search Bar with Filter Button
 * Extended search with sort/filter options
 */
@Composable
fun WePraySearchBarWithFilter(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    onFilterClick: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search bar
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = if (isFocused) WePrayTheme.elevation.sm else 0.dp,
                    shape = WePrayTheme.shapes.input
                )
                .clip(WePrayTheme.shapes.input)
                .background(WePrayTheme.colors.surfaceVariant)
                .border(
                    width = 1.dp,
                    color = if (isFocused) 
                        WePrayTheme.colors.primary 
                    else 
                        WePrayTheme.colors.border,
                    shape = WePrayTheme.shapes.input
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            enabled = enabled,
            singleLine = true,
            textStyle = WePrayTheme.typography.bodyMedium.copy(
                color = if (enabled) WePrayTheme.colors.textPrimary else WePrayTheme.colors.textDisabled
            ),
            cursorBrush = SolidColor(WePrayTheme.colors.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(WePrayTheme.spacing.inputPadding),
                    horizontalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(WePrayTheme.iconSize.default),
                        tint = if (isFocused) 
                            WePrayTheme.colors.primary 
                        else 
                            WePrayTheme.colors.textSecondary
                    )
                    
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = WePrayTheme.typography.bodyMedium,
                                color = WePrayTheme.colors.textSecondary
                            )
                        }
                        innerTextField()
                    }
                    
                    if (value.isNotEmpty()) {
                        WePrayIconButton(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Clear",
                            onClick = { onValueChange("") },
                            size = IconButtonSize.Small
                        )
                    }
                }
            }
        )
        
        // Filter/Sort button
        WePrayIconButton(
            icon = androidx.compose.material.icons.Icons.Outlined.FilterList,
            contentDescription = "Filter",
            onClick = onFilterClick,
            variant = IconButtonVariant.Default
        )
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(800.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xxxl)
        ) {
            Text(
                text = "Search Bar Variants",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            // Empty state
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Empty State",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                var searchText1 by remember { mutableStateOf("") }
                WePraySearchBar(
                    value = searchText1,
                    onValueChange = { searchText1 = it },
                    placeholder = "Filter APKs by name..."
                )
            }
            
            // With text
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "With Text",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                var searchText2 by remember { mutableStateOf("instagram") }
                WePraySearchBar(
                    value = searchText2,
                    onValueChange = { searchText2 = it },
                    placeholder = "Search..."
                )
            }
            
            // Disabled
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Disabled",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                WePraySearchBar(
                    value = "Disabled search",
                    onValueChange = {},
                    enabled = false
                )
            }
            
            // With filter button
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "With Filter Button",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                var searchText3 by remember { mutableStateOf("") }
                WePraySearchBarWithFilter(
                    value = searchText3,
                    onValueChange = { searchText3 = it },
                    placeholder = "Search devices...",
                    onFilterClick = { println("Filter clicked") }
                )
            }
            
            // Use case
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.md)
            ) {
                Text(
                    text = "Use Case - APK Filter",
                    style = WePrayTheme.typography.headlineLarge,
                    color = WePrayTheme.colors.onBackground
                )
                
                var searchText4 by remember { mutableStateOf("com.instagram") }
                WePraySearchBarWithFilter(
                    value = searchText4,
                    onValueChange = { searchText4 = it },
                    placeholder = "Filter APKs by name...",
                    onFilterClick = { /* Open sort menu */ }
                )
                
                // Results info
                Text(
                    text = "Found 3 APKs matching \"${searchText4}\"",
                    style = WePrayTheme.typography.bodySmall,
                    color = WePrayTheme.colors.textSecondary
                )
            }
        }
    }
}

