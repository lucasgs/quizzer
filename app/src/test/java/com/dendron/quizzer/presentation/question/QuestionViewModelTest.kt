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
import com.dendron.quizzer.domain.usecase.BuildQuestionUiStateUseCase
import com.dendron.quizzer.domain.usecase.EvaluateAnswerUseCase
import com.dendron.quizzer.domain.usecase.FetchQuestionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init should expose empty state when repository returns no questions`() = runTest {
        val repository = FakeTriviaRepository(
            flowOf(
                Resource.Loading(),
                Resource.Success(emptyList())
            )
        )
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()

        assertEquals(QuestionUiError.EmptyQuestions, viewModel.state.value.error)
        assertEquals("", viewModel.state.value.question)
    }

    @Test
    fun `retry should reload questions after initial error`() = runTest {
        val repository = SequencedTriviaRepository(
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
        )
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
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

    @Test
    fun `next question should check answer before advancing`() = runTest {
        val repository = FakeTriviaRepository(flowOf(Resource.Loading(), Resource.Success(twoQuestionList)))
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()
        viewModel.onEvent(QuestionListEvent.SetAnswer("A"))
        viewModel.onEvent(QuestionListEvent.NextQuestion)

        assertEquals("1", viewModel.state.value.questionNumber)
        assertEquals(AnswerResult.Correct("Correct! Great job."), viewModel.state.value.answerResult)

        viewModel.onEvent(QuestionListEvent.NextQuestion)
        assertEquals("2", viewModel.state.value.questionNumber)
        assertEquals(AnswerResult.None, viewModel.state.value.answerResult)
    }

    @Test
    fun `init should expose populated success state when repository succeeds`() = runTest {
        val repository = FakeTriviaRepository(flowOf(Resource.Loading(), Resource.Success(questionList)))
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()

        assertEquals("question 1", viewModel.state.value.question)
        assertEquals("1", viewModel.state.value.questionNumber)
        assertEquals("1", viewModel.state.value.questionCount)
        assertEquals("category", viewModel.state.value.category)
        assertEquals("Easy", viewModel.state.value.difficulty)
        assertEquals(QuestionUiError.None, viewModel.state.value.error)
    }

    @Test
    fun `next question without selecting answer should expose selection error`() = runTest {
        val repository = FakeTriviaRepository(flowOf(Resource.Loading(), Resource.Success(questionList)))
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()
        viewModel.onEvent(QuestionListEvent.NextQuestion)

        assertEquals(QuestionUiError.NoAnswerSelected, viewModel.state.value.error)
        assertEquals(AnswerResult.None, viewModel.state.value.answerResult)
    }

    @Test
    fun `last question should mark game ended after checked answer`() = runTest {
        val repository = FakeTriviaRepository(flowOf(Resource.Loading(), Resource.Success(questionList)))
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()
        viewModel.onEvent(QuestionListEvent.SetAnswer("A"))
        viewModel.onEvent(QuestionListEvent.NextQuestion)
        assertEquals(AnswerResult.Correct("Correct! Great job."), viewModel.state.value.answerResult)

        viewModel.onEvent(QuestionListEvent.NextQuestion)
        assertTrue(viewModel.state.value.gameEnded)
    }

    @Test
    fun `set answer after evaluation should not overwrite checked answer`() = runTest {
        val repository = FakeTriviaRepository(flowOf(Resource.Loading(), Resource.Success(questionList)))
        val viewModel = QuestionViewModel(
            fetchQuestionsUseCase = FetchQuestionsUseCase(repository),
            evaluateAnswerUseCase = EvaluateAnswerUseCase(),
            buildQuestionUiStateUseCase = BuildQuestionUiStateUseCase(),
            settingsRepository = FakeSettingsRepository(),
            game = Game()
        )

        advanceUntilIdle()
        viewModel.onEvent(QuestionListEvent.SetAnswer("A"))
        viewModel.onEvent(QuestionListEvent.NextQuestion)
        viewModel.onEvent(QuestionListEvent.SetAnswer("B"))

        assertEquals("A", viewModel.state.value.answer)
        assertEquals(AnswerResult.Correct("Correct! Great job."), viewModel.state.value.answerResult)
    }

    private class FakeSettingsRepository : SettingsRepository {
        override fun getSettings(): Flow<Settings> = flowOf(Settings(10, Difficulty.Any, Category.Any))

        override suspend fun setSettings(newSettings: Settings) = Unit

        override suspend fun recordGameResult(score: Int, questionCount: Int) = Unit
    }

    private class FakeTriviaRepository(
        private val flow: Flow<Resource<List<Question>>>
    ) : TriviaRepository {
        override fun getQuestions(
            numberOfQuestions: Int,
            difficulty: Difficulty,
            category: Category
        ): Flow<Resource<List<Question>>> = flow
    }

    private class SequencedTriviaRepository(
        private val flows: List<Flow<Resource<List<Question>>>>
    ) : TriviaRepository {
        private var index = 0

        override fun getQuestions(
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

        private val twoQuestionList = listOf(
            questionList.first(),
            Question(
                category = "category",
                type = Type.MultipleChoice,
                difficulty = Difficulty.Medium,
                text = "question 2",
                correctAnswer = "B",
                incorrectAnswer = listOf("A", "C", "D")
            )
        )
    }
}
