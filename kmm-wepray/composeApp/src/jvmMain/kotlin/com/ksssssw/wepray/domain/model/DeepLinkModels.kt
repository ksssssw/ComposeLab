package com.ksssssw.wepray.domain.model

/**
 * Domain model for deep link category
 * 
 * @property colorValue Color value as Long (ARGB). Convert to Compose Color in UI layer using Color(colorValue)
 */
data class DeepLinkCategory(
    val id: String,
    val name: String,
    val colorValue: Long
)

/**
 * Domain model for deep link history item
 */
data class DeepLinkHistoryItem(
    val id: String,
    val url: String,
    val packageName: String,
    val appName: String,
    val categoryId: String? = null,
    val timestamp: Long
)

