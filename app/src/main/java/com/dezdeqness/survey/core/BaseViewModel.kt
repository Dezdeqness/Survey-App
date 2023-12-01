package com.dezdeqness.survey.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.survey.core.event.BaseEvent
import com.dezdeqness.survey.core.event.EventConsumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel(), CoroutineScope, EventConsumer<BaseEvent> {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    override fun onEventConsumed(event: BaseEvent) {}

    protected fun launchOnIo(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.io()) {
            lambda.invoke()
        }

    protected fun launchOnMain(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.main()) {
            lambda.invoke()
        }

}
