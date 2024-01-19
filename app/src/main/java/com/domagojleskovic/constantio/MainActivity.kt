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
import com.domagojleskovic.constantio.ui.RegisterScreen
import com.domagojleskovic.constantio.ui.listOfProfiles
import com.domagojleskovic.constantio.ui.theme.ConstantioTheme
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


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
                        Profile(R.drawable.profpic1, "Marko", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic2, "Constantin", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic3, "Yeaah", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic4, "Wassup", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic5, "Lego", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic6, "Constantin", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic7, "Yeaah", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic8, "Wassup", listOf(), listOf(), listOf()),
                        Profile(R.drawable.profpic9, "Wassup", listOf(), listOf(), listOf())
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
                    var email = "marko@gmail.com"
                    var password = "password"
                    var auth = Firebase.auth

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this){
                            task->
                            if(task.isSuccessful){
                                println("Success")
                            }
                            else
                            {
                                println("Error")
                            }
                        }
                    /*
                    val authenticator = EmailPasswordActivity()
                    authenticator.createAccount("Marko@gmail.com", "LigmaSigma")*/
                    //ProfileScreen(listOfProfiles[2])
                    RegisterScreen()
                }
            }
        }
    }
}

