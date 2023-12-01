package com.dezdeqness.survey.utils

import com.dezdeqness.survey.core.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Unconfined
    override fun io() = Dispatchers.Unconfined
    override fun computation() = Dispatchers.Unconfined
}
