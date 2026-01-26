package com.ksssssw.wepray.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Category entity for deep link history categorization
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val colorHex: String, // Store as hex string (e.g., "FF3B82F6")
    val createdAt: Long = System.currentTimeMillis()
)

