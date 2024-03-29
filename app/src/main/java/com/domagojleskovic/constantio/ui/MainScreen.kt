package com.domagojleskovic.constantio.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class Profile(
    var icon : Uri? = null,
    val userId : String? = null,
    var name : String? = null,
    var email : String? = null,
    var listOfPosts : List<Post> = listOf(),
    var listOfFollowedProfiles : List<String?> = listOf()
){
    override fun toString(): String {
        return if(name == null)
            ""
        else
            name as String
    }
}

data class Post(
    var image : Uri? = null,
    var description : String? = null,
    val userId: String? = null,
    val uniqueUUID : String? = null,
    var datePosted : Long = System.currentTimeMillis()
){
    override fun toString(): String {
        return "Image: $image\n Desc: $description \n DatePosted: ${Date(datePosted)}"
    }
}

/*
val listOfProfiles = mutableListOf<Profile>(
    Profile(null, R.drawable.profpic1, "Marko", listOf(),
        listOf(
            R.drawable.profpic1,
            R.drawable.profpic2
        ), listOf()),
    Profile(null, R.drawable.profpic2, "Constantin", listOf(), listOf(), listOf()),
    Profile(null, R.drawable.profpic3, "Yeaah", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic4, "Wassup", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic5, "Lego", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic6, "Constantin", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic7, "Yeaah", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic8, "Wassup", listOf(), listOf(), listOf()),
    Profile(null,R.drawable.profpic9, "Wassup", listOf(), listOf(), listOf())
)*/

@Composable
fun MainScreen(
    onNavigateProfileScreen : (String?) -> Unit,
    onNavigateSearchScreen : () -> Unit,
    emailPasswordManager: EmailPasswordManager,
    context: Context
) {
    var profile by remember { mutableStateOf<Profile?>(null) }
    profile = emailPasswordManager.profile
    val scrollState = rememberLazyListState()
    var showActivity by remember { mutableStateOf(false)}
    LaunchedEffect(Unit){
        val followedProfiles = emailPasswordManager.getFollowedProfiles()
        Log.i("FollowedProfiles",   followedProfiles.toString())
        var noActivity = true
        followedProfiles.forEach{followedProfile ->
            if(followedProfile.listOfPosts.isNotEmpty()){
                noActivity = false
                return@forEach
            }
        }
        showActivity = noActivity
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brownish_Palette),
        state = scrollState,
    ) {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(DarkBlue_Palette)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Brownish_Palette
                                ),
                                startY = 180f
                            )
                        )
                )
                Column (
                    modifier = Modifier.fillMaxSize()
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        Image(painter = rememberAsyncImagePainter(profile?.icon),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .border(2.dp, Brownish_Palette, CircleShape)
                                .clickable {
                                    profile?.let { onNavigateProfileScreen(profile?.userId) }
                                }
                        )
                        Spacer(modifier = Modifier.width(70.dp))
                        Text(text = "Constantio", fontSize = 36.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.W700,
                            color = Color.White)
                        Spacer(modifier = Modifier.width(78.dp))
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(top = 8.dp)
                                .clickable(
                                    onClick = {
                                        onNavigateSearchScreen()
                                    }
                                )
                        )
                    }
                    Divider(
                        thickness = 2.dp,
                        color = LightRed_Palette,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                }
            }
        }
        item{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Activity", fontSize = 36.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W700,
                    color = Color.White)
            }
            if(showActivity){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No Activity", fontSize = 36.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.W700,
                        color = Color.White)
                }
            }
        }
        val listOfFollowedProfiles = emailPasswordManager.followedProfiles
        items(listOfFollowedProfiles) { followedProfile ->
            followedProfile.listOfPosts.forEach { post ->
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Post(
                        onNavigateProfileScreen = { onNavigateProfileScreen(it) },
                        profile = followedProfile,
                        post = post
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StoryIcon(
    profile: Profile?,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val imageUri = profile?.icon
        if(imageUri != null){
            Image(painter = rememberAsyncImagePainter(profile.icon),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(2.dp, Brownish_Palette, CircleShape)
                    .clickable(
                        onClick = {
                            /*
                            for (story in profile.listOfStories){
                                println("Story")
                            }
                            */
                        }
                    )
            )
        }else{
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Default Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(2.dp, Brownish_Palette, CircleShape)
                    .clickable(
                        onClick = {
                            /*
                            for (story in profile.listOfStories){
                                println("Story")
                            }
                            */
                        }
                    )
            )
        }

    }
}
