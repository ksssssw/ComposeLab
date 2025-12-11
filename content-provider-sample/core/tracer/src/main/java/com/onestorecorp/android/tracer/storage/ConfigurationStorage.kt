package com.onestorecorp.android.tracer.storage

import android.content.Context
import com.onestorecorp.android.tracer.data.AppConfiguration
import com.onestorecorp.android.tracer.provider.ConfigurationProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.edit

class ConfigurationStorage(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "configuration_prefs",
        Context.MODE_PRIVATE
    )
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    fun saveConfiguration(config: AppConfiguration) {
        val jsonString = json.encodeToString(config)
        sharedPreferences.edit {
            putString(KEY_CONFIGURATION, jsonString)
        }
        
        // Content Provider에 변경 알림
        context.contentResolver.notifyChange(
            ConfigurationProvider.CONTENT_URI,
            null
        )
    }
    
    fun loadConfiguration(): AppConfiguration {
        val jsonString = sharedPreferences.getString(KEY_CONFIGURATION, null)
        return if (jsonString != null) {
            try {
                json.decodeFromString<AppConfiguration>(jsonString)
            } catch (e: Exception) {
                AppConfiguration()
            }
        } else {
            AppConfiguration()
        }
    }
    
    companion object {
        private const val KEY_CONFIGURATION = "app_configuration"
    }
}

