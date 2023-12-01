package com.dezdeqness.survey.core

class TimeProvider {

    fun getTimeoutMills() = TIMEOUT_MILLIS

    companion object {
        private const val TIMEOUT_MILLIS = 3000L
    }

}