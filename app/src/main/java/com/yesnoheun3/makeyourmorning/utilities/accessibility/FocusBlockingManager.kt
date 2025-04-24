package com.yesnoheun3.makeyourmorning.utilities.accessibility

import com.yesnoheun3.makeyourmorning.common.data.BlockType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FocusBlockingManager {
    private val _isBlocking = MutableStateFlow(false)
    val isBlocking: StateFlow<Boolean> get() = _isBlocking

    private val _blockType = MutableStateFlow(BlockType.NIGHT)
    val getBlockType: BlockType get() = _blockType.value
    val isNight: StateFlow<BlockType> get() = _blockType

    var blockingEndTime: Long = 0L

    fun startBlockingFor(durationMillis: Long, blockType: BlockType) {
        blockingEndTime = System.currentTimeMillis() + durationMillis
        _isBlocking.value = true
        _blockType.value = blockType
    }

    fun stopBlocking() {
        blockingEndTime = 0L
        _isBlocking.value = false
    }

    fun checkBlocking() {
        val now = System.currentTimeMillis()
        _isBlocking.value = blockingEndTime > now
    }

    fun setBlockTypeMorning(){
        _blockType.value = BlockType.MORNING
    }

    fun setBlockTypeNight(){
        _blockType.value = BlockType.NIGHT
    }
}