package com.dendron.quizzer.domain.usecase

import com.dendron.quizzer.presentation.question.AnswerResult
import javax.inject.Inject

class EvaluateAnswerUseCase @Inject constructor() {
    operator fun invoke(correctAnswer: String, isCorrect: Boolean): AnswerResult =
        if (isCorrect) {
            AnswerResult.Correct("Correct! Great job.")
        } else {
            AnswerResult.Incorrect("Incorrect. The right answer is $correctAnswer")
        }
}
