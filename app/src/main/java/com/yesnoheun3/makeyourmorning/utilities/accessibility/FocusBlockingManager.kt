package com.yesnoheun3.makeyourmorning.utilities.accessibility

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FocusBlockingManager {
    private val _isBlocking = MutableStateFlow(false)
    val isBlocking: StateFlow<Boolean> get() = _isBlocking

    var blockingEndTime: Long = 0L
        private set

    fun startBlockingFor(durationMillis: Long) {
        blockingEndTime = System.currentTimeMillis() + durationMillis
        _isBlocking.value = true
    }

    fun stopBlocking() {
        blockingEndTime = 0L
        _isBlocking.value = false
    }

    fun checkBlocking() {
        val now = System.currentTimeMillis()
        _isBlocking.value = blockingEndTime > now
    }
}