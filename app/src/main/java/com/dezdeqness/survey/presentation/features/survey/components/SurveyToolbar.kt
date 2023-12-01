package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dezdeqness.survey.R
import com.dezdeqness.survey.presentation.features.survey.SurveyState
import com.dezdeqness.survey.presentation.features.survey.model.QuestionUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyToolbar(
    state: SurveyState,
    navController: NavController,
    modifier: Modifier = Modifier,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    val status = stringResource(
        id = R.string.survey_status,
        state.currentQuestionIndex + 1,
        state.questions.size
    )

    val isPrevEnabled = state.isPrevActionEnabled
    val isNextEnabled = state.isNextActionEnabled

    val isStatusInvisible = remember(state.isLoading, state.isError) {
        state.isLoading || state.isError
    }

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (isStatusInvisible.not()) {
                Text(status)
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                enabled = isPrevEnabled,
                onClick = {
                    onPrevClicked()
                },
            ) {
                Text(stringResource(id = R.string.survey_prev_label))
            }
            TextButton(
                enabled = isNextEnabled,
                onClick = {
                    onNextClicked()
                },
            ) {
                Text(stringResource(id = R.string.survey_next_label))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyToolbarPreviewDefault() {
    val navController = rememberNavController()

    val state =
        SurveyState(
            isNextActionEnabled = true,
            isPrevActionEnabled = true,
            currentQuestionIndex = 2,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                ),
                QuestionUiModel(
                    id = 2,
                    question = "Question2",
                ),
                QuestionUiModel(
                    id = 3,
                    question = "Question3",
                ),
                QuestionUiModel(
                    id = 4,
                    question = "Question4",
                )
            ),
        )

    SurveyToolbar(
        state = state,
        navController = navController,
        onPrevClicked = { },
        onNextClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyToolbarPreviewFirstQuestion() {
    val navController = rememberNavController()

    val state =
        SurveyState(
            isNextActionEnabled = true,
            isPrevActionEnabled = false,
            currentQuestionIndex = 0,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                ),
                QuestionUiModel(
                    id = 2,
                    question = "Question2",
                ),
                QuestionUiModel(
                    id = 3,
                    question = "Question3",
                ),
                QuestionUiModel(
                    id = 4,
                    question = "Question4",
                )
            ),
        )

    SurveyToolbar(
        state = state,
        navController = navController,
        onPrevClicked = { },
        onNextClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyToolbarPreviewLastQuestion() {
    val navController = rememberNavController()

    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = true,
            currentQuestionIndex = 3,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                ),
                QuestionUiModel(
                    id = 2,
                    question = "Question2",
                ),
                QuestionUiModel(
                    id = 3,
                    question = "Question3",
                ),
                QuestionUiModel(
                    id = 4,
                    question = "Question4",
                )
            ),
        )

    SurveyToolbar(
        state = state,
        navController = navController,
        onPrevClicked = { },
        onNextClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyToolbarPreviewLoading() {
    val navController = rememberNavController()

    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = false,
            currentQuestionIndex = 0,
            isLoading = true,
        )

    SurveyToolbar(
        state = state,
        navController = navController,
        onPrevClicked = { },
        onNextClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyToolbarPreviewError() {
    val navController = rememberNavController()

    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = false,
            currentQuestionIndex = 0,
            isError = true,
        )


    SurveyToolbar(
        state = state,
        navController = navController,
        onPrevClicked = { },
        onNextClicked = { }
    )
}