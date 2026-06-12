package com.dendron.quizzer.presentation.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.repository.SettingsRepository
import com.dendron.quizzer.domain.usecase.BuildQuestionUiStateUseCase
import com.dendron.quizzer.domain.usecase.EvaluateAnswerUseCase
import com.dendron.quizzer.domain.usecase.FetchQuestionsUseCase
import com.dendron.quizzer.remote.OpenTriviaDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase,
    private val evaluateAnswerUseCase: EvaluateAnswerUseCase,
    private val buildQuestionUiStateUseCase: BuildQuestionUiStateUseCase,
    private val settingsRepository: SettingsRepository,
    private var game: Game
) : ViewModel() {

    private val _state = MutableStateFlow(QuestionState())
    val state = _state.asStateFlow()
    private var fetchJob: Job? = null

    init {
        fetchQuestionList()
    }

    fun onEvent(event: QuestionListEvent) {
        when (event) {
            QuestionListEvent.NextQuestion -> nextQuestion()
            QuestionListEvent.RetryLoad -> fetchQuestionList()
            is QuestionListEvent.SetAnswer -> setAnswer(event.answer)
        }
    }

    private fun fetchQuestionList() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            fetchQuestionsUseCase(settings).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                question = "",
                                answers = emptyList(),
                                answer = "",
                                answerResult = AnswerResult.None,
                                error = resource.message.toUiError()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                error = QuestionUiError.None,
                                answerResult = AnswerResult.None
                            )
                        }
                    }

                    is Resource.Success -> {
                        if (resource.data.isEmpty()) {
                            _state.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    question = "",
                                    answers = emptyList(),
                                    answer = "",
                                    answerResult = AnswerResult.None,
                                    error = QuestionUiError.EmptyQuestions
                                )
                            }
                        } else {
                            game.start(resource.data)
                            updateGameState()
                        }
                    }
                }
            }
        }
    }

    private fun updateGameState() {
        _state.value = buildQuestionUiStateUseCase(game)
    }

    private fun nextQuestion() {
        val currentState = state.value
        val currentAnswer = currentState.answer
        if (currentAnswer.isEmpty()) {
            _state.update { it.copy(error = QuestionUiError.NoAnswerSelected) }
            return
        }

        if (currentState.answerResult == AnswerResult.None) {
            checkAnswer(currentAnswer)
        } else {
            game.nextQuestion()
            updateGameState()
        }
    }

    private fun setAnswer(answer: String) {
        if (state.value.answerResult == AnswerResult.None) {
            _state.update { currentState ->
                currentState.copy(
                    answer = answer,
                    error = QuestionUiError.None,
                )
            }
        }
    }

    private fun checkAnswer(answer: String) {
        val correctAnswer = game.getCurrentCorrectAnswer()
        val isCorrect = game.checkAnswer(answer)
        _state.update { currentState ->
            currentState.copy(
                error = QuestionUiError.None,
                answerResult = evaluateAnswerUseCase(
                    correctAnswer = correctAnswer,
                    isCorrect = isCorrect,
                )
            )
        }
    }

    private fun String?.toUiError(): QuestionUiError = when (this) {
        OpenTriviaDbRepository.NO_RESULTS_MESSAGE -> QuestionUiError.EmptyQuestions
        null, "" -> QuestionUiError.LoadingFailed(OpenTriviaDbRepository.NETWORK_ERROR_MESSAGE)
        else -> QuestionUiError.LoadingFailed(this)
    }
}