package com.domagojleskovic.constantio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import java.io.ByteArrayOutputStream
import java.io.File

class EmailPasswordManager(
    private val context: Context,
) {

    private var auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var storage : StorageReference
    lateinit var profile : Profile

    fun getCurrentUser() : FirebaseUser?{
        return auth.currentUser
    }
    private fun getDBO() : DatabaseReference{
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
                writeUserProfilePicture(imageUri){
                    uri ->
                    if(uri != null){
                        profile = Profile(uri, userId,name,email)
                        onSuccess()
                    }else{
                        Toast.makeText(context, "Error writing user picture", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseWrite", "Error writing user data", e)
            }
    }
    fun writeUserProfilePicture(imageUri : Uri, callback : (Uri?) -> Unit){
        var bitmap : Bitmap? = null
        try{
            bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    imageUri
                )
                else -> {
                    val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(source)
                }
            }
        }catch(e : Exception){
            Log.e("WritingUserError","Error " + e.message)
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val compressedData = byteArrayOutputStream.toByteArray()
        storage = FirebaseStorage.getInstance().getReference("UserProfilePictures/"+auth.currentUser?.uid)
        storage.putBytes(compressedData).addOnSuccessListener {
            callback(imageUri)
        }
            .addOnFailureListener{
                callback(null)
            }
        /*
        storage.putFile(imageUri)
            .addOnSuccessListener {
                callback(imageUri)
        }
            .addOnFailureListener{
                callback(null)
            }*/
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
                    Log.e("FirebaseError", "Error getting data")
                    callback(null)
                }
            })
    }
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure : () -> Unit) {
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
                onFailure()
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