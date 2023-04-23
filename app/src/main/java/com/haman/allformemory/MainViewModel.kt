package com.haman.allformemory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _isExternalStoragePermissionGranted = MutableStateFlow(false)
    val isExternalStoragePermissionGranted: StateFlow<Boolean> = _isExternalStoragePermissionGranted.asStateFlow()

    private val _isNeedPermissionRationale = MutableStateFlow(false)
    val isNeedPermissionRationale: StateFlow<Boolean> = _isNeedPermissionRationale.asStateFlow()

    fun externalStoragePermissionGrant() {
        _isNeedPermissionRationale.value = false
        _isExternalStoragePermissionGranted.value = true
    }

    fun needPermissionRationale() {
        _isNeedPermissionRationale.value = true
    }
}