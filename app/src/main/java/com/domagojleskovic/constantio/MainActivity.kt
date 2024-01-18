package com.domagojleskovic.constantio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.domagojleskovic.constantio.ui.Comment
import com.domagojleskovic.constantio.ui.MainScreen
import com.domagojleskovic.constantio.ui.Post
import com.domagojleskovic.constantio.ui.PostScreen
import com.domagojleskovic.constantio.ui.Profile
import com.domagojleskovic.constantio.ui.ProfileScreen
import com.domagojleskovic.constantio.ui.listOfProfiles
import com.domagojleskovic.constantio.ui.theme.ConstantioTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConstantioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        val post1 = Post(image = R.drawable.profpic3,
                            description = "Where my girls at?" ,
                            profile = listOfProfiles[2],
                            comments = listOf(
                                Comment(listOfProfiles[3], "Wow, so stunning!"),
                                Comment(listOfProfiles[4], "In love with you"),
                                Comment(listOfProfiles[5], "When are we going to get out nails done?")
                            )
                    )
                    val listOfProfiles = mutableListOf<Profile>(
                        Profile(R.drawable.profpic1, "Marko", listOf()),
                        Profile(R.drawable.profpic2, "Constantin", listOf()),
                        Profile(R.drawable.profpic3, "Yeaah", listOf()),
                        Profile(R.drawable.profpic4, "Wassup", listOf()),
                        Profile(R.drawable.profpic5, "Lego", listOf()),
                        Profile(R.drawable.profpic6, "Constantin", listOf()),
                        Profile(R.drawable.profpic7, "Yeaah", listOf()),
                        Profile(R.drawable.profpic8, "Wassup", listOf()),
                        Profile(R.drawable.profpic9, "Wassup", listOf())
                    )

                    val listOfPictures : List<Int> = listOf(
                        R.drawable.pic1,
                        R.drawable.pic2,
                        R.drawable.pic3,
                        R.drawable.pic4,
                        R.drawable.pic5,
                        R.drawable.profpic1,
                        R.drawable.profpic2,
                        R.drawable.profpic3,
                        R.drawable.profpic4,
                        R.drawable.profpic5
                    )
                    listOfProfiles[2].listOfPictures = listOf(
                        listOfPictures[0],
                        listOfPictures[2],
                        listOfPictures[4]
                    )
                    ProfileScreen(listOfProfiles[2])
                }
            }
        }
    }
}

