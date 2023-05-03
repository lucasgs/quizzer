package com.dendron.quizzer.remote

import com.dendron.quizzer.data.model.Trivia
import retrofit2.http.GET

interface TheOpenTriviaDbApi {
    @GET("api.php?amount=10")
    suspend fun getQuestions(): Trivia
}