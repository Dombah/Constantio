package com.domagojleskovic.constantio.ui

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.domagojleskovic.constantio.R
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.LightRedTransparent_Palette
import com.domagojleskovic.constantio.ui.theme.LightRed_Palette

@Preview(showBackground = true)
@Composable
fun PostPreview() {
    val context = LocalContext.current
    val postUri = Uri.parse("android.resource://${context.packageName}/${R.drawable.logo}")
    val post = Post(
        image = postUri,
        description = "Wow, you are so gay",
        userId = "eKopiKNVErZ6Vd5qyl1BA5fnySJ3",
        uniqueUUID = null,
        datePosted = System.currentTimeMillis()
        )
    //Post(null , post, "Doctor")
}

@Composable
fun Post(
    profile : Profile,
    post: Post,
) {
    val roundedCornerShape = 8.dp
    Column(
        modifier = Modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = LightRedTransparent_Palette
            )
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Spacer(modifier = Modifier.padding(start = 16.dp))
                AsyncImage(model = profile.icon,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brownish_Palette, CircleShape)
                )
                Text(
                    text = "@${profile.name}", fontSize = 24.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = post.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(roundedCornerShape))
                    .border(1.dp, LightRed_Palette, RoundedCornerShape(roundedCornerShape))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    "@${profile.name}: ${post.description}",
                    color = Color.White,
                    softWrap = true,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
