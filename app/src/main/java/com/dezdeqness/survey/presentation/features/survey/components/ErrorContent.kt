package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.survey.R

@Composable
fun ErrorContent(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            stringResource(id = R.string.survey_error_label),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = { onRetryClicked() }) {
            Text(stringResource(id = R.string.survey_error_retry_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {
    Box(modifier = Modifier.fillMaxWidth()) {
        ErrorContent(
            onRetryClicked = {

            },
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
