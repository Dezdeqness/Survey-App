package com.dezdeqness.survey.data.mapper

import com.dezdeqness.survey.data.model.remote.response.QuestionRemote
import com.dezdeqness.survey.domain.model.QuestionEntity
import javax.inject.Inject

class QuestionMapper @Inject constructor() {

    fun fromResponse(item: QuestionRemote): QuestionEntity? {
        return QuestionEntity(
            id = item.id ?: return null,
            question = item.question ?: return null,
        )
    }

}
