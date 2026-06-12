package com.dendron.quizzer.presentation.question

sealed interface QuestionListEvent {
    object NextQuestion : QuestionListEvent
    object RetryLoad : QuestionListEvent
    data class SetAnswer(val answer: String) : QuestionListEvent
}