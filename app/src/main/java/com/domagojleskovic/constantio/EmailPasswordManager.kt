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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
    lateinit var profile : Profile

    fun getCurrentUser() : FirebaseUser?{
        return auth.currentUser
    }
    fun getDBO() : DatabaseReference{
        return database
    }

    private fun displayAuthenticationException(exception: Exception){
        try{
            throw exception
        }catch(e : FirebaseAuthWeakPasswordException){
            Toast.makeText(context, "Password too weak", Toast.LENGTH_SHORT).show()
        }catch(e : FirebaseAuthInvalidCredentialsException){
            Toast.makeText(context, "Email address is invalid", Toast.LENGTH_SHORT).show()
        }catch(e : FirebaseAuthUserCollisionException){
            Toast.makeText(context, "User with this email already exists", Toast.LENGTH_SHORT).show()
        }
    }

    fun createAccount(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser!!
                    writeNewUser(user.uid, email.removeRange(email.indexOf('@'), email.length), email, onSuccess)
                }
            }
            .addOnFailureListener{
                exception ->
                displayAuthenticationException(exception)
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
                    profile = Profile(imageUri, name, email)
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
    private fun observeUserProfile(callback: (Profile?) -> Unit) {
        val userId = getCurrentUser()?.uid
            getDBO()
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
                                Log.e("Callback error", "Why you go here man?")
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
                    val user = auth.currentUser!!
                    observeUserProfile {
                        profile = it!!
                        onSuccess()
                    }
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, "User either doesn't exist or the password is wrong", Toast.LENGTH_SHORT).show()
            }
    }
    fun resetPassword(email : String, onSuccess: () -> Unit){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                task ->
                if(task.isSuccessful){
                    Toast.makeText(context, "Successfully send password reset request to your mail address", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, "Failed sending password reset to email address", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}