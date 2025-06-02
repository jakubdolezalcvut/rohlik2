package cz.rohlik.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.rohlik.domain.UiChoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

private object PreferencesKeys {
    val UI_CHOICE = intPreferencesKey("ui_choice")
}

internal class UiChoiceDataSource(
    private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore(
        name = "user_preferences",
    )

    val uiChoiceFlow: Flow<UiChoice> = context.dataStore.data
        .catch { exception ->
            Timber.e(exception, "Could not load preferences.")
            emit(emptyPreferences())
        }
        .map { preferences ->
            val value = preferences[PreferencesKeys.UI_CHOICE]

            UiChoice.entries.find { choice -> choice.value == value }
                ?: UiChoice.HUMAN
        }

    suspend fun update(uiChoice: UiChoice) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.UI_CHOICE] = uiChoice.value
        }
    }
}
