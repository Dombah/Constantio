package com.domagojleskovic.constantio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.domagojleskovic.constantio.ui.MainScreen
import com.domagojleskovic.constantio.ui.ProfileScreen
import com.domagojleskovic.constantio.ui.RegisterScreen
import com.domagojleskovic.constantio.ui.theme.ConstantioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domagojleskovic.constantio.ui.ForgotPasswordScreen
import com.domagojleskovic.constantio.ui.LoginScreen
import com.domagojleskovic.constantio.ui.SearchScreen
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
                    val emailPasswordManager = EmailPasswordManager(this)

                    // View models
                    val searchViewModel = SearchViewModel(emailPasswordManager)
                    val loginViewModel = LoginViewModel(emailPasswordManager)
                    val registerViewModel = RegisterViewModel(emailPasswordManager)

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onNavigateForgotPasswordScreen = { navController.navigate("forgot_password") },
                                onNavigateRegisterScreen = { navController.navigate("register") },
                                onNavigateMainScreen = { navController.navigate("main_screen") },
                                viewModel = loginViewModel,
                                this@MainActivity
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onNavigateMainScreen = { navController.navigate("main_screen") },
                                viewModel = registerViewModel,
                                context = this@MainActivity
                            )
                        }
                        composable("main_screen") { MainScreen(
                            onNavigateProfileScreen = {
                                navController.navigate("profile")
                            },
                            onNavigateSearchScreen = {
                                navController.navigate("search_screen")
                            },
                            emailPasswordManager,
                            this@MainActivity
                        ) }
                        composable("profile"){
                            ProfileScreen(
                                emailPasswordManager
                            )
                        }
                        composable("forgot_password"){
                            ForgotPasswordScreen(
                                navController = navController,
                                emailPasswordManager = emailPasswordManager
                            )
                        }
                        composable("search_screen"){
                            SearchScreen(
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}



