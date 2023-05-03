package com.dendron.quizzer.data.model


import com.dendron.quizzer.remote.model.Result
import com.google.gson.annotations.SerializedName

data class Trivia(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Result>
)