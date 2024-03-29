package com.domagojleskovic.constantio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
    private val emailPasswordManager: EmailPasswordManager,
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _signInStatus = MutableLiveData(false)
    val signInStatus: LiveData<Boolean> = _signInStatus

    fun clearSignInStatus() {
        _signInStatus.value = false
    }
    fun signIn(email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            val success = emailPasswordManager.signIn(email, password)
            _signInStatus.value = success
            _isLoading.value = false
        }
    }
}