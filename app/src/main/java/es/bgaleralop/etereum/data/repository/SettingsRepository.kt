package es.bgaleralop.etereum.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import es.bgaleralop.etereum.di.dataStore
import es.bgaleralop.etereum.domain.config.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
){
    private object PreferencesKeys {
        val PREFER_SLIDER_MODE = booleanPreferencesKey("preferSliderMode")
        val LAST_MISSION_FOLDER = stringPreferencesKey("lastMissionFolder")
        val DELETE_ORIGINAL = booleanPreferencesKey("deleteOriginal")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preferences ->
            UserSettings(
                preferSliderMode = preferences[PreferencesKeys.PREFER_SLIDER_MODE] ?: false,
                lastMissionFolder = preferences[PreferencesKeys.LAST_MISSION_FOLDER] ?: "",
                deleteOriginal = preferences[PreferencesKeys.DELETE_ORIGINAL] ?: false
            )
        }

    suspend fun updatePreferSliderMode(preferSliderMode: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.PREFER_SLIDER_MODE] = preferSliderMode }
    }

    suspend fun updateLastMissionFolder(lastMissionFolder: String) {
        context.dataStore.edit { it[PreferencesKeys.LAST_MISSION_FOLDER] = lastMissionFolder }
    }

    suspend fun updateDeleteOriginal(deleteOriginal: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.DELETE_ORIGINAL] = deleteOriginal }
    }
}
