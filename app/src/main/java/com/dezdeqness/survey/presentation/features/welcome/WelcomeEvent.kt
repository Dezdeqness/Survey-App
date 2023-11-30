package com.dezdeqness.survey.presentation.features.welcome

import com.dezdeqness.survey.core.event.BaseEvent

sealed class WelcomeEvent : BaseEvent() {
    object NavigateToSurveyPage : WelcomeEvent()
}
