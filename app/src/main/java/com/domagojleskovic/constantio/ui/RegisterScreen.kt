package com.domagojleskovic.constantio.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette

@Preview(showBackground = true)
@Composable
fun RegisterScreen(
    emailPasswordManager: EmailPasswordManager,
    navController:NavController,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("")}
    var openAlertDialog by remember { mutableStateOf(false)}
    var passwordVisible by remember { mutableStateOf(false)}
    var alertDialogTitle by remember { mutableStateOf("") }
    var alertDialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue_Palette),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.width(350.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkBlue_Palette
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                Spacer(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                Text(text = "Constantio", fontSize = 48.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W700,
                    color = Color.White)
                Spacer(modifier = Modifier.padding(top = 8.dp))
                OutlinedTextField(
                    value = email, onValueChange = {
                        email = it
                    },
                    label = {
                        Text("Email", color = Color.White)
                    },
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
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(
                                imageVector  = image,
                                description,
                                tint = Color.White
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    label = {
                        Text("Confirm Password: ", color = Color.White)
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if(confirmPassword != password){
                            Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                        }else {
                            emailPasswordManager.createAccount(email, password) {
                                navController.navigate("main_screen")
                            }
                        }
                    },
                    modifier = Modifier.width(150.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightRed_Palette
                    )
                ) {
                    Text("Sign Up", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                }
            }

        }
        when {
            openAlertDialog -> {
                EmailPasswordAlertDialog(
                    onDismissRequest = { openAlertDialog = false },
                    onConfirmation = { openAlertDialog = false },
                    dialogTitle = alertDialogTitle,
                    dialogText = alertDialogMessage
                )
            }
        }
    }
}


