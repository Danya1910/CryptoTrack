package com.example.cryptotrack.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.UserData
import com.example.cryptotrack.domain.usecase.GetDataUseCase
import com.example.cryptotrack.domain.usecase.InsertAvatarUseCase
import com.example.cryptotrack.domain.usecase.InsertNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val insertNameUseCase: InsertNameUseCase,
    private val insertAvatarUseCase: InsertAvatarUseCase,
    private val getDataUseCase: GetDataUseCase,
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    init {
        viewModelScope.launch {
            getDataUseCase()
        }
    }

    fun insertName(
        name: String
    ) {
        viewModelScope.launch {
            insertNameUseCase(name = name)
            _userData.value = getDataUseCase()
        }
    }


    fun insertAvatar(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {

            val file = File(context.filesDir, "avatar.jpg")

            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            insertAvatarUseCase(file.absolutePath)

            val updated = getDataUseCase()

            withContext(Dispatchers.Main) {
                _userData.value = updated
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            _userData.value = getDataUseCase()
        }
    }

}