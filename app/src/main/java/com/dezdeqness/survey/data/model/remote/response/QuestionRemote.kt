package com.dezdeqness.survey.data.model.remote.response

import com.squareup.moshi.Json

data class QuestionRemote(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "question") val question: String?,
)
