package com.dendron.quizzer.domain.repository

import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface TriviaRepository {
    suspend fun getQuestions(numberOfQuestions: Int = 10): Flow<Resource<List<Question>>>
}