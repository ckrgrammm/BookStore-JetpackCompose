package com.example.bookstore.ui.screen.BookScreen.Screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.bookstore.data.model.User
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddBookScreen(navController: NavHostController, bookDao: BookDao, currentUser: User) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var publishedYear by remember { mutableStateOf("") }
    var bookImage by remember { mutableStateOf<String?>(null) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
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
                        title = { Text("Add New Book") },
                        backgroundColor = MaterialTheme.colors.primary
                )
            }
    ) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
            Spacer(modifier = Modifier.height(16.dp))
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
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = {
                        val newBook = Book(
                                title = title,
                                author = author,
                                description = description,
                                publishedYear = publishedYear.toIntOrNull(),
                                dateOfRegister = Date(),
                                bookOwnerId = currentUser.id,
                                imageUri = bookImage
                        )
                        if (!newBook.isTitleValid()) {
                            dialogTitle = "Error"
                            dialogMessage = "Title is required"
                            showDialog = true
                            Log.d("AddBookScreen", "Title is invalid")
                        } else if (!newBook.isAuthorValid()) {
                            dialogTitle = "Error"
                            dialogMessage = "Author is required"
                            showDialog = true
                        } else {
                            coroutineScope.launch {
                                bookDao.insertBook(newBook)
                                dialogTitle = "Success"
                                dialogMessage = "Book added successfully!"
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add Book")
            }

            if (showDialog) {
                AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(dialogTitle) },
                        text = { Text(dialogMessage) },
                        confirmButton = {
                            Button(
                                    onClick = {
                                        showDialog = false
                                        if (dialogTitle == "Success") {
                                            navController.navigate("home")
                                        }
                                    }
                            ) {
                                Text("OK")
                            }
                        }
                )
            }
        }
    }
}

