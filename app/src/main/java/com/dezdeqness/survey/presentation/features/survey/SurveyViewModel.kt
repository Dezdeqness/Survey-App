package com.dezdeqness.survey.presentation.features.survey

import com.dezdeqness.survey.core.BaseViewModel
import com.dezdeqness.survey.core.CoroutineDispatcherProvider
import com.dezdeqness.survey.core.TimeProvider
import com.dezdeqness.survey.domain.repository.QuestionRepository
import com.dezdeqness.survey.presentation.features.survey.mapper.QuestionUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val questionUiMapper: QuestionUiMapper,
    private val timeProvider: TimeProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : BaseViewModel(coroutineDispatcherProvider = coroutineDispatcherProvider) {

    private val _surveyState: MutableStateFlow<SurveyState> = MutableStateFlow(SurveyState())
    val surveyState: StateFlow<SurveyState> = _surveyState

    private val listOfSubmittedIds = mutableListOf<Int>()

    private var timeoutJob: Job? = null

    init {
        fetchQuestions()
    }

    override fun onCleared() {
        super.onCleared()
        timeoutJob?.cancel()
    }

    fun dispatch(surveyAction: SurveyAction) {
        when(surveyAction) {
            SurveyAction.OnRetryClicked -> {
                onRetryClicked()
            }
            SurveyAction.OnNextClicked -> {
                onNextButtonClicked()
            }
            SurveyAction.OnPrevClicked -> {
                onPrevButtonClicked()
            }
            is SurveyAction.OnAnswerChanged -> {
                onAnswerTextChanged(surveyAction.text)
            }
            SurveyAction.OnNotificationShown -> {
                setupAnswerStatusTimeout()
            }
            SurveyAction.OnSubmitAnswerCLicked -> {
                onSubmittedButtonClicked()
            }
        }
    }

    private fun onNextButtonClicked() {
        val currentQuestionIndex = _surveyState.value.currentQuestionIndex
        val questionsLength = _surveyState.value.questions.size

        if (currentQuestionIndex >= questionsLength) return

        val newQuestionIndex = currentQuestionIndex + 1

        _surveyState.update {
            _surveyState.value.copy(
                currentQuestionIndex = newQuestionIndex,
                isQuestionSubmitted = isQuestionSubmitted(newQuestionIndex),
                isPrevActionEnabled = newQuestionIndex != 0,
                isNextActionEnabled = newQuestionIndex < questionsLength - 1,
                currentAnswerStatus = AnswerStatus.None,
            )
        }
    }

    private fun onPrevButtonClicked() {
        val currentQuestionIndex = _surveyState.value.currentQuestionIndex

        if (currentQuestionIndex == 0) return

        val newQuestionIndex = currentQuestionIndex - 1

        val questionsLength = _surveyState.value.questions.size

        _surveyState.update {
            _surveyState.value.copy(
                currentQuestionIndex = newQuestionIndex,
                isQuestionSubmitted = isQuestionSubmitted(newQuestionIndex),
                isPrevActionEnabled = newQuestionIndex != 0,
                isNextActionEnabled = newQuestionIndex < questionsLength - 1,
                currentAnswerStatus = AnswerStatus.None,
            )
        }
    }

    private fun onSubmittedButtonClicked() {
        _surveyState.update {
            _surveyState.value.copy(currentAnswerStatus = AnswerStatus.None)
        }
        val currentIndex = _surveyState.value.currentQuestionIndex

        val currentQuestion = _surveyState.value.questions[currentIndex]

        launchOnIo {
            questionRepository
                .submitAnswer(
                    id = currentQuestion.id,
                    answer = currentQuestion.answer
                )
                .onSuccess { status ->

                    val answerStatus = if (status == SUCCESS_CODE) {
                        listOfSubmittedIds.add(currentQuestion.id)
                        AnswerStatus.Success
                    } else {
                        AnswerStatus.Failure
                    }

                    val submittedCounter = _surveyState.value.submittedQuestionCounter

                    _surveyState.update {
                        _surveyState.value.copy(
                            submittedQuestionCounter = submittedCounter + 1,
                            currentAnswerStatus = answerStatus,
                        )
                    }
                }
                .onFailure {
                    _surveyState.update {
                        _surveyState.value.copy(
                            currentAnswerStatus = AnswerStatus.Failure,
                        )
                    }
                }
        }
    }

    private fun onAnswerTextChanged(value: String) {
        val currentIndex = _surveyState.value.currentQuestionIndex

        val questions = _surveyState.value.questions.mapIndexed { index, questionUiModel ->
            if (currentIndex == index) {
                questionUiModel.copy(answer = value)
            } else {
                questionUiModel
            }
        }

        _surveyState.update {
            _surveyState.value.copy(
                questions = questions,
            )
        }
    }

    private fun onRetryClicked() {
        fetchQuestions()
    }

    private fun setupAnswerStatusTimeout() {
        timeoutJob?.cancel()

        timeoutJob = launchOnIo {

            delay(timeProvider.getTimeoutMills())
            if (_surveyState.value.currentAnswerStatus == AnswerStatus.None) return@launchOnIo

            val currentIndex = _surveyState.value.currentQuestionIndex

            _surveyState.update {
                _surveyState.value.copy(
                    currentAnswerStatus = AnswerStatus.None,
                    isQuestionSubmitted = isQuestionSubmitted(currentIndex),
                )
            }
        }
    }

    private fun isQuestionSubmitted(index: Int): Boolean {
        val currentQuestionId = _surveyState.value.questions[index].id

        return listOfSubmittedIds.contains(currentQuestionId)
    }

    private fun fetchQuestions() {
        _surveyState.update {
            SurveyState(isLoading = true)
        }

        launchOnIo {
            questionRepository
                .getQuestions()
                .onSuccess { list ->
                    val uiQuestions = questionUiMapper.mapListEntities(list)

                    _surveyState.update {
                        _surveyState.value.copy(
                            questions = uiQuestions,
                            currentQuestionIndex = INITIAL_QUESTION_INDEX,
                            isLoading = false,
                            isPrevActionEnabled = false,
                            isNextActionEnabled = uiQuestions.isNotEmpty() && uiQuestions.size > 1,
                        )
                    }
                }
                .onFailure {
                    _surveyState.update {
                        _surveyState.value.copy(
                            isError = true,
                            isLoading = false,
                            isPrevActionEnabled = false,
                            isNextActionEnabled = false,
                        )
                    }
                }
        }
    }

    companion object {
        private const val INITIAL_QUESTION_INDEX = 0
        private const val SUCCESS_CODE = 200
    }

}
