package com.dendron.quizzer.presentation.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.repository.SettingsRepository
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.OpenTriviaDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: TriviaRepository,
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
            questionRepository.getQuestions(
                numberOfQuestions = settings.questionCount,
                difficulty = settings.difficulty,
                category = settings.category,
            ).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                question = "",
                                answers = emptyList(),
                                error = resource.message.toUiError()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                error = QuestionUiError.None
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
                                    error = QuestionUiError.EmptyQuestions
                                )
                            }
                        } else {
                            game.start(resource.data)
                            updateGameState()
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun updateGameState() {
        val question = game.getCurrentQuestion()
        val answers = (question.incorrectAnswer + question.correctAnswer).shuffled()
        _state.update { questionState ->
            questionState.copy(
                question = question.text,
                answers = answers.map { answer ->
                    QuestionResult(
                        text = answer,
                        isCorrect = (answer == question.correctAnswer)
                    )
                },
                score = game.getScore().toString(),
                questionCount = game.getQuestionCount().toString(),
                questionNumber = game.getQuestionNumber().toString(),
                answerResult = AnswerResult.None,
                answer = "",
                gameEnded = game.isGameEnded(),
                isLoading = false,
                error = QuestionUiError.None,
            )
        }
    }

    private fun nextQuestion() {
        val currentAnswer = state.value.answer
        if (currentAnswer.isEmpty()) {
            _state.update { currentState -> currentState.copy(error = QuestionUiError.NoAnswerSelected) }
        } else {
            game.nextQuestion()
            updateGameState()
        }
    }

    private fun setAnswer(answer: String) {
        val answerResult = state.value.answerResult
        if (answerResult == AnswerResult.None) {
            _state.update { currentState ->
                currentState.copy(
                    answer = answer,
                    error = QuestionUiError.None,
                    answerResult = if (game.checkAnswer(answer)) {
                        AnswerResult.Correct(
                            message = "Nice :)"
                        )
                    } else {
                        AnswerResult.Incorrect(buildString {
                            append("The correct was: '")
                            append(game.getCurrentCorrectAnswer())
                            append("'")
                        })
                    }
                )
            }
        }
    }

    private fun String?.toUiError(): QuestionUiError = when (this) {
        OpenTriviaDbRepository.NO_RESULTS_MESSAGE -> QuestionUiError.EmptyQuestions
        null, "" -> QuestionUiError.LoadingFailed(OpenTriviaDbRepository.NETWORK_ERROR_MESSAGE)
        else -> QuestionUiError.LoadingFailed(this)
    }
}