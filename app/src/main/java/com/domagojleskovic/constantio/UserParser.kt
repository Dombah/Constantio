package com.domagojleskovic.constantio

import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class UserParser {
    private lateinit var database: DatabaseReference

    fun createUserProfile(userID: String, email : String) : Profile{
        database = Firebase.database.reference
        val profile = Profile(userID,email)
        database.child("users").child(userID).setValue(profile)
        return profile
    }
}