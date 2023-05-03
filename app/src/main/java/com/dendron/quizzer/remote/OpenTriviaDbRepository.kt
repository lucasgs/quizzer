package com.dendron.quizzer.remote

import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.model.toModel

class OpenTriviaDbRepository(private val api: TheOpenTriviaDbApi) : TriviaRepository {
    override suspend fun getQuestions(numberOfQuestions: Int): List<Question> {
        return api.getQuestions().results.map { it.toModel() }
    }
}

