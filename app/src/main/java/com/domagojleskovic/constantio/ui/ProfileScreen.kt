package com.domagojleskovic.constantio.ui

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domagojleskovic.constantio.EmailPasswordManager
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


@Composable
fun ProfileScreen(
    emailPasswordManager : EmailPasswordManager,
    userId: String?
) {
    var profile by remember { mutableStateOf<Profile?>(null) }

    DisposableEffect(userId) {
        val userListener = emailPasswordManager.getDBO()
            .child("users")
            .child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue<Profile>()
                    userProfile?.let {
                        emailPasswordManager.getCurrentUserImageUri {
                                uri ->
                            if(uri != null){
                                val updatedProfile = userProfile.copy(icon = uri)
                                profile = updatedProfile
                            }
                            else{
                                //
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error getting data", error.toException())
                }
            })
        onDispose {
            emailPasswordManager.getDBO()
                .child("users")
                .child(userId)
                .removeEventListener(userListener)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brownish_Palette)
    ) {
        item {
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
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Constantio", fontSize = 48.sp,
                                fontFamily = FontFamily.Cursive,
                                fontWeight = FontWeight.W700,
                                color = Color.White
                            )
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
                        Row (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            //StoryIcon(profile = profile)
                            Column (
                                modifier = Modifier.padding(16.dp)
                            ){
                                /*
                                Text(
                                    text = profile?.name as String, fontSize = 28.sp,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.W700,
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )*/
                                //Text(
                                //    text = "Posts: ${profile.listOfPictures.size}", fontSize = 20.sp,
                                //    fontFamily = FontFamily.Cursive,
                                //    fontWeight = FontWeight.W700,
                                //    color = Color.White,
                                //    modifier = Modifier.padding(8.dp)
                                //)
                                Text(
                                    text = "Followers: 0", fontSize = 20.sp,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.W700,
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    text = "Following: 0", fontSize = 20.sp,
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
                        ){
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LightRed_Palette
                                )
                            ) {
                                Text(text = "Follow")
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LightRed_Palette
                                )
                            ) {
                                Text(text = "Message")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            /*
            if(profile.listOfPictures.isEmpty()){
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 36.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ){
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
                EasyGrid(nColumns = 3, items = profile.listOfPictures) {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable(
                                onClick = {
                                    /*TODO - Go to post screen*/
                                }
                            )
                    )
                }
            }
            */
        }
    }
}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit){
    Column(){
        for(i in items.indices step nColumns){
            Row{
                for(j in 0 until nColumns){
                    if(i + j < items.size){
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ){
                            content(items[i+j])
                        }}else{
                        Spacer(Modifier.weight(1f,fill = true))
                    }
                }
            }
        }
    }
}