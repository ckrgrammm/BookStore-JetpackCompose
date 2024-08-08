package com.example.bookstore.ui.screen.HomeScreen

import android.annotation.SuppressLint
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
fun HomeScreen(navController: NavHostController, bookDao: BookDao, currentUser: User) {
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
                        title = { Text("SS Book Store") },
                        backgroundColor = MaterialTheme.colors.primary
                )
            }
    ) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
        ) {
            LazyColumn(
                    modifier = Modifier.fillMaxSize()
            ) {
                items(books, key = { it.id }) { book ->
                    val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                    coroutineScope.launch {
                                        bookDao.deleteBook(book)
                                        books = bookDao.getAllBooksSortedByDate()
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
                                        contentAlignment = when (direction) {
                                            DismissDirection.StartToEnd -> Alignment.CenterStart
                                            DismissDirection.EndToStart -> Alignment.CenterEnd
                                        }
                                ) {
                                    Icon(icon, contentDescription = "Delete Icon", tint = Color.White)
                                }
                            },
                            dismissContent = {
                                BookItem(book, navController, currentUser, bookDao, books) { updatedBooks ->
                                    books = updatedBooks
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = {
                        UserSession.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
            ) {
                Text("Logout", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookItem(
        book: Book,
        navController: NavHostController,
        currentUser: User,
        bookDao: BookDao,
        books: List<Book>,
        onBooksUpdate: (List<Book>) -> Unit
) {
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
                        onClick = { navController.navigate("bookDetail/${book.id}") },
                        onLongClick = { showMenu = true }
                ),
            elevation = 2.dp
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
                Text(text = book.title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = book.author, style = MaterialTheme.typography.body2)
            }

            if (showMenu && book.bookOwnerId == currentUser.id) {
                DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        navController.navigate("editBook/${book.id}")
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

    if (showDialog) {
        CustomDialog(
                title = "Confirm Delete",
                message = "Are you sure you want to delete this book?",
                onDismiss = { showDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        bookDao.deleteBook(book)
                        val updatedBooks = bookDao.getAllBooksSortedByDate()
                        onBooksUpdate(updatedBooks)
                    }
                    showDialog = false
                }
        )
    }
}
