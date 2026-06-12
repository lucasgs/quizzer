package com.dendron.quizzer.presentation.question

sealed class AnswerResult {
    data class Correct(val message: String) : AnswerResult()
    data class Incorrect(val message: String) : AnswerResult()
    data object None : AnswerResult()
}