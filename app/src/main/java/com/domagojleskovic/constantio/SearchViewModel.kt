package com.domagojleskovic.constantio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    val emailPasswordManager: EmailPasswordManager
) : ViewModel() {
    private val _searchResults = MutableLiveData<List<Profile>>()
    val searchResults: LiveData<List<Profile>> = _searchResults
    private var searchJob: Job? = null
    private val queryRef = FirebaseDatabase.getInstance().getReference("users")

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun searchProfiles(query: String) {
        searchJob?.cancel()
        clearSearchResults()
        searchJob = viewModelScope.launch {
            delay(100)
            if (query.isNotEmpty()) {
                val filteredQuery = queryRef
                    .orderByChild("name")
                    .startAt(query)
                    .endAt("$query\uf8ff")

                filteredQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        viewModelScope.launch {
                            val profiles = snapshot.children.mapNotNull { it.getValue(Profile::class.java) }
                            val updatedProfiles = profiles.map {
                                async { emailPasswordManager.parseUserToProfile(it.userId) }
                            }.awaitAll().filterNotNull().filter {
                                it.userId != emailPasswordManager.getCurrentUser()?.uid
                            }
                            _searchResults.value = updatedProfiles
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.w("FirebaseSearch", "Failed to read value.", error.toException())
                    }
                })
            }
        }
    }
}