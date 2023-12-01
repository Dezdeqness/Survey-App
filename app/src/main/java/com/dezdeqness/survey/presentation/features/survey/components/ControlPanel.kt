package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.survey.R
import com.dezdeqness.survey.presentation.features.survey.SurveyState
import com.dezdeqness.survey.presentation.features.survey.model.QuestionUiModel

@Composable
fun ControlPanel(
    state: SurveyState,
    modifier: Modifier = Modifier,
    onAnswerTextChanger: (String) -> Unit,
    onSubmittedButtonClicked: () -> Unit,
) {
    val currentQuestion =
        remember(state.currentQuestionIndex, state.questions) {
            state.questions[state.currentQuestionIndex]
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        TextField(
            value = currentQuestion.answer,
            onValueChange = { text ->
                onAnswerTextChanger(text)
            },
            placeholder = {
                Text(stringResource(id = R.string.survey_input_hint))
            },
            modifier = Modifier.padding(top = 20.dp)
        )

        val isButtonEnabled =
            state.isQuestionSubmitted.not() && currentQuestion.answer.isNotEmpty()

        Button(
            enabled = isButtonEnabled,
            onClick = {
                onSubmittedButtonClicked()
            },
            modifier = Modifier.padding(top = 36.dp)
        ) {
            val resId = if (state.isQuestionSubmitted.not()) {
                R.string.survey_submit_label
            } else {
                R.string.survey_already_submitted_label
            }

            Text(stringResource(id = resId))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ControlPanelPreviewDefault() {
    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = true,
            currentQuestionIndex = 0,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                ),
            ),
        )


    ControlPanel(
        state = state,
        onSubmittedButtonClicked = {},
        onAnswerTextChanger = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ControlPanelPreviewWithAnswer() {
    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = true,
            currentQuestionIndex = 0,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                    answer = "Answer1"
                ),
            ),
        )


    ControlPanel(
        state = state,
        onSubmittedButtonClicked = {},
        onAnswerTextChanger = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ControlPanelPreviewWithAnswerSubmitted() {
    val state =
        SurveyState(
            isNextActionEnabled = false,
            isPrevActionEnabled = true,
            currentQuestionIndex = 0,
            questions = listOf(
                QuestionUiModel(
                    id = 1,
                    question = "Question1",
                    answer = "Answer1"
                ),
            ),
            isQuestionSubmitted = true
        )

    ControlPanel(
        state = state,
        onSubmittedButtonClicked = {},
        onAnswerTextChanger = {}
    )
}
