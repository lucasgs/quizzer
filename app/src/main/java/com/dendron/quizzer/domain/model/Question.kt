package com.dendron.quizzer.domain.model

enum class Type {
    Any,
    MultipleChoice,
    Boolean,
}

enum class Difficulty {
    Any,
    Easy,
    Medium,
    Hard,
}

data class Question(
    val category: String,
    val type: Type,
    val difficulty: Difficulty,
    val text: String,
    val correctAnswer: String,
    val incorrectAnswer: List<String>
)

enum class Category {
    Any,
    GeneralKnowledge,
    EntertainmentBooks,
    EntertainmentFilm,
    EntertainmentMusic,
    EntertainmentMusicalsAndTheatres,
    EntertainmentTelevision,
    EntertainmentVideoGames,
    EntertainmentBoardGames,
    ScienceAndNature,
    ScienceComputers,
    ScienceMathematics,
    Mythology,
    Sports,
    Geography,
    History,
    Politics,
    Art,
    Celebrities,
    Animals,
    Vehicles,
    EntertainmentComics,
    ScienceGadgets,
    EntertainmentJapaneseAnimeAndMange,
    EntertainmentCartoonAndAnimation,
}
