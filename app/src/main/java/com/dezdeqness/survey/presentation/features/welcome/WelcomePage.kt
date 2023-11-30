package com.dezdeqness.survey.presentation.features.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dezdeqness.survey.R
import com.dezdeqness.survey.navigation.SURVEY_ROUTE

@Composable
fun WelcomePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    welcomeViewModel: WelcomeViewModel = hiltViewModel(),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        Button(onClick = {
            welcomeViewModel.onStartSurveyClicked()
        }) {
            Text(stringResource(id = R.string.welcome_start_survey))
        }

        val state = welcomeViewModel.welcomeState.collectAsState()

        val currentValue = state.value

        currentValue.events.forEach { event ->
            when (event) {
                is WelcomeEvent.NavigateToSurveyPage -> {
                    navController.navigate(SURVEY_ROUTE)
                }
            }

            welcomeViewModel.onEventConsumed(event)
        }
    }

}
