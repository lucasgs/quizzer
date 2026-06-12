package com.dendron.quizzer.remote

import app.cash.turbine.test
import com.dendron.quizzer.MainDispatcherRule
import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.data.model.Trivia
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.remote.model.Result
import com.dendron.quizzer.remote.model.toModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class OpenTriviaDbRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var api: OpenTriviaDbApi

    private lateinit var repository: OpenTriviaDbRepository

    @Before
    fun setUp() {
        repository = OpenTriviaDbRepository(api)
    }

    @Test
    fun `getQuestions should emit Loading and Success when is successful`() = runTest {
        whenever(api.getQuestions()).thenReturn(
            Trivia(
                responseCode = 0,
                results = questions
            )
        )

        val results = questions.map { it.toModel() }

        val expectedLoading = Resource.Loading<List<Question>>()
        val expectedSuccess = Resource.Success(results)

        repository.getQuestions(
            numberOfQuestions = questionNumber,
            difficulty = difficulty,
            category = category
        ).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedSuccess, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getQuestions should emit Loading and Error when request fails`() = runTest {
        val errorMessage = "error"
        val expectedLoading = Resource.Loading<List<Question>>()
        val expectedError = Resource.Error<List<Question>>(errorMessage)

        whenever(api.getQuestions()).thenThrow(RuntimeException(errorMessage))

        repository.getQuestions(
            numberOfQuestions = questionNumber,
            difficulty = difficulty,
            category = category
        ).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getQuestions should emit error when api returns no results`() = runTest {
        val expectedLoading = Resource.Loading<List<Question>>()
        val expectedError = Resource.Error<List<Question>>(OpenTriviaDbRepository.NO_RESULTS_MESSAGE)

        whenever(api.getQuestions()).thenReturn(
            Trivia(
                responseCode = 1,
                results = emptyList()
            )
        )

        repository.getQuestions(
            numberOfQuestions = questionNumber,
            difficulty = difficulty,
            category = category
        ).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getQuestions should emit error when success response has empty questions`() = runTest {
        val expectedLoading = Resource.Loading<List<Question>>()
        val expectedError = Resource.Error<List<Question>>(OpenTriviaDbRepository.NO_RESULTS_MESSAGE)

        whenever(api.getQuestions()).thenReturn(
            Trivia(
                responseCode = 0,
                results = emptyList()
            )
        )

        repository.getQuestions(
            numberOfQuestions = questionNumber,
            difficulty = difficulty,
            category = category
        ).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    companion object {

        private val questionNumber = 10
        private val difficulty = Difficulty.Any
        private val category = Category.Any

        private val questions = listOf(
            Result(
                category = "9",
                type = "multiple",
                difficulty = "easy",
                question = "question 1",
                correctAnswer = "D",
                incorrectAnswers = listOf(
                    "A", "B", "C"
                ),
            ),
            Result(
                category = "9",
                type = "multiple",
                difficulty = "easy",
                question = "question 2",
                correctAnswer = "B",
                incorrectAnswers = listOf(
                    "A", "D", "C"
                ),
            )
        )
    }
}