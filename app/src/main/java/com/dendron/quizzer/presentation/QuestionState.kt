package com.dendron.quizzer.presentation

data class QuestionState(
    val question: String = "",
    val progress: String = "",
    val answers: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)