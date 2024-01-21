package com.domagojleskovic.constantio

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.domagojleskovic.constantio.ui.Comment
import com.domagojleskovic.constantio.ui.MainScreen
import com.domagojleskovic.constantio.ui.Post
import com.domagojleskovic.constantio.ui.Profile
import com.domagojleskovic.constantio.ui.ProfileScreen
import com.domagojleskovic.constantio.ui.RegisterScreen
import com.domagojleskovic.constantio.ui.listOfProfiles
import com.domagojleskovic.constantio.ui.theme.ConstantioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.domagojleskovic.constantio.ui.LoginScreen
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.initialize


class MainActivity : ComponentActivity() {
    private val database: DatabaseReference = Firebase.database.reference
    private var auth: FirebaseAuth = Firebase.auth
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
                        composable("main_screen") { MainScreen(emailPasswordManager.getCurrentUser()) }
                        composable("profile") { ProfileScreen(profile = listOfProfiles[0])}
                    }
                }
            }
        }
    }
}


