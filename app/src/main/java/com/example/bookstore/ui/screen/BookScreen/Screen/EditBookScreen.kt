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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.R
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.common.saveImageToInternalStorage
import com.example.bookstore.data.dao.BookDao
import com.example.bookstore.data.dao.IBookDao
import com.example.bookstore.data.di.ActualBookDao
import com.example.bookstore.data.model.Book
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditBookScreen(
        navController: NavHostController,
        @ActualBookDao bookDao: IBookDao,
        bookId: Int
)
{
    val coroutineScope = rememberCoroutineScope()
    var book by remember { mutableStateOf<Book?>(null) }

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var publishedYear by remember { mutableStateOf("") }
    var bookImage by remember { mutableStateOf<String?>(null) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(bookId) {
        coroutineScope.launch {
            val loadedBook = bookDao.getBookById(bookId)
            book = loadedBook
            loadedBook?.let {
                title = it.title
                author = it.author
                description = it.description ?: ""
                publishedYear = it.publishedYear?.toString() ?: ""
                bookImage = it.imageUri
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(context, it)
            if (savedPath != null)
            {
                bookImage = savedPath
            }
            else
            {
                dialogTitle = "Error"
                dialogMessage = "Failed to save image"
                showDialog = true
            }
        }
    }

    Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                TopAppBar(
                        title = { Text("Edit Book") },
                        backgroundColor = MaterialTheme.colors.primary
                )
            }
    ) {
        book?.let {
            EditBookContent(
                    title = title,
                    onTitleChange = { title = it },
                    author = author,
                    onAuthorChange = { author = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    publishedYear = publishedYear,
                    onPublishedYearChange = { publishedYear = it },
                    bookImage = bookImage,
                    onImageClick = { imagePickerLauncher.launch("image/*") },
                    onSaveClick = {
                        val year = publishedYear.toIntOrNull()
                        if (year == null)
                        {
                            dialogTitle = "Error"
                            dialogMessage = "Invalid published year"
                            showDialog = true
                        }
                        else
                        {
                            val updatedBook = it.copy(
                                    title = title,
                                    author = author,
                                    description = description,
                                    publishedYear = year,
                                    imageUri = bookImage
                            )

                            coroutineScope.launch {
                                if (!updatedBook.isTitleValid())
                                {
                                    dialogTitle = "Error"
                                    dialogMessage = "Title is required"
                                    showDialog = true
                                }
                                else if (!updatedBook.isAuthorValid())
                                {
                                    dialogTitle = "Error"
                                    dialogMessage = "Author is required"
                                    showDialog = true
                                }
                                else if (!updatedBook.isPublishedYearValid())
                                {
                                    dialogTitle = "Error"
                                    dialogMessage = "Published year must be after 1950"
                                    showDialog = true
                                }
                                else
                                {
                                    bookDao.updateBook(updatedBook)
                                    dialogTitle = "Success"
                                    dialogMessage = "Book updated successfully!"
                                    showDialog = true
                                    Log.d("EditBookScreen", "Dialog Msg showed $dialogTitle")
                                }
                            }
                        }
                    }
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
                                onClick = {
                                    showDialog = false
                                    if (dialogTitle == "Success")
                                    {
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

@Composable
fun EditBookContent(
        title: String,
        onTitleChange: (String) -> Unit,
        author: String = "",
        onAuthorChange: (String) -> Unit,
        description: String = "",
        onDescriptionChange: (String) -> Unit,
        publishedYear: String = "",
        onPublishedYearChange: (String) -> Unit,
        bookImage: String?,
        onImageClick: () -> Unit,
        onSaveClick: () -> Unit
)
{
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
                        .clickable { onImageClick() }
                        .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
            )
        } ?: Image(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = "Book Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
                    .clickable { onImageClick() }
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = author,
                onValueChange = onAuthorChange,
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description of this book") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = publishedYear,
                onValueChange = onPublishedYearChange,
                label = { Text("Published Year of this book") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
        ) {
            Text("Save Changes")
        }
    }
}


