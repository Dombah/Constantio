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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.domagojleskovic.constantio.ui.theme.Red_Palette

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    var textUser by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }

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
                    OutlinedTextField(
                        value = textUser, onValueChange = {
                            textUser = it
                        },
                        label = {
                            Text("Username", color = Color.White)
                        },
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(16.dp),

                    )
                    OutlinedTextField(
                        value = textPassword,
                        onValueChange = {
                            textPassword = it
                        },
                        label = {
                            Text("Password", color = Color.White)
                        },
                        shape = RoundedCornerShape(16.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ){
                       ClickableText(
                           text = AnnotatedString("Forgot Password?"),
                           onClick = {/*TODO*/},
                           style = TextStyle(
                                    color = Red_Palette
                                ),
                           modifier = Modifier.padding(4.dp, 4.dp, 16.dp, 4.dp)
                           )

                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { /*TODO*/ },
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
                        Text(text = "Don't have an account? ",
                            color = Color.White)
                        ClickableText(
                            text = AnnotatedString("Sign Up"),
                            onClick = {/*TODO*/},
                            style = TextStyle(
                                color = Red_Palette
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }

}


