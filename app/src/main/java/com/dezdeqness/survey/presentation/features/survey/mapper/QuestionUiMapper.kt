package com.dezdeqness.survey.presentation.features.survey.mapper

import com.dezdeqness.survey.domain.model.QuestionEntity
import com.dezdeqness.survey.presentation.features.survey.model.QuestionUiModel
import javax.inject.Inject

class QuestionUiMapper @Inject constructor() {

    fun fromEntity(value: QuestionEntity) =
        QuestionUiModel(
            id = value.id,
            question = value.question,
        )
}
