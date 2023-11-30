package com.dezdeqness.survey.data.datasource

import com.dezdeqness.survey.data.model.remote.request.AnswerBody
import com.dezdeqness.survey.data.service.QuestionsService

class QuestionsRemoteDatasourceImpl(
    private val questionService: QuestionsService,
) : QuestionsRemoteDatasource {
    override fun getQuestions() = tryWithCatch {
        val response = questionService.getQuestions().execute()

        val body = response.body()

        if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override fun submitAnswer(body: AnswerBody) = tryWithCatch {
        val response = questionService.submitAnswer(body).execute()

        val code = response.code()

        if (response.isSuccessful) {
            Result.success(code)
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    private fun <T> tryWithCatch(block: () -> Result<T>) = try {
        block()
    } catch (exception: Throwable) {
        Result.failure(exception)
    }

}