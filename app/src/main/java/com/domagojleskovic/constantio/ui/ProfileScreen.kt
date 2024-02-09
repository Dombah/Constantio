package com.domagojleskovic.constantio.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userId: String?,
    emailPasswordManager: EmailPasswordManager,
    onNavigateAddPostScreen : (Uri?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var progressBarLoading by remember { mutableStateOf(false) }

    var profile by remember { mutableStateOf<Profile?>(null) }
    var listOfPosts by remember { mutableStateOf(profile?.listOfPosts ?: emptyList()) }
    var isFollowing = mutableStateOf(emailPasswordManager.profile.listOfFollowedProfiles.contains(profile?.userId))
    val isUserProfileOwner = userId == emailPasswordManager.getCurrentUser()?.uid
    if(isUserProfileOwner){
        profile = emailPasswordManager.profile
        listOfPosts = profile!!.listOfPosts
    }
    else{
        LaunchedEffect(Unit){
            progressBarLoading = true
            coroutineScope.async {
                profile = emailPasswordManager.parseUserToProfile(userId)
            }.await()
            progressBarLoading = false
            listOfPosts = profile!!.listOfPosts
            isFollowing = mutableStateOf(emailPasswordManager.profile.listOfFollowedProfiles.contains(profile?.userId))
        }
    }
    val name = profile?.name ?: "Default Name"
    var postPictureUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val postPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            postPictureUri = it
        }
    )
    var profilePictureUri by remember {
        mutableStateOf(profile?.icon)
    }
    val profilePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            profilePictureUri = it
        }
    )

    LaunchedEffect(postPictureUri) {
        if(postPictureUri != null)
        {
            onNavigateAddPostScreen(postPictureUri)
        }
        Log.i("ProfilePictureCount", "This profile currently has: ${profile?.listOfPosts?.size}")
    }
    LaunchedEffect(profilePictureUri) {
        if(profilePictureUri != profile?.icon){ // Otherwise will be called as soon as navigating to ProfileScreen
            coroutineScope.launch {
                emailPasswordManager.writeUserProfilePicture(
                    profilePictureUri as Uri,
                    compressionPercentage = 75
                )
                profile = profile!!.copy(icon = profilePictureUri)
                emailPasswordManager.profile = profile!!
                progressBarLoading = false
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brownish_Palette)
    ) {
        item {
            if (progressBarLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(DarkBlue_Palette)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Brownish_Palette
                                    ),
                                    startY = 200f
                                )
                            )
                    ){

                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        trackColor = Color.White,
                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(60.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(DarkBlue_Palette)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Brownish_Palette
                                    ),
                                    startY = 200f
                                )
                            )
                    )
                    {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Constantio", fontSize = 36.sp,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.W700,
                                    color = Color.White
                                )
                            }
                            Divider(
                                thickness = 2.dp,
                                color = LightRed_Palette,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StoryIcon(profile = profile)
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "@$name", fontSize = 28.sp,
                                        fontFamily = FontFamily.Cursive,
                                        fontWeight = FontWeight.W700,
                                        color = Color.White,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Text(
                                        text = "Posts: ${profile?.listOfPosts?.size}",
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily.Cursive,
                                        fontWeight = FontWeight.W700,
                                        color = Color.White,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Text(
                                        text = "Following: ${profile?.listOfFollowedProfiles?.size}", fontSize = 20.sp,
                                        fontFamily = FontFamily.Cursive,
                                        fontWeight = FontWeight.W700,
                                        color = Color.White,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                            ) {
                                if (!isUserProfileOwner) {
                                    Button(
                                        onClick = {
                                            if(!isFollowing.value){
                                                emailPasswordManager.followProfile(profile?.userId){
                                                    isFollowing.value = true
                                                    coroutineScope.launch {
                                                        progressBarLoading = true
                                                        emailPasswordManager.followedProfiles = emailPasswordManager.getFollowedProfiles()
                                                        progressBarLoading = false
                                                    }
                                                }
                                            }else{
                                                emailPasswordManager.unfollowProfile(profile?.userId){
                                                    isFollowing.value = false
                                                    coroutineScope.launch {
                                                        progressBarLoading = true
                                                        emailPasswordManager.followedProfiles = emailPasswordManager.getFollowedProfiles()
                                                        progressBarLoading = false
                                                    }
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = LightRed_Palette
                                        )
                                    ) {
                                        if(!isFollowing.value){
                                            Text("Follow")
                                        }else{
                                            Text(text = "Following")
                                        }
                                    }
                                    Spacer(modifier = Modifier.padding(8.dp))
                                } else {
                                    Button(
                                        onClick = {
                                            postPhotoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = LightRed_Palette
                                        )
                                    ) {
                                        Text(text = "Add post")
                                    }
                                    Spacer(modifier = Modifier.padding(8.dp))
                                    Button(
                                        onClick = {
                                            progressBarLoading = true
                                            profilePhotoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = LightRed_Palette
                                        )
                                    ) {
                                        Text(text = "Change profile picture")
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (listOfPosts.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, end = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No posts to display", fontSize = 32.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.W700,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else{
                    EasyGrid(nColumns = 3, items = listOfPosts) { post ->
                        AsyncImage(
                            model = post.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}