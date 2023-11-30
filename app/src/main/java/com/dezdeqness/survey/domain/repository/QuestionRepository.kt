package com.dezdeqness.survey.domain.repository

import com.dezdeqness.survey.domain.model.QuestionEntity

interface QuestionRepository {

    fun getQuestions(): Result<List<QuestionEntity>>

    fun submitAnswer(id: Int, answer: String): Result<Int>

}
