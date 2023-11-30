package com.dezdeqness.survey.core.event

import java.util.UUID

abstract class BaseEvent(
    open val eventId: String = UUID.randomUUID().toString()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEvent

        if (eventId != other.eventId) return false

        return true
    }

    override fun hashCode() = eventId.hashCode()
}
