package com.dendron.quizzer.presentation.question

sealed class AnswerResult {
    class Correct(val message: String): AnswerResult()
    class Incorrect(val message: String): AnswerResult()
    object None : AnswerResult()
}