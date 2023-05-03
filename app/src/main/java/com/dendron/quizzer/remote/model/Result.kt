package com.dendron.quizzer.remote.model

import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.model.Type
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("category")
    val category: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>,
    @SerializedName("question")
    val question: String,
    @SerializedName("type")
    val type: String
)

fun Result.toModel(): Question = Question(
    category = category,
    type = mapToType(type),
    difficulty = mapToDifficulty(difficulty),
    text = question,
    correctAnswer = correctAnswer,
    incorrectAnswer = incorrectAnswers,
)

fun mapToType(value: String): Type = when (value) {
    "multiple" -> Type.MultipleChoice
    "boolean" -> Type.Boolean
    else -> Type.Undefined
}

fun mapToDifficulty(value: String): Difficulty = when (value) {
    "hard" -> Difficulty.Hard
    "medium" -> Difficulty.Medium
    "easy" -> Difficulty.Easy
    else -> Difficulty.Undefined
}
