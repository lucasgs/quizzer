package com.dendron.quizzer.presentation.question

data class QuestionResult(val text: String, val isCorrect: Boolean)

sealed interface QuestionUiError {
    data object None : QuestionUiError
    data object NoAnswerSelected : QuestionUiError
    data object EmptyQuestions : QuestionUiError
    data class LoadingFailed(val message: String) : QuestionUiError
}

data class QuestionState(
    val question: String = "",
    val questionNumber: String = "",
    val questionCount: String = "",
    val selected: String = "",
    val score: String = "",
    val answers: List<QuestionResult> = emptyList(),
    val answerCorrect: String = "",
    val category: String = "",
    val difficulty: String = "",
    val gameEnded: Boolean = false,
    val answerResult: AnswerResult = AnswerResult.None,
    val answer: String = "",
    val isLoading: Boolean = false,
    val error: QuestionUiError = QuestionUiError.None,
)