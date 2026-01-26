package com.ksssssw.wepray.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Deep link history entity
 */
@Entity(tableName = "deep_link_history")
data class DeepLinkHistoryEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val packageName: String,
    val appName: String,
    val categoryId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

