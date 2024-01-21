package com.domagojleskovic.constantio

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.database

class EmailPasswordManager(
    private val context : Context,
    private val navController: NavController
) {

    private var auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    fun getCurrentUser() : FirebaseUser?{
        return auth.currentUser
    }
    fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser!!
                    writeNewUser(user.uid, email, password)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }
    private fun writeNewUser(userId: String, name: String, email: String) {
        val user = Profile(userId, name, email)
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("FirebaseWrite", "User data written successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseWrite", "Error writing user data", e)
            }
    }
    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //val userObject = Profile(user, email, null, null, null)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?)
    {
        if(user != null)
        {
            navController.navigate("main_screen")
        }
        else{
            Toast.makeText(context, "User with given credentials not found", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun reload()
    {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}