package com.dezdeqness.survey.presentation.features.survey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dezdeqness.survey.presentation.features.survey.components.ControlPanel
import com.dezdeqness.survey.presentation.features.survey.components.EmptyContent
import com.dezdeqness.survey.presentation.features.survey.components.ErrorContent
import com.dezdeqness.survey.presentation.features.survey.components.NotificationSurface
import com.dezdeqness.survey.presentation.features.survey.components.SubmittedCounter
import com.dezdeqness.survey.presentation.features.survey.components.SurveyToolbar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SurveyPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    surveyViewModel: SurveyViewModel = hiltViewModel(),
) {

    val state = surveyViewModel.surveyState.collectAsState()

    Scaffold(
        topBar = {
            SurveyToolbar(
                state = state.value,
                navController = navController,
                onPrevClicked = {
                    surveyViewModel.onPrevButtonClicked()
                },
                onNextClicked = {
                    surveyViewModel.onNextButtonClicked()
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.value.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                state.value.isError -> {
                    ErrorContent(
                        onRetryClicked = {
                            surveyViewModel.onRetryClicked()
                        },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                state.value.questions.isEmpty() -> {
                    EmptyContent(
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val pagerState = rememberPagerState(0)

                            LaunchedEffect(state.value.currentQuestionIndex) {
                                pagerState.animateScrollToPage(state.value.currentQuestionIndex)
                            }

                            SubmittedCounter(
                                counter = state.value.submittedQuestionCounter,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            HorizontalPager(
                                userScrollEnabled = false,
                                state = pagerState,
                                pageCount = state.value.questions.size
                            ) { page ->
                                Text(text = state.value.questions[page].question)
                            }

                            ControlPanel(
                                state = state.value,
                                onAnswerTextChanger = { text ->
                                    surveyViewModel.onAnswerTextChanged(text)

                                },
                                onSubmittedButtonClicked = {
                                    surveyViewModel.onSubmittedButtonClicked()
                                }

                            )

                        }

                        val currentAnswerStatus = state.value.currentAnswerStatus

                        AnimatedVisibility(visible = currentAnswerStatus != AnswerStatus.None) {

                            LaunchedEffect(currentAnswerStatus) {
                                if (currentAnswerStatus != AnswerStatus.None) {
                                    surveyViewModel.setupAnswerStatusTimeout()
                                }
                            }

                            if (state.value.currentAnswerStatus == AnswerStatus.None) return@AnimatedVisibility
                            NotificationSurface(state = state.value) {
                                surveyViewModel.onSubmittedButtonClicked()
                            }
                        }
                    }


                }
            }
        }
    }

}
