package com.ksssssw.wepray.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.ksssssw.wepray.ui.theme.WePrayTheme
import com.ksssssw.wepray.ui.theme.tokens.Background
import com.ksssssw.wepray.ui.theme.tokens.BorderColor
import com.ksssssw.wepray.ui.theme.tokens.TextDisabled

/**
 * WePray Dropdown/Select
 * 
 * 디자인 가이드 스펙:
 * - Background: rgba(10, 10, 10, 0.8)
 * - Border: 2px solid rgba(255, 255, 255, 0.2)
 * - Border (Focus): 2px solid #4A9EE0
 * - Padding: 15px 20px
 * - Border Radius: 8px
 * - Text Color: #FFFFFF
 * - Font Size: 1rem (16px)
 * - Icon: expand_more (24px)
 * 
 * States:
 * - Default: Border rgba(255, 255, 255, 0.2)
 * - Focus: Border #4A9EE0, Glow 0 0 15px rgba(74, 158, 224, 0.3)
 * - Error: Border #FF3D00
 * - Success: Border #00E676
 * - Disabled: Opacity 0.5, cursor not-allowed
 */
@Composable
fun <T> WePrayDropdown(
    value: T?,
    onValueChange: (T) -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier,
    placeholder: String = "선택하세요",
    enabled: Boolean = true,
    isError: Boolean = false,
    isSuccess: Boolean = false,
    itemLabel: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    
    val borderColor = when {
        !enabled -> BorderColor.copy(alpha = 0.2f)
        isError -> WePrayTheme.colors.error
        isSuccess -> WePrayTheme.colors.success
        isFocused || expanded -> WePrayTheme.colors.primary
        else -> BorderColor.copy(alpha = 0.2f)
    }
    
    Box(
        modifier = modifier
    ) {
        // Dropdown Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isFocused || expanded) WePrayTheme.elevation.md else WePrayTheme.elevation.none,
                    shape = WePrayTheme.shapes.input,
                    spotColor = WePrayTheme.colors.primary.copy(alpha = 0.3f)
                )
                .clip(WePrayTheme.shapes.input)
                .background(Background.copy(alpha = 0.8f))
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = WePrayTheme.shapes.input
                )
                .clickable(enabled = enabled) {
                    expanded = !expanded
                }
                .padding(
                    horizontal = WePrayTheme.spacing.inputPaddingHorizontal,
                    vertical = WePrayTheme.spacing.inputPaddingVertical
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value?.let { itemLabel(it) } ?: placeholder,
                style = WePrayTheme.typography.bodySmall,
                color = if (value != null) {
                    if (enabled) WePrayTheme.colors.onBackground else WePrayTheme.colors.onBackground.copy(alpha = 0.5f)
                } else {
                    TextDisabled
                }
            )
            
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown",
                modifier = Modifier.size(WePrayTheme.iconSize.default),
                tint = if (enabled) WePrayTheme.colors.onSurfaceVariant else WePrayTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        
        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = WePrayTheme.shapes.input,
            containerColor = WePrayTheme.colors.surface,
            border = BorderStroke(1.dp, BorderColor),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = itemLabel(item),
                            style = WePrayTheme.typography.bodySmall,
                            color = WePrayTheme.colors.onSurface
                        )
                    },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (value == item) 
                                WePrayTheme.colors.hoverEffect 
                            else 
                                androidx.compose.ui.graphics.Color.Transparent
                        )
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropdownPreview() {
    WePrayTheme {
        Column(
            modifier = Modifier
                .width(600.dp)
                .background(WePrayTheme.colors.background)
                .padding(WePrayTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.lg)
        ) {
            Text(
                text = "Dropdown States",
                style = WePrayTheme.typography.displayMedium,
                color = WePrayTheme.colors.onBackground
            )
            
            val categories = listOf("홈 화면", "상세 화면", "설정 화면", "프로필 화면")
            
            // Default
            var selectedCategory1 by remember { mutableStateOf<String?>(null) }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Default",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayDropdown(
                    value = selectedCategory1,
                    onValueChange = { selectedCategory1 = it },
                    items = categories,
                    placeholder = "카테고리 선택"
                )
            }
            
            // With Selection
            var selectedCategory2 by remember { mutableStateOf<String?>("홈 화면") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Selected",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayDropdown(
                    value = selectedCategory2,
                    onValueChange = { selectedCategory2 = it },
                    items = categories
                )
            }
            
            // Error
            var selectedCategory3 by remember { mutableStateOf<String?>(null) }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Error State",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.error
                )
                WePrayDropdown(
                    value = selectedCategory3,
                    onValueChange = { selectedCategory3 = it },
                    items = categories,
                    placeholder = "필수 선택",
                    isError = true
                )
            }
            
            // Success
            var selectedCategory4 by remember { mutableStateOf<String?>("상세 화면") }
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Success State",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.success
                )
                WePrayDropdown(
                    value = selectedCategory4,
                    onValueChange = { selectedCategory4 = it },
                    items = categories,
                    isSuccess = true
                )
            }
            
            // Disabled
            Column(
                verticalArrangement = Arrangement.spacedBy(WePrayTheme.spacing.xs)
            ) {
                Text(
                    text = "Disabled",
                    style = WePrayTheme.typography.labelLarge,
                    color = WePrayTheme.colors.onSurfaceVariant
                )
                WePrayDropdown(
                    value = "설정 화면",
                    onValueChange = {},
                    items = categories,
                    enabled = false
                )
            }
        }
    }
}
