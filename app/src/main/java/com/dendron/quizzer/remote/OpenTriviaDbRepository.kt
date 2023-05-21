package com.dendron.quizzer.remote

import com.dendron.quizzer.common.Resource
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Question
import com.dendron.quizzer.domain.repository.TriviaRepository
import com.dendron.quizzer.remote.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenTriviaDbRepository @Inject constructor(private val api: OpenTriviaDbApi) :
    TriviaRepository {
    override suspend fun getQuestions(
        numberOfQuestions: Int,
        difficulty: Difficulty,
        category: Category,
    ): Flow<Resource<List<Question>>> =
        flow {
            try {
                emit(Resource.Loading())
                val questions =
                    api.getQuestions(
                        amount = numberOfQuestions,
                        difficulty = difficulty.toDataModel(),
                        category = category.toDataModel()
                    ).results.map { it.toModel() }
                emit(Resource.Success(questions))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }
}

fun Difficulty.toDataModel(): String = when (this) {
    Difficulty.Any -> ""
    Difficulty.Easy -> "easy"
    Difficulty.Medium -> "medium"
    Difficulty.Hard -> "hard"
}

fun Category.toDataModel(): String = when (this) {
    Category.Any -> ""
    Category.GeneralKnowledge -> "9"
    Category.EntertainmentBooks -> "10"
    Category.EntertainmentFilm -> "11"
    Category.EntertainmentMusic -> "12"
    Category.EntertainmentMusicalsAndTheatres -> "13"
    Category.EntertainmentTelevision -> "14"
    Category.EntertainmentVideoGames -> "15"
    Category.EntertainmentBoardGames -> "16"
    Category.ScienceAndNature -> "17"
    Category.ScienceComputers -> "18"
    Category.ScienceMathematics -> "19"
    Category.Mythology -> "20"
    Category.Sports -> "21"
    Category.Geography -> "22"
    Category.History -> "23"
    Category.Politics -> "24"
    Category.Art -> "25"
    Category.Celebrities -> "26"
    Category.Animals -> "27"
    Category.Vehicles -> "28"
    Category.EntertainmentComics -> "29"
    Category.ScienceGadgets -> "30"
    Category.EntertainmentJapaneseAnimeAndMange -> "31"
    Category.EntertainmentCartoonAndAnimation -> "32"
}