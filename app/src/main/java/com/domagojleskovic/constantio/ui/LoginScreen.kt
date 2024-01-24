package com.domagojleskovic.constantio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.domagojleskovic.constantio.ui.theme.Red_Palette

@Composable
fun LoginScreen(
    onNavigateForgotPasswordScreen: () -> Unit,
    onNavigateRegisterScreen: () -> Unit,
    navController: NavController,
    emailPasswordManager: EmailPasswordManager
) {
    var email by remember { mutableStateOf("d@g.com") } // TODO set to empty at deployment
    var password by remember { mutableStateOf("123456") } // TODO set to empty at deployment
    var passwordVisible by remember { mutableStateOf(false) }
    var progressBarLoading by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue_Palette),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(progressBarLoading){
            CircularProgressIndicator(
                trackColor = Color.White,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                strokeWidth = 6.dp,
                modifier = Modifier.size(60.dp)
            )
        }
        else{
            Spacer(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
            )
            Text(
                text = "Constantio", fontSize = 48.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.W700,
                color = Color.White
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text("Email", color = Color.White)
                },
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(color = Color.White),
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password", color = Color.White)
                },
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(color = Color.White),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            description,
                            tint = Color.White
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                ClickableText(
                    text = AnnotatedString("Forgot Password?"),
                    onClick = {
                        onNavigateForgotPasswordScreen()
                    },
                    style = TextStyle(
                        color = Red_Palette
                    ),
                    modifier = Modifier.padding(4.dp, 4.dp, 16.dp, 4.dp)
                )

            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    progressBarLoading = true
                    emailPasswordManager.signIn(email,password){
                        navController.navigate("main_screen")
                        progressBarLoading = false
                    }
                },
                modifier = Modifier.width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightRed_Palette
                )
            ) {
                Text("Log In", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White
                )
                ClickableText(
                    text = AnnotatedString("Sign Up"),
                    onClick = { onNavigateRegisterScreen() },
                    style = TextStyle(
                        color = Red_Palette
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
