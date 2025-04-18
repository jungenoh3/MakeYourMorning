package com.yesnoheun3.makeyourmorning.utilities

object FocusBlockingManager {
    var blockingEndTime: Long = 0L

    fun startBlockingFor(durationMillis: Long) {
        blockingEndTime = System.currentTimeMillis() + durationMillis
    }

    fun isBlockingActive(): Boolean {
        return System.currentTimeMillis() < blockingEndTime
    }
}