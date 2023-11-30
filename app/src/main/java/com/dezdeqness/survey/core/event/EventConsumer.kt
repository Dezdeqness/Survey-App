package com.dezdeqness.survey.core.event

interface EventConsumer<T> {

    fun onEventConsumed(event: T)

}
