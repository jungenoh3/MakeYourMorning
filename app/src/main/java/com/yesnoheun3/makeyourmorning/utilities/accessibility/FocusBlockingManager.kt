package com.yesnoheun3.makeyourmorning.utilities.accessibility

import android.app.Application
import com.yesnoheun3.makeyourmorning.common.data.BlockType
import com.yesnoheun3.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object FocusBlockingManager {
    private val _isBlocking = MutableStateFlow(false)
    val isBlocking: StateFlow<Boolean> get() = _isBlocking

    private val _blockType = MutableStateFlow(BlockType.NIGHT)
    val getBlockType: BlockType get() = _blockType.value
    val blockType: StateFlow<BlockType> get() = _blockType

    var blockingEndTime: Long = 0L

    val allowedAppList = mutableSetOf<String>(
        "com.yesnoheun3.makeyourmorning",
        "com.google.android.apps.nexuslauncher",
        "com.google.android.googlequicksearchbox",
        "com.android.systemui",
    )
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun startBlockingFor(durationMillis: Long, blockType: BlockType, application: Application) {
        blockingEndTime = System.currentTimeMillis() + durationMillis
        _isBlocking.value = true
        _blockType.value = blockType

        scope.launch {
            allowedAppList.addAll(AppRepository(application).getAllInstalledApp().first())
        }
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