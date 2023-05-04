package com.dendron.quizzer.presentation

import com.dendron.quizzer.domain.model.Question

data class QuestionState(
    val currentQuestion: Question? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)