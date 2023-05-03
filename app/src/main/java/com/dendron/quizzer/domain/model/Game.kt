package com.dendron.quizzer.domain.model

class Game(private val questionList: List<Question>) {

    private var questionNumber = 0
    private var score = 0

    fun getCurrentQuestion() = questionList[questionNumber].text

    fun getCurrentIncorrectAnswer() = questionList[questionNumber].incorrectAnswer

    fun getCurrentCorrectAnswer() = questionList[questionNumber].correctAnswer

    fun getProgress() = (questionNumber + 1) / questionList.size

    fun getScore() = score

    fun nextQuestion() {
        if (questionNumber + 1 < questionList.size) {
            questionNumber++
        } else {
            questionNumber = 0
            score = 0
        }
    }
}