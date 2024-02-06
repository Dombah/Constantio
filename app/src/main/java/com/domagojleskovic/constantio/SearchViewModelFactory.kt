package com.domagojleskovic.constantio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(private val emailPasswordManager: EmailPasswordManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(emailPasswordManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}