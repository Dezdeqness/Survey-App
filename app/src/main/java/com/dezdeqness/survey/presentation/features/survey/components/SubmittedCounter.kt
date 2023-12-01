package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezdeqness.survey.R


@Composable
fun SubmittedCounter(
    counter: Int,
    modifier: Modifier = Modifier,
) {
    val formattedString = stringResource(id = R.string.survey_submitted_status, counter)

    Text(
        text = formattedString,
        modifier = modifier.padding(
            vertical = 12.dp,
            horizontal = 8.dp,
        ),
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun SubmittedCounterPreview() {
    SubmittedCounter(counter = 1, modifier = Modifier.fillMaxWidth())
}
