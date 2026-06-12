package com.dendron.quizzer.domain.usecase

import com.dendron.quizzer.domain.model.Game
import com.dendron.quizzer.presentation.question.AnswerResult
import com.dendron.quizzer.presentation.question.QuestionResult
import com.dendron.quizzer.presentation.question.QuestionState
import com.dendron.quizzer.presentation.question.QuestionUiError
import javax.inject.Inject

class BuildQuestionUiStateUseCase @Inject constructor() {
    operator fun invoke(game: Game): QuestionState {
        val question = game.getCurrentQuestion()
        val answers = (question.incorrectAnswer + question.correctAnswer).shuffled()

        return QuestionState(
            question = question.text,
            answers = answers.map { answer ->
                QuestionResult(
                    text = answer,
                    isCorrect = answer == question.correctAnswer,
                )
            },
            score = game.getScore().toString(),
            questionCount = game.getQuestionCount().toString(),
            questionNumber = game.getQuestionNumber().toString(),
            answerResult = AnswerResult.None,
            answer = "",
            category = question.category,
            difficulty = question.difficulty.name.lowercase().replaceFirstChar(Char::titlecase),
            gameEnded = game.isGameEnded(),
            isLoading = false,
            error = QuestionUiError.None,
        )
    }
}
