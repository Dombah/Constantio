package com.domagojleskovic.constantio.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    image : String?,
    emailPasswordManager: EmailPasswordManager,
    onAddPost : () -> Unit,
) {
    val imageUri = Uri.parse(image)
    var descriptionEntered by remember { mutableStateOf("")}

    val profile = emailPasswordManager.profile
    val coroutineScope = rememberCoroutineScope()

    var listOfPictures by remember { mutableStateOf(profile.listOfPictures) }

    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to DarkBlue_Palette,
            0.2f to Brownish_Palette,
        )
    )

    if(isLoading)
    {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(gradient),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                trackColor = Color.White,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                strokeWidth = 6.dp,
                modifier = Modifier.size(60.dp)
            )
        }
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Constantio", fontSize = 48.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W700,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Spacer(modifier = Modifier.padding(start = 16.dp))
                AsyncImage(model = profile.icon,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brownish_Palette, CircleShape)
                )
                Text(
                    text = "@Constantio", fontSize = 24.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = descriptionEntered,
                    onValueChange = { value->
                        descriptionEntered = value
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Enter description", color = Color.White)
                    },
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        focusedBorderColor = Color.White
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        writePostToDatabase(
                            coroutineScope = coroutineScope,
                            emailPasswordManager = emailPasswordManager,
                            imageUri = imageUri
                        ){
                            listOfPictures = listOf(imageUri) + listOfPictures
                            profile.listOfPictures = listOfPictures
                            isLoading = false
                            onAddPost()
                        }
                    },
                    modifier = Modifier.width(150.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightRed_Palette)
                ) {
                    Text("Add post", fontSize = 16.sp)
                }
            }
        }
    }

}

private fun writePostToDatabase(
    coroutineScope: CoroutineScope,
    emailPasswordManager : EmailPasswordManager,
    imageUri : Uri,
    onSuccess : () -> Unit
){
    coroutineScope.launch {
        emailPasswordManager.writeUserPicture(
            isProfilePicture = false,
            imageUri,
            compressionPercentage = 65,
        )
        onSuccess()
    }
}