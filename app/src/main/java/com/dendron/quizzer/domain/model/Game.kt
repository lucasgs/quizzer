package com.dendron.quizzer.domain.model

import javax.inject.Inject

enum class Status {
    PLAYING,
    ENDED
}

class Game @Inject constructor() {

    private var questionList: MutableList<Question> = mutableListOf()
    private var questionNumber = 0
    private var score = 0
    private var status = Status.PLAYING

    fun start(questions: List<Question>) {
        questionList.clear()
        questionList.addAll(questions)
        questionNumber = 0
        score = 0
    }

    fun getCurrentQuestion() = questionList[questionNumber]

    fun getCurrentCorrectAnswer() = questionList[questionNumber].correctAnswer

    fun getQuestionNumber() = questionNumber + 1

    fun getQuestionCount() = questionList.size

    fun getScore() = score

    fun checkAnswer(answer: String): Boolean {
        return if (getCurrentCorrectAnswer() == answer) {
            score += 1
            true
        } else {
            false
        }
    }

    fun getStatus() = status

    fun nextQuestion() {
        status = Status.PLAYING
        if (questionNumber + 1 < questionList.size) {
            questionNumber++
        } else {
            status = Status.ENDED
        }
    }
}