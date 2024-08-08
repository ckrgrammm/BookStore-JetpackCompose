package com.example.bookstore.ui.screen.BookScreen.Screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.R
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.common.saveImageToInternalStorage
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.model.Book
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditBookScreen(navController: NavHostController, bookDao: BookDao, bookId: Int) {
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

    book?.let { currentBook ->
        var title by remember { mutableStateOf(currentBook.title) }
        var author by remember { mutableStateOf(currentBook.author) }
        var description by remember { mutableStateOf(currentBook.description ?: "") }
        var publishedYear by remember { mutableStateOf(currentBook.publishedYear?.toString() ?: "") }
        var bookImage by remember { mutableStateOf(currentBook.imageUri ?: "") }

        val scaffoldState = rememberScaffoldState()
        val context = LocalContext.current

        val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val savedPath = saveImageToInternalStorage(context, it)
                if (savedPath != null) {
                    bookImage = savedPath
                } else {
                    coroutineScope.launch {
                        dialogTitle = "Error"
                        dialogMessage = "Failed to save image"
                        showDialog = true
                    }
                }
            }
        }

        Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                            title = { Text("Edit Book") },
                            backgroundColor = MaterialTheme.colors.primary
                    )
                }
        ) {
            Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    bookImage?.let {
                        Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "Book Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(bottom = 16.dp)
                                    .clickable { imagePickerLauncher.launch("image/*") }
                                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                        )
                    } ?: Image(
                            painter = painterResource(id = R.drawable.ic_book),
                            contentDescription = "Book Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(bottom = 16.dp)
                                .clickable { imagePickerLauncher.launch("image/*") }
                                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = author,
                            onValueChange = { author = it },
                            label = { Text("Author") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description of this book") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = publishedYear,
                            onValueChange = { publishedYear = it },
                            label = { Text("Published Year of this book") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                    )
                }

                Button(
                        onClick = {
                            coroutineScope.launch {
                                val updatedBook = currentBook.copy(
                                        title = title,
                                        author = author,
                                        description = description,
                                        publishedYear = publishedYear.toIntOrNull(),
                                        imageUri = bookImage,
                                        dateOfRegister = currentBook.dateOfRegister,
                                        bookOwnerId = currentBook.bookOwnerId
                                )
                                if (!updatedBook.isTitleValid()) {
                                    dialogTitle = "Error"
                                    dialogMessage = "Title is required"
                                    showDialog = true
                                } else if (!updatedBook.isAuthorValid()) {
                                    dialogTitle = "Error"
                                    dialogMessage = "Author is required"
                                    showDialog = true
                                } else if (!updatedBook.isPublishedYearValid()) {
                                    dialogTitle = "Error"
                                    dialogMessage = "Valid Published Year is required"
                                    showDialog = true
                                } else {
                                    bookDao.updateBook(updatedBook)
                                    dialogTitle = "Success"
                                    dialogMessage = "Book updated successfully!"
                                    showDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Update", fontSize = 18.sp, color = Color.White)
                }
            }

            if (showDialog) {
                CustomDialog(
                        title = dialogTitle,
                        message = dialogMessage,
                        onDismiss = { showDialog = false },
                        onConfirm = {
                            showDialog = false
                            if (dialogTitle == "Success") {
                                navController.popBackStack()
                            }
                        }
                )
            }
        }
    }
}
