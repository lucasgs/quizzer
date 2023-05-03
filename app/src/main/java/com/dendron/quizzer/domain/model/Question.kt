package com.dendron.quizzer.domain.model

enum class Type {
    MultipleChoice,
    Boolean,
    Undefined
}

enum class Difficulty {
    Easy,
    Medium,
    Hard,
    Undefined
}

data class Question(
    val category: String,
    val type: Type,
    val difficulty: Difficulty,
    val text: String,
    val correctAnswer: String,
    val incorrectAnswer: List<String>
)