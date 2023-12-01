package com.dezdeqness.survey.presentation.features.welcome

import com.dezdeqness.survey.utils.TestCoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {

    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        viewModel = WelcomeViewModel(
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN user click start button SHOULD trigger NavigateToSurveyPage event`() = runTest {
        viewModel.onStartSurveyClicked()

        val uiState = viewModel.welcomeState.value

        assertAll(
            { assertTrue(uiState.events.size == 1) },
            {
                assertTrue(
                    uiState.events.filterIsInstance<WelcomeEvent.NavigateToSurveyPage>()
                        .isNotEmpty()
                )
            },
        )
    }


}
