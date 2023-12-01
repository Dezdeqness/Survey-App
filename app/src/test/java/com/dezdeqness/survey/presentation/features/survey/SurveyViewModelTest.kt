package com.dezdeqness.survey.presentation.features.survey

import com.dezdeqness.survey.core.TimeProvider
import com.dezdeqness.survey.domain.model.QuestionEntity
import com.dezdeqness.survey.domain.repository.QuestionRepository
import com.dezdeqness.survey.presentation.features.survey.mapper.QuestionUiMapper
import com.dezdeqness.survey.presentation.features.survey.model.QuestionUiModel
import com.dezdeqness.survey.utils.TestCoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SurveyViewModelTest {

    @Mock
    private lateinit var questionRepository: QuestionRepository

    @Mock
    private lateinit var questionUiMapper: QuestionUiMapper

    @Mock
    private lateinit var timeProvider: TimeProvider

    @Before
    fun setUp() {
        `when`(timeProvider.getTimeoutMills()).thenReturn(0)

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN fetch of questions is success SHOULD show emit data`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf<QuestionUiModel>(mock(), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

        val viewModel = createViewModel()

        val uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

    }

    @Test
    fun `WHEN fetch of questions is failure SHOULD show emit error state`() = runTest {
        `when`(questionRepository.getQuestions()).thenReturn(Result.failure(Throwable()))

        val viewModel = createViewModel()

        val uiState = viewModel.surveyState.value

        assertAll(
            { assertTrue(uiState.isError) },
            { assertFalse(uiState.isLoading) },
            { assertFalse(uiState.isPrevActionEnabled) },
            { assertFalse(uiState.isNextActionEnabled) },
            { assertTrue(uiState.questions.isEmpty()) },
        )

    }

    @Test
    fun `WHEN fetch of questions is success, but no items SHOULD show emit empty state`() =
        runTest {

            `when`(questionRepository.getQuestions()).thenReturn(Result.success(listOf()))

            val viewModel = createViewModel()

            val uiState = viewModel.surveyState.value

            assertAll(
                { assertFalse(uiState.isError) },
                { assertFalse(uiState.isLoading) },
                { assertFalse(uiState.isPrevActionEnabled) },
                { assertFalse(uiState.isNextActionEnabled) },
                { assertTrue(uiState.questions.isEmpty()) },
            )
        }

    @Test
    fun `WHEN fetch of questions is failure, but retry is success SHOULD show emit data`() =
        runTest {
            val entities = listOf<QuestionEntity>(mock(), mock())
            val uiItems = listOf<QuestionUiModel>(mock(), mock())

            `when`(questionRepository.getQuestions())
                .thenReturn(Result.failure(Throwable()))
                .thenReturn(Result.success(entities))
            `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

            val viewModel = createViewModel()

            var uiState = viewModel.surveyState.value

            assertAll(
                { assertTrue(uiState.isError) },
                { assertFalse(uiState.isLoading) },
                { assertFalse(uiState.isPrevActionEnabled) },
                { assertFalse(uiState.isNextActionEnabled) },
                { assertTrue(uiState.questions.isEmpty()) },
            )

            viewModel.dispatch(SurveyAction.OnRetryClicked)

            uiState = viewModel.surveyState.value

            assertAll(
                { assertFalse(uiState.isError) },
                { assertFalse(uiState.isLoading) },
                { assertFalse(uiState.isPrevActionEnabled) },
                { assertTrue(uiState.isNextActionEnabled) },
                { assertTrue(uiState.currentQuestionIndex == 0) },
                { assertTrue(uiState.questions.size == 2) },
            )

        }

    @Test
    fun `WHEN on next clicked is success SHOULD show open second question from 3`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock(), mock())
        val uiItems = listOf<QuestionUiModel>(mock(), mock(), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 3)
            }
        )

        viewModel.dispatch(SurveyAction.OnNextClicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 1)
            }
        )
    }

    @Test
    fun `WHEN on next clicked is success SHOULD show open last question`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf<QuestionUiModel>(mock(), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

        viewModel.dispatch(SurveyAction.OnNextClicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.isPrevActionEnabled)
                assertFalse(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 1)
            }
        )
    }

    @Test
    fun `WHEN on prev clicked is success SHOULD show open first question`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf<QuestionUiModel>(mock(), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

        viewModel.dispatch(SurveyAction.OnNextClicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.isPrevActionEnabled)
                assertFalse(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 1)
            }
        )

        viewModel.dispatch(SurveyAction.OnPrevClicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
            }
        )
    }

    @Test
    fun `WHEN user enter enter is success SHOULD show update UI state`() = runTest {
        val question = QuestionUiModel(
            id = 1,
            question = "Question1"
        )

        val enteredAnswer = "Answer"

        val entities = listOf<QuestionEntity>(mock())
        val uiItems = listOf(question)

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertFalse(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 1)
            }
        )

        viewModel.dispatch(SurveyAction.OnAnswerChanged(enteredAnswer))

        uiState = viewModel.surveyState.value

        assertAll({
            assertEquals(
                listOf(
                    question.copy(answer = enteredAnswer)
                ),
                uiState.questions,
            )
        })

    }

    @Test
    fun `WHEN user clicked on submit code is 200 SHOULD show Success notification`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf(QuestionUiModel(id = 1, question = ""), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)
        `when`(questionRepository.submitAnswer(id = 1, answer = "")).thenReturn(Result.success(200))

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

        viewModel.dispatch(SurveyAction.OnSubmitAnswerCLicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.currentAnswerStatus == AnswerStatus.Success)
            }
        )

    }

    @Test
    fun `WHEN user clicked on submit code is 400 SHOULD show Failure notification`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf(QuestionUiModel(id = 1, question = ""), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)
        `when`(questionRepository.submitAnswer(id = 1, answer = "")).thenReturn(Result.success(400))

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

        viewModel.dispatch(SurveyAction.OnSubmitAnswerCLicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.currentAnswerStatus == AnswerStatus.Failure)
            }
        )

    }

    @Test
    fun `WHEN user clicked on submit is failure SHOULD show Failure notification`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf(QuestionUiModel(id = 1, question = ""), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)
        `when`(questionRepository.submitAnswer(id = 1, answer = "")).thenReturn(
            Result.failure(
                Throwable()
            )
        )

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            {
                assertFalse(uiState.isError)
                assertFalse(uiState.isLoading)
                assertFalse(uiState.isPrevActionEnabled)
                assertTrue(uiState.isNextActionEnabled)
                assertTrue(uiState.currentQuestionIndex == 0)
                assertTrue(uiState.questions.size == 2)
            }
        )

        viewModel.dispatch(SurveyAction.OnSubmitAnswerCLicked)

        uiState = viewModel.surveyState.value

        assertAll(
            {
                assertTrue(uiState.currentAnswerStatus == AnswerStatus.Failure)
            }
        )

    }

    @Test
    fun `WHEN success notification is dismissed SHOULD show refresh submit state`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf(QuestionUiModel(id = 1, question = ""), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)
        `when`(questionRepository.submitAnswer(id = 1, answer = "")).thenReturn(Result.success(200))

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            { assertFalse(uiState.isError) },
            { assertFalse(uiState.isLoading) },
            { assertFalse(uiState.isPrevActionEnabled) },
            { assertTrue(uiState.isNextActionEnabled) },
            { assertTrue(uiState.currentQuestionIndex == 0) },
            { assertTrue(uiState.questions.size == 2) },
        )

        viewModel.dispatch(SurveyAction.OnSubmitAnswerCLicked)

        uiState = viewModel.surveyState.value

        assertAll({ assertTrue(uiState.currentAnswerStatus == AnswerStatus.Success) })

        viewModel.dispatch(SurveyAction.OnNotificationShown)

        uiState = viewModel.surveyState.value


        assertAll(
            { assertFalse(uiState.isError) },
            { assertFalse(uiState.isLoading) },
            { assertFalse(uiState.isPrevActionEnabled) },
            { assertTrue(uiState.isNextActionEnabled) },
            { assertTrue(uiState.currentQuestionIndex == 0) },
            { assertTrue(uiState.questions.size == 2) },
            { assertTrue(uiState.currentAnswerStatus == AnswerStatus.None) },
            { assertTrue(uiState.submittedQuestionCounter == 1) },
            { assertTrue(uiState.isQuestionSubmitted) },
        )

    }

    @Test
    fun `WHEN failure notification is dismissed SHOULD show refresh submit state`() = runTest {
        val entities = listOf<QuestionEntity>(mock(), mock())
        val uiItems = listOf(QuestionUiModel(id = 1, question = ""), mock())

        `when`(questionRepository.getQuestions()).thenReturn(Result.success(entities))
        `when`(questionUiMapper.mapListEntities(entities)).thenReturn(uiItems)
        `when`(questionRepository.submitAnswer(id = 1, answer = "")).thenReturn(Result.success(400))

        val viewModel = createViewModel()

        var uiState = viewModel.surveyState.value

        assertAll(
            { assertFalse(uiState.isError) },
            { assertFalse(uiState.isLoading) },
            { assertFalse(uiState.isPrevActionEnabled) },
            { assertTrue(uiState.isNextActionEnabled) },
            { assertTrue(uiState.currentQuestionIndex == 0) },
            { assertTrue(uiState.questions.size == 2) },
        )

        viewModel.dispatch(SurveyAction.OnSubmitAnswerCLicked)

        uiState = viewModel.surveyState.value

        assertAll({ assertTrue(uiState.currentAnswerStatus == AnswerStatus.Failure) })

        viewModel.dispatch(SurveyAction.OnNotificationShown)

        uiState = viewModel.surveyState.value


        assertAll(
            { assertFalse(uiState.isError) },
            { assertFalse(uiState.isLoading) },
            { assertFalse(uiState.isPrevActionEnabled) },
            { assertTrue(uiState.isNextActionEnabled) },
            { assertTrue(uiState.currentQuestionIndex == 0) },
            { assertTrue(uiState.questions.size == 2) },
            { assertTrue(uiState.currentAnswerStatus == AnswerStatus.None) },
            { assertTrue(uiState.submittedQuestionCounter == 1) },
            { assertFalse(uiState.isQuestionSubmitted) },
        )

    }

    private fun createViewModel() =
        SurveyViewModel(
            questionRepository = questionRepository,
            questionUiMapper = questionUiMapper,
            timeProvider = timeProvider,
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
        )

}
