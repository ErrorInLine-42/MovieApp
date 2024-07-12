package com.example.movieapp.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.movieapp.R
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies
import com.example.movieapp.widgets.MovieRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, movieID: String?) {
    val newMovieList = getMovies().filter { it.id == movieID }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.shimmering))

    if (newMovieList.isNotEmpty()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Movies")
                        }
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 75.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MovieRow(movie = newMovieList[0])
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Text(text = "Movie Images")
                LazyRow {
                    items(newMovieList[0].images) { image ->
                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(250.dp),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current).data(image)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                            )

                            when (painter.state) {
                                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                                    LottieAnimation(composition = composition, iterations = Int.MAX_VALUE, modifier = Modifier.fillMaxWidth().fillMaxHeight())
                                }
                                else -> {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Movie Image",
                                        modifier = Modifier.size(250.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Text("Movie not found", modifier = Modifier.padding(16.dp))
    }
}
