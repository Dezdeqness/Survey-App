package com.dezdeqness.survey.data.service

import com.dezdeqness.survey.data.model.remote.request.AnswerBody
import com.dezdeqness.survey.data.model.remote.response.QuestionRemote
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionsService {

    @GET("questions")
    fun getQuestions(): Call<List<QuestionRemote>>

    @POST("question/submit")
    fun submitAnswer(@Body value: AnswerBody): Call<ResponseBody>

}
