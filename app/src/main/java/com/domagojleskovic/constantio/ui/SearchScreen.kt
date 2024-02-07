package com.domagojleskovic.constantio.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.domagojleskovic.constantio.SearchViewModel
import com.domagojleskovic.constantio.ui.theme.Brownish_Palette
import com.domagojleskovic.constantio.ui.theme.DarkBlue_Palette

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
) {
    var searchText by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.observeAsState(initial = emptyList())

    /*
    DisposableEffect(Unit){
        onDispose {
            viewModel.clearSearchResults()
        }
    }
    */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brownish_Palette)
    ){
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.width(70.dp))
                            Text(
                                text = "Constantio", fontSize = 36.sp,
                                fontFamily = FontFamily.Cursive,
                                fontWeight = FontWeight.W700,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(70.dp))

                        }
                        Row {
                            TextField(
                                label = { Text("Search profiles") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.White
                                ),
                                textStyle = TextStyle(color = Color.White),
                                value = searchText,
                                onValueChange = { newText ->
                                    searchText = newText
                                    viewModel.searchProfiles(newText)
                                },
                            )
                        }

                    }
                }
            }
        }
        items(searchResults){profile ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                ){
                    AsyncImage(
                        model = profile.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, Brownish_Palette, CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = "@${profile.name}",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.W700,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}