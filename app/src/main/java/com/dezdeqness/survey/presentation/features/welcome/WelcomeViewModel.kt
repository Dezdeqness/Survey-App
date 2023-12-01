package com.dezdeqness.survey.presentation.features.welcome

import com.dezdeqness.survey.core.BaseViewModel
import com.dezdeqness.survey.core.CoroutineDispatcherProvider
import com.dezdeqness.survey.core.event.BaseEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : BaseViewModel(coroutineDispatcherProvider = coroutineDispatcherProvider) {

    private val _welcomeState: MutableStateFlow<WelcomeState> = MutableStateFlow(WelcomeState())
    val welcomeState: StateFlow<WelcomeState> = _welcomeState

    fun onStartSurveyClicked() {
        _welcomeState.value =
            _welcomeState.value.copy(
                events = _welcomeState.value.events + WelcomeEvent.NavigateToSurveyPage
            )
    }

    override fun onEventConsumed(event: BaseEvent) {
        val value = _welcomeState.value

        _welcomeState.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

}
