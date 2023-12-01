package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.survey.R
import com.dezdeqness.survey.presentation.features.survey.AnswerStatus
import com.dezdeqness.survey.presentation.features.survey.SurveyState

@Composable
fun NotificationSurface(
    state: SurveyState,
    onRetryButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x8A888888))
            .clickable(enabled = false) {},
    ) {

        val isSuccessFlow =
            state.currentAnswerStatus == AnswerStatus.Success

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSuccessFlow) {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
                .requiredHeightIn(min = 90.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val resId = if (isSuccessFlow) {
                R.string.survey_notification_success_label
            } else {
                R.string.survey_notification_error_label
            }

            Text(stringResource(id = resId))

            if (isSuccessFlow.not()) {
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        onRetryButtonClicked()
                    },
                ) {
                    Text(stringResource(id = R.string.survey_notification_retry_label))
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationSurfaceSuccess() {
    val state = SurveyState(currentAnswerStatus = AnswerStatus.Success)

    NotificationSurface(state) {

    }
}

@Preview
@Composable
fun NotificationSurfaceFailure() {
    val state = SurveyState(currentAnswerStatus = AnswerStatus.Failure)

    NotificationSurface(state) {

    }
}
