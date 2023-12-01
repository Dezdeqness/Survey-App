package com.dezdeqness.survey.di.modules

import com.dezdeqness.survey.data.datasource.QuestionsRemoteDatasource
import com.dezdeqness.survey.data.datasource.QuestionsRemoteDatasourceImpl
import com.dezdeqness.survey.data.mapper.QuestionMapper
import com.dezdeqness.survey.data.repository.QuestionRepositoryImpl
import com.dezdeqness.survey.data.service.QuestionsService
import com.dezdeqness.survey.domain.repository.QuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class QuestionsRepositoryModule {

    @Provides
    fun provideQuestionsRemoteDatasource(
        questionsService: QuestionsService,
    ) : QuestionsRemoteDatasource = QuestionsRemoteDatasourceImpl(
        questionService = questionsService,
    )

    @Provides
    fun provideQuestionsRepository(
        questionsRemoteDatasource: QuestionsRemoteDatasource,
        questionMapper: QuestionMapper,
    ): QuestionRepository =
        QuestionRepositoryImpl(
            questionsRemoteDatasource = questionsRemoteDatasource,
            questionMapper = questionMapper,
        )

}
