package com.dendron.quizzer.presentation.question

import com.dendron.quizzer.MainDispatcherRule
import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.model.Type
import com.dendron.quizzer.domain.repository.SettingsRepository
import com.dendron.quizzer.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class QuestionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init should expose empty state when repository returns no questions`() = runTest {
        val viewModel = QuestionViewModel(
            questionRepository = FakeTriviaRepository(
                flowOf(
                    Resource.Loading(),
                    Resource.Success(emptyList())
                )
            ),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()

        assertEquals(QuestionUiError.EmptyQuestions, viewModel.state.value.error)
        assertEquals("", viewModel.state.value.question)
    }

    @Test
    fun `retry should reload questions after initial error`() = runTest {
        val viewModel = QuestionViewModel(
            questionRepository = SequencedTriviaRepository(
                listOf(
                    flowOf(
                        Resource.Loading(),
                        Resource.Error("Network down")
                    ),
                    flowOf(
                        Resource.Loading(),
                        Resource.Success(questionList)
                    )
                )
            ),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()
        assertEquals(QuestionUiError.LoadingFailed("Network down"), viewModel.state.value.error)

        viewModel.onEvent(QuestionListEvent.RetryLoad)
        advanceUntilIdle()

        assertEquals(QuestionUiError.None, viewModel.state.value.error)
        assertTrue(viewModel.state.value.question.isNotEmpty())
        assertEquals("1", viewModel.state.value.questionNumber)
    }

    private class FakeSettingsRepository : SettingsRepository {
        override fun getSettings(): Flow<Settings> = flowOf(Settings(10, Difficulty.Any, Category.Any))

        override suspend fun setSettings(newSettings: Settings) = Unit
    }

    private class FakeTriviaRepository(
        private val flow: Flow<Resource<List<Question>>>
    ) : TriviaRepository {
        override suspend fun getQuestions(
            numberOfQuestions: Int,
            difficulty: Difficulty,
            category: Category
        ): Flow<Resource<List<Question>>> = flow
    }

    private class SequencedTriviaRepository(
        private val flows: List<Flow<Resource<List<Question>>>>
    ) : TriviaRepository {
        private var index = 0

        override suspend fun getQuestions(
            numberOfQuestions: Int,
            difficulty: Difficulty,
            category: Category
        ): Flow<Resource<List<Question>>> = flows[index++]
    }

    companion object {
        private val questionList = listOf(
            Question(
                category = "category",
                type = Type.MultipleChoice,
                difficulty = Difficulty.Easy,
                text = "question 1",
                correctAnswer = "A",
                incorrectAnswer = listOf("B", "C", "D")
            )
        )
    }
}
