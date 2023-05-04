package com.dendron.quizzer.domain.model

class Game(private val questionList: List<Question>) {

    private var questionNumber = 0
    private var score = 0

    fun getCurrentQuestion() = questionList[questionNumber]

    private fun getCurrentCorrectAnswer() = questionList[questionNumber].correctAnswer

//    fun getProgress() = (questionNumber + 1) / questionList.size.toFloat()
    fun getProgress() = "${questionNumber+1} / ${questionList.size}"

    fun getScore() = score

   fun checkAnswer(answer: String): Boolean {
       return if (getCurrentCorrectAnswer() == answer) {
           score += 1
           true
       } else {
           false
       }
    }
    fun nextQuestion() {
        if (questionNumber + 1 < questionList.size) {
            questionNumber++
        } else {
            questionNumber = 0
            score = 0
        }
    }
}