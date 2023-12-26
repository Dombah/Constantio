package com.domagojleskovic.constantio

import android.widget.Space
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultStrokeLineWidth
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue2_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRedTransparent_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.domagojleskovic.constantio.ui.theme.Red_Palette

data class Profile(
    @DrawableRes var icon : Int,
    var name : String
){
    override fun toString(): String {
        return name
    }
}

data class Comment(
    val profile : Profile,
    var text : String
){
    override fun toString(): String {
        return text
    }
}

val listOfProfiles = mutableListOf<Profile>(
    Profile(R.drawable.profpic1, "Marko"),
    Profile(R.drawable.profpic2, "Constantin"),
    Profile(R.drawable.profpic3, "Yeaah"),
    Profile(R.drawable.profpic4, "Wassup"),
    Profile(R.drawable.profpic5, "Lego"),
    Profile(R.drawable.profpic6, "Constantin"),
    Profile(R.drawable.profpic7, "Yeaah"),
    Profile(R.drawable.profpic8, "Wassup"),
    Profile(R.drawable.profpic9, "Wassup")
)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
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
                        items(listOfProfiles){profile ->
                            StoryIcon(profile = profile)
                        }
                    }

                }
            }
            Column {
                Post(
                    image = R.drawable.profpic1,
                    description = "Living life :D" ,
                    profile = listOfProfiles[0],
                    comments = listOf()
                )
                Post(
                    image = R.drawable.profpic2,
                    description = "Where my girls at?" ,
                    profile = listOfProfiles[1],
                    comments = listOf(
                        Comment(listOfProfiles[2], "Wow, so stunning!"),
                        Comment(listOfProfiles[3], "In love with you"),
                        Comment(listOfProfiles[6], "When are we going to get out nails done?")
                    )
                )
                Post(
                    image = R.drawable.profpic3,
                    description = "Where my girls at?" ,
                    profile = listOfProfiles[2],
                    comments = listOf(
                        Comment(listOfProfiles[3], "Wow, so stunning!"),
                        Comment(listOfProfiles[4], "In love with you"),
                        Comment(listOfProfiles[5], "When are we going to get out nails done?")
                    )
                )
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
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Image(painter = painterResource(id = profile.icon),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(2.dp, Brownish_Palette, CircleShape)
        )
    }
}

@Composable
fun Post(
    @DrawableRes image: Int,
    description: String,
    profile : Profile,
    comments : List<Comment>
) {
    val roundedCornerShape = 8
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card (
            colors = CardDefaults.cardColors(
                containerColor = LightRedTransparent_Palette
            )
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(roundedCornerShape.dp))
                    .border(1.dp, LightRed_Palette, RoundedCornerShape(roundedCornerShape.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier.padding(start = 8.dp)
            ){
                Text(
                    "@${profile.name}: $description",
                    color = Color.White,
                    softWrap = true,
                    fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            for (comment in comments){
                Text(
                    text = "@${comment.profile}: $comment",
                    color = Color.White,
                    softWrap = true,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                    fontSize = 18.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}