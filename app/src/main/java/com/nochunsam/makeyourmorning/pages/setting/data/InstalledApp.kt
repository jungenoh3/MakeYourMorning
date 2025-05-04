package com.nochunsam.makeyourmorning.pages.setting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class InstalledApp(
    @PrimaryKey() val packageName: String,
    val appName: String
)
