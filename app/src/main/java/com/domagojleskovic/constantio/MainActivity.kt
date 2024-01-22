package com.domagojleskovic.constantio

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.domagojleskovic.constantio.ui.MainScreen
import com.domagojleskovic.constantio.ui.ProfileScreen
import com.domagojleskovic.constantio.ui.RegisterScreen
import com.domagojleskovic.constantio.ui.theme.ConstantioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.domagojleskovic.constantio.ui.LoginScreen
import com.domagojleskovic.constantio.ui.Profile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity : ComponentActivity() {
    private val database: DatabaseReference = Firebase.database.reference
    private lateinit var imageUri : Uri
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        setContent {
            ConstantioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val emailPasswordManager = EmailPasswordManager(this, navController)
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onNavigateRegisterScreen = { navController.navigate("register")},
                                emailPasswordManager
                            )
                        }
                        composable("register") { RegisterScreen(emailPasswordManager) }
                        composable("main_screen") { MainScreen(
                            onNavigateProfileScreen = {navController.navigate("profile")},
                            emailPasswordManager
                        ) }
                        composable("profile") { ProfileScreen(profile = Profile(Uri.parse("android.resource://${packageName}/${R.drawable.logo}"),"null",  "Marko", "Markic@gmail.com"))}
                    }
                }
            }
        }
    }
}



