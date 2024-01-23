package com.domagojleskovic.constantio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class EmailPasswordManager(
    private val context: Context,
    private val navController: NavController,
) {

    private var auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var storage : StorageReference

    fun getCurrentUser() : FirebaseUser?{
        return auth.currentUser
    }
    fun getDBO() : DatabaseReference{
        return database
    }
    fun createAccount(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser!!
                    writeNewUser(user.uid, email.removeRange(email.indexOf('@'), email.length), email, onSuccess)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    private fun writeNewUser(userId: String, name: String, email: String, onSuccess: () -> Unit) {
        val user = Profile(userId = userId, name = name, email = email)
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("FirebaseWrite", "User data written successfully")
                val imageUri = Uri.parse("android.resource://${context.packageName}/${R.drawable.logo}")
                storage = FirebaseStorage.getInstance().getReference("UserProfilePictures/"+auth.currentUser?.uid)
                storage.putFile(imageUri).addOnSuccessListener {
                    Toast.makeText(context, "Successfully created user", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }.addOnFailureListener{
                    Toast.makeText(context, "Failed created user", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseWrite", "Error writing user data", e)
            }
    }

    fun getCurrentUserImageUri(callback: (Uri?) -> Unit) {
        val user = auth.currentUser
        storage = FirebaseStorage.getInstance().getReference("UserProfilePictures/${user?.uid}")
        if(user != null) {
            val localFile = File.createTempFile("images", ".png")
            storage.getFile(localFile).addOnSuccessListener {
                callback(Uri.fromFile(localFile))
            }.addOnFailureListener {
                callback(null)
            }
        }
        else{
            callback(null)
        }
    }
    fun observeUserProfile(callback: (Profile?) -> Unit) {
        val userId = getCurrentUser()?.uid
        val userListener = getDBO()
            .child("users")
            .child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue<Profile>()
                    userProfile?.let {
                        getCurrentUserImageUri { uri ->
                            if (uri != null) {
                                val updatedProfile = userProfile.copy(icon = uri)
                                callback(updatedProfile)
                            } else {
                                callback(null)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error getting data",)
                    callback(null)
                }
            })
    }
    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser!!
                    onSuccess()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
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

    private fun reload()
    {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}