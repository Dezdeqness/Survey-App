package com.dezdeqness.survey.presentation.features.survey

import com.dezdeqness.survey.core.BaseViewModel
import com.dezdeqness.survey.core.CoroutineDispatcherProvider
import com.dezdeqness.survey.domain.repository.QuestionRepository
import com.dezdeqness.survey.presentation.features.survey.mapper.QuestionUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val questionUiMapper: QuestionUiMapper,
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

    fun onNextButtonClicked() {
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

    fun onPrevButtonClicked() {
        val currentQuestionIndex = _surveyState.value.currentQuestionIndex

        if (currentQuestionIndex == 0) return

        val newQuestionIndex = currentQuestionIndex - 1;

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

    fun onSubmittedButtonClicked() {
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

    fun onAnswerTextChanged(value: String) {
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

    fun onRetryClicked() {
        fetchQuestions()
    }

    fun setupAnswerStatusTimeout() {
        timeoutJob?.cancel()

        timeoutJob = launchOnIo {

            delay(TIMEOUT_MILLIS)
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
                    val uiQuestions = list.map(questionUiMapper::fromEntity)

                    _surveyState.update {
                        _surveyState.value.copy(
                            questions = uiQuestions,
                            currentQuestionIndex = INITIAL_QUESTION_INDEX,
                            isLoading = false,
                            isPrevActionEnabled = false,
                            isNextActionEnabled = true,
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
        private const val TIMEOUT_MILLIS = 3000L
    }

}
