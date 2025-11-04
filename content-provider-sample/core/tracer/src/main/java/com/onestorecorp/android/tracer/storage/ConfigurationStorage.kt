package com.onestorecorp.android.tracer.storage

import android.content.Context
import com.onestorecorp.android.tracer.data.AppConfiguration
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConfigurationStorage(context: Context) {
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
        sharedPreferences.edit()
            .putString(KEY_CONFIGURATION, jsonString)
            .apply()
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

