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
                    PostScreen(post = post1)
                }
            }
        }
    }
}

