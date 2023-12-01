package com.dezdeqness.survey.presentation.features.survey

sealed class SurveyAction {

    object OnRetryClicked: SurveyAction()

    object OnNextClicked: SurveyAction()

    object OnPrevClicked: SurveyAction()

    object OnNotificationShown: SurveyAction()

    object OnSubmitAnswerCLicked: SurveyAction()

    data class OnAnswerChanged(val text: String): SurveyAction()

}
