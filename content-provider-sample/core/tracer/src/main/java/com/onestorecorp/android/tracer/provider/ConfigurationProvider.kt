package com.onestorecorp.android.tracer.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.onestorecorp.android.tracer.data.AppConfiguration
import com.onestorecorp.android.tracer.storage.ConfigurationStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConfigurationProvider : ContentProvider() {
    
    private lateinit var storage: ConfigurationStorage
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    companion object {
        const val AUTHORITY = "com.onestorecorp.android.cp_tracer.provider"
        const val PATH_CONFIGURATION = "configuration"
        
        private const val CODE_CONFIGURATION = 1
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_CONFIGURATION, CODE_CONFIGURATION)
        }
        
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_CONFIGURATION")
        
        // Cursor columns
        const val COLUMN_CONFIGURATION = "configuration"
    }
    
    override fun onCreate(): Boolean {
        storage = ConfigurationStorage(context!!)
        return true
    }
    
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            CODE_CONFIGURATION -> {
                val config = storage.loadConfiguration()
                val jsonString = json.encodeToString(config)
                
                MatrixCursor(arrayOf(COLUMN_CONFIGURATION)).apply {
                    addRow(arrayOf(jsonString))
                }
            }
            else -> null
        }
    }
    
    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CODE_CONFIGURATION -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_CONFIGURATION"
            else -> null
        }
    }
    
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Not supported
        return null
    }
    
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // Not supported
        return 0
    }
    
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            CODE_CONFIGURATION -> {
                values?.getAsString(COLUMN_CONFIGURATION)?.let { jsonString ->
                    try {
                        val config = json.decodeFromString<AppConfiguration>(jsonString)
                        storage.saveConfiguration(config)
                        context?.contentResolver?.notifyChange(uri, null)
                        1
                    } catch (e: Exception) {
                        0
                    }
                } ?: 0
            }
            else -> 0
        }
    }
}

