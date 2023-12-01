package com.dezdeqness.survey.presentation.features.survey

import com.dezdeqness.survey.presentation.features.survey.model.QuestionUiModel

data class SurveyState(
    var isLoading: Boolean = false,
    var questions: List<QuestionUiModel> = listOf(),
    var currentQuestionIndex: Int = -1,
    var submittedQuestionCounter: Int = 0,
    var isQuestionSubmitted: Boolean = false,
    var isPrevActionEnabled: Boolean = false,
    var isNextActionEnabled: Boolean = false,
    var isError: Boolean = false,
    var currentAnswerStatus: AnswerStatus = AnswerStatus.None,
)

enum class AnswerStatus {
    None, Success, Failure
}
