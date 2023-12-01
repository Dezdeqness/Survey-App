package com.dezdeqness.survey.presentation.features.survey.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dezdeqness.survey.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(id = R.string.survey_empty_list))
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyContentPreview() {
    EmptyContent(
        modifier = Modifier.fillMaxSize()
    )
}
