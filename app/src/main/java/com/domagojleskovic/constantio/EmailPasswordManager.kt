package com.domagojleskovic.constantio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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
                writeUserPicture(
                    isProfilePicture = true,
                    imageUri,
                    compressionPercentage = 75
                ) {
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
    fun writeUserPicture(
        isProfilePicture: Boolean,
        imageUri : Uri,
        compressionPercentage : Int = 20,
        callback : (Uri?) -> Unit
    ){
        if(compressionPercentage < 0 || compressionPercentage > 100){
            Log.e("CompressionError", "The compression percentage is out of bounds")
            return
        }
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

        val timestamp = if (Build.VERSION.SDK_INT < 26) {
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
        } else {
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(LocalDateTime.now())
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100 - compressionPercentage, byteArrayOutputStream)
        val compressedData = byteArrayOutputStream.toByteArray()

        val uniqueUUID = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().reference
        storage = when {
            isProfilePicture -> {
                storageRef.child(StringConstants.firebaseUserProfilePicturePath + auth.currentUser!!.uid)
            }
            else -> {
                storageRef.child(StringConstants.firebaseUserPicturesPath + auth.currentUser!!.uid + "/Posts/" + uniqueUUID + " " + timestamp)
            }
        }
        storage.putBytes(compressedData).addOnSuccessListener {
            callback(imageUri)
        }
            .addOnFailureListener{
                callback(null)
            }
    }
    fun getUserImageUri(userId: String? ,callback: (Uri?) -> Unit) {
        storage = FirebaseStorage.getInstance().getReference(StringConstants.firebaseUserProfilePicturePath+userId)
        val localFile = File.createTempFile("images", ".png")
        storage.getFile(localFile).addOnSuccessListener {
            callback(Uri.fromFile(localFile))
        }.addOnFailureListener {
            callback(Uri.parse("android.resource://${context.packageName}/${R.drawable.logo}"))
        }
    }

    private fun fetchUserPosts(userId: String? ,callback: (List<Uri?>) -> Unit){
        storage = FirebaseStorage.getInstance().getReference(StringConstants.firebaseUserPicturesPath+userId+"/Posts/")
        storage.listAll()
            .addOnSuccessListener { listResult ->
                val itemsCount = listResult.items.size
                if (itemsCount == 0) {
                    callback(emptyList())
                }
                val urisWithTimestamps = mutableListOf<Pair<Long, Uri?>>()
                listResult.items.forEach { item ->
                    item.downloadUrl
                        .addOnSuccessListener { uri ->
                            val timestamp = extractTimestampFromUri(uri)
                            urisWithTimestamps.add(timestamp to uri)
                            if (urisWithTimestamps.size == itemsCount) {
                                val sortedUris = urisWithTimestamps.sortedByDescending { it.first }.map { it.second }
                                callback(sortedUris)
                            }
                    }
                        .addOnFailureListener {
                            urisWithTimestamps.add(0L to null)
                            if (urisWithTimestamps.size == itemsCount) {
                                val sortedUris = urisWithTimestamps.sortedByDescending { it.first }.map { it.second }
                                callback(sortedUris)
                            }
                    }
                }
            }
            .addOnFailureListener{
                callback(emptyList())
            }
    }

    private fun extractTimestampFromUri(uri : Uri) : Long{
        val decodedFileName = URLDecoder.decode(uri.lastPathSegment, StandardCharsets.UTF_8.name())

        val regex = Regex("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}")
        val matchResult = regex.find(decodedFileName)

        return if (matchResult != null) {
            val dateString = matchResult.value
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            try {
                dateFormat.parse(dateString)?.time ?: 0L
            } catch (e: Exception) {
                Log.e("DateToLongParsingError", "Couldn't parse dateFormat inside extractTimestampFromUri")
                0L
            }
        } else {
            0L
        }
    }

    suspend fun parseUserToProfile(userId: String?): Profile? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            if (userId == null) {
                continuation.resume(null)
                return@suspendCoroutine
            }

            getDBO().child("users").child(userId).get().addOnSuccessListener { snapshot ->
                val userProfile = snapshot.getValue<Profile>()
                userProfile?.let { profile ->
                    getUserImageUri(userId) { uri ->
                        if (uri != null) {
                            var updatedProfile = profile.copy(icon = uri)
                            fetchUserPosts(userId) { posts ->
                                updatedProfile = updatedProfile.copy(listOfPictures = posts)
                                continuation.resume(updatedProfile)
                            }
                        } else {
                            continuation.resume(null)
                        }
                    }
                } ?: continuation.resume(null) // If userProfile is null, resume with null
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }
    suspend fun signIn(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user

            if (user != null) {
                val profile = parseUserToProfile(user.uid)
                val allUsers = addAllUsers()

                if (profile != null) {
                    this@EmailPasswordManager.profile = profile.apply {
                        listOfFollowedProfiles = allUsers
                    }
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "User either doesn't exist or the password is wrong", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    private suspend fun addAllUsers() : List<Profile> = withContext(Dispatchers.IO) {
        coroutineScope {
            val dataSnapshot = database.child("users").get().await()
            val deferredProfiles = dataSnapshot.children.mapNotNull { childSnapshot ->
                val profile = childSnapshot.getValue(Profile::class.java)
                val userId = childSnapshot.key
                if (profile != null && userId != auth.currentUser?.uid) {
                    async { parseUserToProfile(userId) }
                } else null
            }
            deferredProfiles.awaitAll().filterNotNull()
        }
    }

    fun resetPassword(email : String, onSuccess: () -> Unit){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                task ->
                if(task.isSuccessful){
                    Toast.makeText(context, "Successfully send password reset request to your email address", Toast.LENGTH_SHORT).show()
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