package com.dendron.quizzer.local

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalSettingsRepositoryTest {

    @Test
    fun `getSettings should return defaults when preferences are empty`() = runTest {
        val repository = LocalSettingsRepository(createDataStore())

        val settings = repository.getSettings().first()

        assertEquals(Settings(), settings)
    }

    @Test
    fun `getSettings should fall back to Any for invalid enum values`() = runTest {
        val dataStore = createDataStore()
        dataStore.edit {
            it[intPreferencesKey("question_count")] = 7
            it[stringPreferencesKey("difficulty")] = "Impossible"
            it[stringPreferencesKey("category")] = "Unknown"
        }
        val repository = LocalSettingsRepository(dataStore)

        val settings = repository.getSettings().first()

        assertEquals(
            Settings(
                questionCount = 7,
                difficulty = Difficulty.Any,
                category = Category.Any,
            ),
            settings,
        )
    }

    @Test
    fun `setSettings should persist values`() = runTest {
        val repository = LocalSettingsRepository(createDataStore())
        val expected = Settings(
            questionCount = Constants.QUESTION_COUNT + 2,
            difficulty = Difficulty.Hard,
            category = Category.History,
        )

        repository.setSettings(expected)

        assertEquals(expected, repository.getSettings().first())
    }

    private fun createDataStore() = PreferenceDataStoreFactory.create(
        produceFile = { File.createTempFile("settings-test", ".preferences_pb") }
    )
}
