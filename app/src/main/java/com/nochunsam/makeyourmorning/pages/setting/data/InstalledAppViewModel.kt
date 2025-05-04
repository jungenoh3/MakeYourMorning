package com.nochunsam.makeyourmorning.pages.setting.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InstalledAppViewModel(application: Application): AndroidViewModel(application) {
    private val _repository = AppRepository(application)

    val items: StateFlow<List<String>> = _repository
        .getAllInstalledApp()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addItem(item: InstalledApp){
        viewModelScope.launch {
            _repository.insertInstallApp(item)
        }
    }

    fun deleteItem(item: InstalledApp){
        viewModelScope.launch {
            _repository.deleteInstallApp(item)
        }
    }
}