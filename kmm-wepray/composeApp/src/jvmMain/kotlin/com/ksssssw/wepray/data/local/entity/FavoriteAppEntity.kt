package com.ksssssw.wepray.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Favorite app entity for deep link tester
 */
@Entity(tableName = "favorite_apps")
data class FavoriteAppEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val addedAt: Long = System.currentTimeMillis()
)

