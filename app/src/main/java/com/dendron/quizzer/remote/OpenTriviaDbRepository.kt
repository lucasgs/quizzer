package com.dendron.quizzer.remote

import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenTriviaDbRepository @Inject constructor(private val api: OpenTriviaDbApi) : TriviaRepository {
    override suspend fun getQuestions(numberOfQuestions: Int): Flow<List<Question>> = flow {
        try {
            val questions = api.getQuestions().results.map { it.toModel() }
            emit(questions)
        } catch (e: Exception) {
        }
    }
}