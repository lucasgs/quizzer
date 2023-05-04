package com.dendron.quizzer.presentation.question

import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.repository.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: TriviaRepository) :
    ViewModel() {

    private lateinit var game: Game

    private val _state = MutableStateFlow(QuestionState())
    val state = _state.asStateFlow()

    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        fetchQuestionList()
    }

    private fun fetchQuestionList() {
        viewModelScope.launch {
            questionRepository.getQuestions().onEach { questionList ->
                game = Game(questionList)
                updateGameState()
            }.launchIn(viewModelScope)
        }
    }

    private fun updateGameState() {
        val progress = "Question ${game.getProgress()}, Score: ${game.getScore()}"
        val question = game.getCurrentQuestion()
        val answers = (question.incorrectAnswer + question.correctAnswer).shuffled()
            .map { it.parseAsHtml().toString() }
        _state.update {
            _state.value.copy(
                progress = progress,
                question = question.text.parseAsHtml().toString(),
                answers = answers,
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
        }
    }

    fun setAnswer(answer: String) {
        _answer.update { answer }
    }
}