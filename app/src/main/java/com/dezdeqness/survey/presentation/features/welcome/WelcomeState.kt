package com.dezdeqness.survey.presentation.features.welcome

import com.dezdeqness.survey.core.event.BaseEvent

data class WelcomeState(
    val events: List<BaseEvent> = listOf(),
)
