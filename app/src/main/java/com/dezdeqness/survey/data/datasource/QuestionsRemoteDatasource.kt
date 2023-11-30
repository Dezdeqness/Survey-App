package com.dezdeqness.survey.data.datasource

import com.dezdeqness.survey.data.model.remote.request.AnswerBody
import com.dezdeqness.survey.data.model.remote.response.QuestionRemote

interface QuestionsRemoteDatasource {

    fun getQuestions(): Result<List<QuestionRemote>>

    fun submitAnswer(body: AnswerBody): Result<Int>

}
