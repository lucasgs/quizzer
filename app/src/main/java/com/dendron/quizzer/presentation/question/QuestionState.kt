package com.dendron.quizzer.presentation.question

data class QuestionResult(val text: String, val isCorrect: Boolean)

data class QuestionState(
    val question: String = "",
    val questionNumber: String = "",
    val questionCount: String = "",
    val selected: String = "",
    val score: String = "",
    val answers: List<QuestionResult> = emptyList(),
)