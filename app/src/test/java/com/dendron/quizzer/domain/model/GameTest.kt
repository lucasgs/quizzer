package com.dendron.quizzer.domain.model

import org.junit.Before
import org.junit.Test

class GameTest {

    private lateinit var game: Game

    @Before
    fun setUp() {
        game = Game()
        game.start(questionList)
    }

    @Test
    fun `start should initialize the state correctly`() {
        val expectedQuestion = questionList.first()
        val expectedQuestionNumber = 1
        val expectedScore = 0
        val expectedStatus = Status.PLAYING

        game.start(questionList)

        assert(game.getCurrentQuestion() == expectedQuestion)
        assert(game.getQuestionNumber() == expectedQuestionNumber)
        assert(game.getScore() == expectedScore)
        assert(game.getStatus() == expectedStatus)
    }

    @Test
    fun `getQuestionNumber should return the correct count when question list is not empty`() {
        val expected = 2
        val current = game.getQuestionCount()
        assert(current == expected)
    }

    companion object {
        private val questionList = listOf(
            Question(
                category = "category",
                type = Type.Boolean,
                difficulty = Difficulty.Easy,
                text = "Which is the correct answer? A",
                correctAnswer = "A",
                incorrectAnswer = listOf(
                    "B", "C", "D"
                )
            ), Question(
                category = "category",
                type = Type.MultipleChoice,
                difficulty = Difficulty.Hard,
                text = "Which is the correct answer? B",
                correctAnswer = "B",
                incorrectAnswer = listOf(
                    "A", "C", "D"
                )
            )
        )
    }

    @Test
    fun `checkAnswer should return TRUE when the answer is correct`() {
        val current = game.checkAnswer(questionList.first().correctAnswer)
        assert(current)
    }

    @Test
    fun `checkAnswer should return FALSE when the answer is incorrect`() {
        val current = game.checkAnswer(questionList.first().incorrectAnswer.first())
        assert(!current)
    }

    @Test
    fun `getCurrentCorrectAnswer should return the correct answer`() {
        val expectedCorrect = questionList.first().correctAnswer
        val current = game.getCurrentCorrectAnswer()
        assert(current == expectedCorrect)
    }

    @Test
    fun `nextQuestion should update state and update score when prev answer was correct`() {
        val answer = questionList.first().correctAnswer

        val expectedQuestion = questionList[1]
        val expectedQuestionNumber = 2
        val expectedScore = 1
        val expectedStatus = Status.PLAYING

        game.checkAnswer(answer)
        game.nextQuestion()

        assert(game.getCurrentQuestion() == expectedQuestion)
        assert(game.getQuestionNumber() == expectedQuestionNumber)
        assert(game.getScore() == expectedScore)
        assert(game.getStatus() == expectedStatus)
    }

    @Test
    fun `nextQuestion should update state and NOT update score when prev answer was incorrect`() {
        val answer = questionList.first().incorrectAnswer.first()

        val expectedQuestion = questionList[1]
        val expectedQuestionNumber = 2
        val expectedScore = 0
        val expectedStatus = Status.PLAYING

        game.checkAnswer(answer)
        game.nextQuestion()

        assert(game.getCurrentQuestion() == expectedQuestion)
        assert(game.getQuestionNumber() == expectedQuestionNumber)
        assert(game.getScore() == expectedScore)
        assert(game.getStatus() == expectedStatus)
    }

    @Test
    fun `nextQuestion should change the status to ENDED when game is ended`(){
        val answer1 = questionList.first().correctAnswer
        val answer2 = questionList[1].correctAnswer

        val expectedScore = 2
        val expectedStatus = Status.ENDED

        game.checkAnswer(answer1)
        game.nextQuestion()
        game.checkAnswer(answer2)
        game.nextQuestion()


        assert(game.getScore() == expectedScore)
        assert(game.getStatus() == expectedStatus)

    }
}

