package com.domagojleskovic.constantio.ui

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.google.firebase.auth.FirebaseUser

data class Profile(
    val userID : String? = null,
    //@DrawableRes var icon : Int,
    var name : String? = null,
    var email : String? = null,
    /*var listOfPictures : List<Int>,
    var listOfStories : List<Int>,
    var listOfFollowedProfiles : List<Profile>*/
){
    override fun toString(): String {
        return if(name == null)
            ""
        else
            name as String
    }
}

data class Post(
    @DrawableRes var image: Int,
    var description : String,
    val profile : Profile,
    var comments : List<Comment>,
    var liked : Boolean = false
)

data class Comment(
    val profile : Profile,
    var text : String
){
    override fun toString(): String {
        return text
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
val listOfProfiles = mutableListOf<Profile>(
    Profile("null",  "Marko", "Markic@gmail.com"),
    Profile("null", "Constantin","Markic@gmail.com"),
    Profile("null", "Yeaah","Markic@gmail.com"),
    Profile("null", "Wassup","Markic@gmail.com"),
    Profile("null", "Lego","Markic@gmail.com"),
    Profile("null", "Constantin","Markic@gmail.com"),
    Profile("null", "Yeaah","Markic@gmail.com"),
    Profile("null", "Wassup","Markic@gmail.com"),
    Profile("null", "Wassup","Markic@gmail.com")
)
@Composable
fun MainScreen(
    user : FirebaseUser?
) {
    val scrollState = rememberLazyListState()
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
                    .height(250.dp)
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
                                startY = 200f
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
                        Text(text = "Constantio", fontSize = 36.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.W700,
                            color = Color.White)
                        Spacer(modifier = Modifier.width(70.dp))
                        Image(
                            painter = painterResource(id = R.drawable.send_red),
                            contentDescription = null,
                            modifier = Modifier
                                .width(36.dp)
                                .height(36.dp)
                                .padding(top = 8.dp)
                        )
                    }
                    Divider(
                        thickness = 2.dp,
                        color = LightRed_Palette,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    LazyRow(modifier = Modifier.fillMaxWidth()){
                        items(listOfProfiles){ profile ->
                            StoryIcon(profile = profile)
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Activity", fontSize = 36.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.W700,
                            color = Color.White)
                    }
                }
            }
            Column (
                modifier = Modifier.fillMaxSize()
            ){
                if (user != null) {
                    Text(text = "User : ${user.email}", fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun StoryIcon(
    profile : Profile
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.profpic1/*profile.icon*/),
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
    }
}
