package com.dendron.quizzer.presentation.question

import android.util.Log
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.model.Status
import com.dendron.quizzer.domain.repository.SettingsRepository
import com.dendron.quizzer.domain.repository.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _answerResult = MutableStateFlow<AnswerResult>(AnswerResult.None)
    val answerResult = _answerResult.asStateFlow()

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded = _state.map {
        game.getStatus() == (Status.ENDED)
    }.stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
    )

    init {
        fetchQuestionList()
    }

    private fun fetchQuestionList() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            questionRepository.getQuestions(
                numberOfQuestions = settings.questionCount,
                difficulty = settings.difficulty,
                category = settings.category,
            ).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _loading.update { false }
                        _error.update { "There was an error loading the questions :(" }
                        Log.e("Quizzer", "fetchQuestionList: ${resource.message.toString()}")
                    }

                    is Resource.Loading -> _loading.update { true }
                    is Resource.Success -> {
                        game.start(resource.data)
                        updateGameState()
                        _loading.update { false }
                    }
                }
            }.launchIn(viewModelScope)
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
                        text = answer, isCorrect = (answer == question.correctAnswer)
                    )
                },
                score = game.getScore().toString(),
                questionCount = game.getQuestionCount().toString(),
                questionNumber = game.getQuestionNumber().toString(),
            )
        }
    }

    fun nextQuestion() {
        val currentAnswer = _answer.value
        if (currentAnswer.isEmpty()) {
            _error.update {
                "Please, select an answer."
            }
        } else {
//            game.checkAnswer(currentAnswer)
            game.nextQuestion()
            updateGameState()
            _error.update { "" }
            _answer.update { "" }
            _answerResult.update { AnswerResult.None }
            _gameEnded.update {
                game.getStatus() == Status.ENDED
            }
        }
    }

    fun setAnswer(answer: String) {
        if (_answerResult.value == AnswerResult.None) {
            _answer.update { answer }
            _error.update { "" }

            _answerResult.update {
                if (game.checkAnswer(answer)) {
                    AnswerResult.Correct(
                        message = "Nice :)"
                    )
                } else {
                    AnswerResult.Incorrect(buildString {
                        append("The correct was: '")
                        append(game.getCurrentCorrectAnswer().parseAsHtml().toString())
                        append("'")
                    })
                }
            }
        }
    }
}