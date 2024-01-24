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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    emailPasswordManager: EmailPasswordManager
) {
    var email by remember { mutableStateOf("d@g.com") } // TODO set to empty at deployment
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue_Palette),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Spacer(modifier = Modifier.size(40.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = "Enter email to recover password",
                fontWeight = FontWeight.W400,
                color = Color.White
            )
        }
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
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                emailPasswordManager?.resetPassword(email) {
                    navController?.navigate("login")
                }
            },
            modifier = Modifier.width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightRed_Palette
            )
        ) {
            Text("Reset", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.size(135.dp))
    }

}
