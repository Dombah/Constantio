package com.domagojleskovic.constantio

import android.os.Bundle
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
import com.domagojleskovic.constantio.ui.ForgotPasswordScreen
import com.domagojleskovic.constantio.ui.LoginScreen
import com.google.firebase.Firebase
import com.google.firebase.initialize


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        setContent {
            ConstantioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val emailPasswordManager = EmailPasswordManager(this, navController)
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onNavigateForgotPasswordScreen = { navController.navigate("forgot_password")},
                                onNavigateRegisterScreen = { navController.navigate("register")},
                                navController,
                                emailPasswordManager
                            )
                        }
                        composable("register") { RegisterScreen(emailPasswordManager, navController, this@MainActivity) }
                        composable("main_screen") { MainScreen(
                            onNavigateProfileScreen = { profile ->
                                navController.navigate("profile/${profile.userId}")
                            },
                            navController,
                            emailPasswordManager,
                            this@MainActivity
                        ) }
                        composable("profile/{userId}"){
                            navBackStackEntry ->
                            ProfileScreen(
                                emailPasswordManager,
                                userId = navBackStackEntry.arguments?.getString("userId"),
                            )
                        }
                        composable("forgot_password"){
                            ForgotPasswordScreen(
                                navController = navController,
                                emailPasswordManager = emailPasswordManager
                            )
                        }
                    }
                }
            }
        }
    }
}



