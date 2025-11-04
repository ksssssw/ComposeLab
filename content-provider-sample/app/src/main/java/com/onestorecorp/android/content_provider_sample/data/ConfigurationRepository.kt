package com.onestorecorp.android.content_provider_sample.data

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.onestorecorp.android.tracer.data.AppConfiguration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json

class ConfigurationRepository(private val context: Context) {
    
    private val contentResolver: ContentResolver = context.contentResolver
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    companion object {
        private const val AUTHORITY = "com.onestorecorp.android.cp_tracer.provider"
        private const val PATH_CONFIGURATION = "configuration"
        private val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_CONFIGURATION")
        private const val COLUMN_CONFIGURATION = "configuration"
    }
    
    private fun isProviderAvailable(): Boolean {
        return try {
            val providerInfo = context.packageManager.resolveContentProvider(
                AUTHORITY,
                PackageManager.GET_META_DATA
            )
            providerInfo != null
        } catch (e: Exception) {
            false
        }
    }
    
    fun getConfiguration(): AppConfiguration {
        if (!isProviderAvailable()) {
            return AppConfiguration()
        }
        
        return try {
            val cursor = contentResolver.query(
                CONTENT_URI,
                arrayOf(COLUMN_CONFIGURATION),
                null,
                null,
                null
            )
            
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(COLUMN_CONFIGURATION)
                    if (columnIndex >= 0) {
                        val jsonString = it.getString(columnIndex)
                        json.decodeFromString<AppConfiguration>(jsonString)
                    } else {
                        AppConfiguration()
                    }
                } else {
                    AppConfiguration()
                }
            } ?: AppConfiguration()
        } catch (e: Exception) {
            e.printStackTrace()
            AppConfiguration()
        }
    }
    
    fun observeConfiguration(): Flow<AppConfiguration> = callbackFlow {
        // Emit initial value immediately
        trySend(getConfiguration())
        
        // Only register observer if provider is available
        if (!isProviderAvailable()) {
            awaitClose {
                // No observer to unregister
            }
            return@callbackFlow
        }
        
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(getConfiguration())
            }
        }
        
        try {
            contentResolver.registerContentObserver(CONTENT_URI, true, observer)
            
            awaitClose {
                try {
                    contentResolver.unregisterContentObserver(observer)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            awaitClose {
                // Registration failed, nothing to clean up
            }
        }
    }
}

