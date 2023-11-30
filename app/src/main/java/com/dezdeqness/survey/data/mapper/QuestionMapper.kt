package com.dezdeqness.survey.data.mapper

import com.dezdeqness.survey.data.model.remote.response.QuestionRemote
import com.dezdeqness.survey.domain.model.QuestionEntity
import javax.inject.Inject

class QuestionMapper @Inject constructor() {

    fun fromResponse(item: QuestionRemote) =
        QuestionEntity(
            id = item.id,
            question = item.question,
        )

}
