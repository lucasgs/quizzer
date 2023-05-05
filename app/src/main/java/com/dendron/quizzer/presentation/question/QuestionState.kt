package com.dendron.quizzer.presentation.question

data class QuestionState(
    val question: String = "",
    val progress: String = "",
    val selected: String = "",
    val score: String = "",
    val answers: List<String> = emptyList(),
)