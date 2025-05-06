package com.nochunsam.makeyourmorning.utilities.accessibility

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nochunsam.makeyourmorning.common.data.BlockType
import com.nochunsam.makeyourmorning.pages.setting.data.AllowedApp
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
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

    val blockAppList = mutableSetOf<String>(
    )
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun startBlockingFor(durationMillis: Long, blockType: BlockType, application: Application) {
        blockingEndTime = System.currentTimeMillis() + durationMillis
        _isBlocking.value = true
        _blockType.value = blockType

        val installedApp = getInstalledApps(application.applicationContext).map{ it.packageName }
        scope.launch {
            val allowedAppList = AppRepository(application).getAllAllowedApp().first()

            println("Installed Apps: $installedApp")
            println("Allowed Apps: $allowedAppList")

            val toBlock = installedApp.filter { it !in allowedAppList.toSet() }
            println("Blocking Apps: $toBlock")

            blockAppList.clear()
            blockAppList.addAll(toBlock)
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

    fun getInstalledApps(context: Context): List<AllowedApp> {
        val pm = context.packageManager

        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos = pm.queryIntentActivities(intent, 0)

        return resolveInfos
            .filter { it.activityInfo.packageName != "com.nochunsam.makeyourmorning" &&
                it.activityInfo.packageName != "com.google.android.googlequicksearchbox"
            }
            .map {
                val appName = it.loadLabel(pm).toString()
                val packageName = it.activityInfo.packageName

                AllowedApp(
                    appName = appName,
                    packageName = packageName
                )
            }.sortedBy { it.appName.lowercase() }
    }
}