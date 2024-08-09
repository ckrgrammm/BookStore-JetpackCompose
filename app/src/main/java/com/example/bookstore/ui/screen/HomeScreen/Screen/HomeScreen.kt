package com.example.bookstore.ui.screen.HomeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.common.UserSession
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.model.Book
import com.example.bookstore.data.model.User
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
        navController: NavHostController,
        bookDao: BookDao,
        currentUser: User
)
{
    val coroutineScope = rememberCoroutineScope()
    var books by remember { mutableStateOf(emptyList<Book>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            books = bookDao.getAllBooksSortedByDate()
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("SS Book Store", style = MaterialTheme.typography.h6) },
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White
                )
            }
    ) {
        HomeContent(
                books = books,
                currentUser = currentUser,
                onBookDeleted = { updatedBooks ->
                    books = updatedBooks
                },
                onBookEdited = { bookId ->
                    navController.navigate("editBook/$bookId")
                },
                onBookSelected = { bookId ->
                    navController.navigate("bookDetail/$bookId")
                },
                bookDao = bookDao
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
        books: List<Book>,
        currentUser: User,
        onBookDeleted: (List<Book>) -> Unit,
        onBookEdited: (Int) -> Unit,
        onBookSelected: (Int) -> Unit,
        bookDao: BookDao
)
{
    val coroutineScope = rememberCoroutineScope()

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
    ) {
        LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
        ) {
            items(books, key = { it.id }) { book ->
                val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart)
                            {
                                coroutineScope.launch {
                                    bookDao.deleteBook(book)
                                    val updatedBooks = bookDao.getAllBooksSortedByDate()
                                    onBookDeleted(updatedBooks)
                                }
                            }
                            true
                        }
                )
                SwipeToDismiss(
                        state = dismissState,
                        directions = if (book.bookOwnerId == currentUser.id)
                            setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
                        else
                            emptySet(),
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val icon = Icons.Default.Delete
                            val color = Color.Red
                            Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = when (direction)
                                    {
                                        DismissDirection.StartToEnd -> Alignment.CenterStart
                                        DismissDirection.EndToStart -> Alignment.CenterEnd
                                    }
                            ) {
                                Icon(icon, contentDescription = "Delete Icon", tint = Color.White)
                            }
                        },
                        dismissContent = {
                            BookItem(
                                    book = book,
                                    currentUser = currentUser,
                                    onBookSelected = onBookSelected,
                                    onBookEdited = onBookEdited,
                                    onBookDeleted = onBookDeleted,
                                    bookDao = bookDao
                            )
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookItem(
        book: Book,
        currentUser: User,
        onBookSelected: (Int) -> Unit,
        onBookEdited: (Int) -> Unit,
        onBookDeleted: (List<Book>) -> Unit,
        bookDao: BookDao
)
{
    val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(book.imageUri)
                .build()
    )

    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .combinedClickable(
                        onClick = { onBookSelected(book.id) },
                        onLongClick = { showMenu = true }
                ),
            elevation = 4.dp
    ) {
        Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                    painter = painter,
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
            )
            Column(
                    modifier = Modifier.weight(1f)
            ) {
                Text(text = book.title, style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = book.author, style = MaterialTheme.typography.body2, color = Color.Gray)
            }

            if (showMenu && book.bookOwnerId == currentUser.id)
            {
                DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        onBookEdited(book.id)
                        showMenu = false
                    }) {
                        Text("Edit")
                    }
                    DropdownMenuItem(onClick = {
                        showDialog = true
                        showMenu = false
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    if (showDialog)
    {
        CustomDialog(
                title = "Confirm Delete",
                message = "Are you sure you want to delete this book?",
                onDismiss = { showDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        bookDao.deleteBook(book)
                        val updatedBooks = bookDao.getAllBooksSortedByDate()
                        onBookDeleted(updatedBooks)
                    }
                    showDialog = false
                }
        )
    }
}

