package com.dendron.quizzer.remote

import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenTriviaDbRepository @Inject constructor(private val api: OpenTriviaDbApi) :
    TriviaRepository {
    override suspend fun getQuestions(numberOfQuestions: Int): Flow<Resource<List<Question>>> =
        flow {
            try {
                emit(Resource.Loading())
                val questions =
                    api.getQuestions(amount = numberOfQuestions).results.map { it.toModel() }
                emit(Resource.Success(questions))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }
}