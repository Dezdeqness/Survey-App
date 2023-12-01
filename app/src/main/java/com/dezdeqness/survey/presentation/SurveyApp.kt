package com.dezdeqness.survey.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dezdeqness.survey.navigation.SURVEY_ROUTE
import com.dezdeqness.survey.navigation.WELCOME_ROUTE
import com.dezdeqness.survey.presentation.features.survey.SurveyPage
import com.dezdeqness.survey.presentation.features.welcome.WelcomePage

@Composable
fun SurveyApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController();

    NavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(WELCOME_ROUTE) {
            WelcomePage(navController = navController)
        }
        composable(SURVEY_ROUTE) {
            SurveyPage(navController = navController)
        }
    }
}
