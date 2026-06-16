package com.newtieba.ui.settings

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class SettingsUiState(
    val darkMode: Boolean = true,
    val theme: String = "System",
    val fontSize: Float = 1.0f,
    val imageQuality: String = "高",
    val notificationsEnabled: Boolean = true,
    val cacheSize: String = "计算中...",
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect { prefs ->
                _uiState.value = SettingsUiState(
                    darkMode = prefs[FOLLOW_SYSTEM_DARK] ?: true,
                    theme = prefs[THEME_MODE] ?: "System",
                    fontSize = prefs[FONT_SIZE] ?: 1.0f,
                    imageQuality = prefs[IMAGE_QUALITY] ?: "高",
                    notificationsEnabled = prefs[NOTIFICATIONS_ENABLED] ?: true,
                    cacheSize = "约 0 MB",
                )
            }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[FOLLOW_SYSTEM_DARK] = enabled
            }
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[THEME_MODE] = theme
            }
        }
    }

    fun setFontSize(size: Float) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[FONT_SIZE] = size
            }
        }
    }

    fun setImageQuality(quality: String) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[IMAGE_QUALITY] = quality
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[NOTIFICATIONS_ENABLED] = enabled
            }
        }
    }

    companion object {
        private val FOLLOW_SYSTEM_DARK = booleanPreferencesKey("follow_system_dark")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val FONT_SIZE = floatPreferencesKey("font_size")
        private val IMAGE_QUALITY = stringPreferencesKey("image_quality")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }
}
