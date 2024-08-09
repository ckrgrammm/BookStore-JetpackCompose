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
fun BookDetailScreen(
        navController: NavHostController,
        bookDao: BookDao,
        bookId: Int
)
{
    val coroutineScope = rememberCoroutineScope()
    var book by remember { mutableStateOf<Book?>(null) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        coroutineScope.launch {
            book = bookDao.getBookById(bookId)
        }
    }

    Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                TopAppBar(
                        title = { Text("Book Details") },
                        backgroundColor = MaterialTheme.colors.primary
                )
            }
    ) {
        book?.let {
            BookDetailContent(
                    book = it,
                    onBackClick = { navController.navigateUp() }
            )
        }

        if (showDialog)
        {
            AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(dialogTitle) },
                    text = { Text(dialogMessage) },
                    confirmButton = {
                        Button(
                                onClick = { showDialog = false }
                        ) {
                            Text("OK")
                        }
                    }
            )
        }
    }
}

@Composable
fun BookDetailContent(
        book: Book,
        onBackClick: () -> Unit
)
{
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val dateString = book.dateOfRegister?.let { dateFormat.format(it) } ?: "Unknown date"

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
    ) {
        Image(
                painter = rememberAsyncImagePainter(book.imageUri),
                contentDescription = "Book Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface)
        )

        Text(
                text = book.title,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
                text = "Author: ${book.author}",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
        )

        book.publishedYear?.let {
            Text(
                    text = "Published Year: $it",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Text(
                text = "Registered on: $dateString",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
        )

        book.description?.let {
            Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))



        Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
        ) {
            Text("Back")
        }
    }
}


