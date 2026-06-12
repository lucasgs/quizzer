package com.dendron.quizzer.remote.model

import com.google.gson.annotations.SerializedName

data class Trivia(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Result>
)