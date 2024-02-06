package com.domagojleskovic.constantio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val emailPasswordManager: EmailPasswordManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(emailPasswordManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}