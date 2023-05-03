package com.dendron.quizzer.domain.repository

import com.dendron.quizzer.domain.model.Question

interface TriviaRepository {
    suspend fun getQuestions(numberOfQuestions: Int = 10): List<Question>
}