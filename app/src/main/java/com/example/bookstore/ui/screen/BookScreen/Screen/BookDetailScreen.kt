package com.example.bookstore.ui.screen.BookScreen.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.model.Book
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailScreen(navController: NavHostController, bookId: Int, bookDao: BookDao) {
    val coroutineScope = rememberCoroutineScope()
    var book by remember { mutableStateOf<Book?>(null) }

    LaunchedEffect(bookId) {
        coroutineScope.launch {
            book = bookDao.getBookById(bookId)
        }
    }

    book?.let { book ->
        val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }
        val dateString = book.dateOfRegister?.let { dateFormat.format(it) } ?: "Unknown date"
        val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(book.imageUri)
                    .build()
        )

        Scaffold(
                topBar = {
                    TopAppBar(
                            title = { Text(book.title) },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                    )
                }
        ) {
            Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colors.background)
                        .verticalScroll(rememberScrollState())

            ) {
                Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                        ) {
                            Image(
                                    painter = painter,
                                    contentDescription = "Book Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.Gray)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                    text = book.title,
                                    style = MaterialTheme.typography.h4.copy(fontSize = 26.sp),
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                    text = "By: ${book.author}",
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                            book.publishedYear?.let { year ->
                                Text(
                                        text = "Published Year: $year",
                                        style = MaterialTheme.typography.subtitle1,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Text(
                                    text = "Registered on: $dateString",
                                    style = MaterialTheme.typography.body2.copy(color = Color.Gray),
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            book.description?.let { desc ->
                                Text(
                                        text = desc,
                                        style = MaterialTheme.typography.body1.copy(lineHeight = 22.sp),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }

                GradientButton(
                        text = "Back",
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp)
                            .navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
fun GradientButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
            ),
            shape = RoundedCornerShape(50)
    ) {
        Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                            MaterialTheme.colors.primary,
                                            MaterialTheme.colors.secondary
                                    )
                            ),
                            shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 18.sp, color = Color.White)
        }
    }
}
