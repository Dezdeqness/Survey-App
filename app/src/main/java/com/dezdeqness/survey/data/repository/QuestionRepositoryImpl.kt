package com.dezdeqness.survey.data.repository

import com.dezdeqness.survey.data.datasource.QuestionsRemoteDatasource
import com.dezdeqness.survey.data.mapper.QuestionMapper
import com.dezdeqness.survey.data.model.remote.request.AnswerBody
import com.dezdeqness.survey.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val questionsRemoteDatasource: QuestionsRemoteDatasource,
    private val questionMapper: QuestionMapper,
) : QuestionRepository {

    override fun getQuestions() =
        questionsRemoteDatasource
            .getQuestions()
            .mapCatching { list ->
                list.map(questionMapper::fromResponse)
            }

    override fun submitAnswer(id: Int, answer: String) =
        questionsRemoteDatasource.submitAnswer(
            AnswerBody(
                id = id,
                answer = answer,
            ),
        )
}
