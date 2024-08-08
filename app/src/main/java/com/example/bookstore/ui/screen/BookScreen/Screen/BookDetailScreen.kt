package com.example.bookstore.ui.screen.BookScreen.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                            backgroundColor = MaterialTheme.colors.primary
                    )
                }
        ) {
            Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
            ) {
                Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 70.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Image(
                                painter = painter,
                                contentDescription = "Book Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(bottom = 16.dp)
                                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                        )

                        Text(
                                text = book.title,
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                                text = "Author: ${book.author}",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(bottom = 8.dp)
                        )
                        book.description?.let { desc ->
                            Text(
                                    text = desc,
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        book.publishedYear?.let { year ->
                            Text(
                                    text = "Published Year: $year",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Text(
                                text = "Registered: $dateString",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                Button(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Back", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}
