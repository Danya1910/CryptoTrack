package com.example.cryptotrack.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.UserData
import com.example.cryptotrack.domain.usecase.GetDataUseCase
import com.example.cryptotrack.domain.usecase.InsertAvatarUseCase
import com.example.cryptotrack.domain.usecase.InsertNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val insertNameUseCase: InsertNameUseCase,
    private val insertAvatarUseCase: InsertAvatarUseCase,
    private val getDataUseCase: GetDataUseCase,
) : ViewModel() {

    private val _userData = MutableStateFlow(UserData("", null))
    val userData = _userData.asStateFlow()

    init {

        viewModelScope.launch {
            _userData.value = getDataUseCase()
            _userData.collect { list ->
                Log.d("UserVM", "user = $list")
            }

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

    fun insertAvatar(
        avatar: String
    ) {
        viewModelScope.launch {
            insertAvatarUseCase(avatar = avatar)
        }
    }

    fun getData() {
        viewModelScope.launch {
            _userData.value = getDataUseCase()
        }
    }

}