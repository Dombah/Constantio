package com.domagojleskovic.constantio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val emailPasswordManager: EmailPasswordManager,
) : ViewModel(){

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerStatus = MutableLiveData(false)
    val registerStatus: LiveData<Boolean> = _registerStatus

    fun clearRegisterStatus() {
        _registerStatus.value = false
    }
    fun register(email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            val success = emailPasswordManager.createAccount(email, password)
            _registerStatus.value = success
            _isLoading.value = false
        }
    }
}
