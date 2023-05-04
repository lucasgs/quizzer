package com.dendron.quizzer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.domain.repository.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: TriviaRepository) :
    ViewModel() {

    private lateinit var game: Game

    private val _state = MutableStateFlow(QuestionState())
    val state = _state.asStateFlow()

    init {
        fetchQuestionList()
    }

    private fun fetchQuestionList() {
        viewModelScope.launch {
            questionRepository.getQuestions().onEach { questionList ->
                game = Game(questionList)
                _state.value = QuestionState(
                    currentQuestion = game.getCurrentQuestion()
                )
            }.launchIn(viewModelScope)
        }
    }
}