package com.dezdeqness.survey.presentation.features.survey.model

data class QuestionUiModel(
    var id: Int,
    var question: String,
    var answer: String = "",
)
