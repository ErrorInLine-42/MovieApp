package com.example.movieapp.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.movieapp.R
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies

@Preview
@Composable
fun MovieRow(movie: Movie = getMovies()[0], onItemClicked: (String) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.shimmering))
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(movie.poster)
            .apply(block = fun ImageRequest.Builder.() {
                transformations(CircleCropTransformation())
                crossfade(true)
            }).build()
    )

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(movie.id) },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .size(100.dp),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 5.dp
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                        LottieAnimation(composition = composition, iterations = Int.MAX_VALUE)
                    }
                    else -> {
                        Image(
                            painter = painter,
                            contentDescription = movie.title,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Director: ${movie.director}", style = MaterialTheme.typography.labelSmall)
                Text(text = "Released: ${movie.year}", style = MaterialTheme.typography.labelSmall)

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Divider()
                        Text(buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp
                                )
                            ) {
                                append("Plot: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Light
                                )
                            ) {
                                append(movie.plot)
                            }
                        }, modifier = Modifier.padding(6.dp))
                        Divider(modifier = Modifier.padding(5.dp))
                        Text(text = "Genre: ${movie.genre}", style = MaterialTheme.typography.labelSmall)
                        Text(text = "Actor: ${movie.actor}", style = MaterialTheme.typography.labelSmall)
                        Text(text = "Rating: ${movie.rating}", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { expanded = !expanded },
                    tint = Color.DarkGray
                )
            }
        }
    }
}
