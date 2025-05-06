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
        .getAllAllowedApp()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addItem(item: AllowedApp){
        viewModelScope.launch {
            _repository.insertAllowedApp(item)
        }
    }

    fun deleteItem(item: AllowedApp){
        viewModelScope.launch {
            _repository.deleteAllowedApp(item)
        }
    }
}