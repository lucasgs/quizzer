package com.dendron.quizzer.presentation

data class QuestionState(
    val question: String = "",
    val progress: String = "",
    val selected: String = "",
    val answers: List<String> = emptyList(),
)