package com.dendron.quizzer.presentation.question

import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.model.Status
import com.dendron.quizzer.domain.repository.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: TriviaRepository) :
    ViewModel() {

    private var game = Game()

    private val _state = MutableStateFlow(QuestionState())
    val state = _state.asStateFlow()

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

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
        _loading.update { true }
        viewModelScope.launch {
            questionRepository.getQuestions().onEach { questionList ->
                game.start(questionList)
                updateGameState()
                _loading.update { false }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateGameState() {
//        val progress = "Question ${game.getProgress()}, Score: ${game.getScore()}"
        val progress = "Question ${game.getProgress()}"
        val question = game.getCurrentQuestion()
        val answers = (question.incorrectAnswer + question.correctAnswer).shuffled()
            .map { it.parseAsHtml().toString() }
        _state.update { questionState ->
            questionState.copy(
                progress = progress,
                question = question.text.parseAsHtml().toString(),
                answers = answers,
                score = game.getScore().toString()
            )
        }
    }

    fun nextQuestion() {
        val currentAnswer = _answer.value
        if (currentAnswer.isEmpty()) {
            _error.update {
                "Please, select an answer :)"
            }
        } else {
            game.checkAnswer(currentAnswer)
            game.nextQuestion()
            updateGameState()
            _error.update { "" }
            _answer.update { "" }
            _gameEnded.update {
                game.getStatus() == Status.ENDED
            }
        }
    }

    fun setAnswer(answer: String) {
        _answer.update { answer }
    }
}