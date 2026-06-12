package com.dendron.quizzer.domain.usecase

import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.TriviaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchQuestionsUseCase @Inject constructor(
    private val triviaRepository: TriviaRepository,
) {
    operator fun invoke(settings: Settings): Flow<Resource<List<Question>>> =
        triviaRepository.getQuestions(
            numberOfQuestions = settings.questionCount,
            difficulty = settings.difficulty,
            category = settings.category,
        )
}
