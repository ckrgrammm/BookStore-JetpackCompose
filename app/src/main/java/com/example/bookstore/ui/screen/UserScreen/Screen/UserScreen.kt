package com.example.bookstore.ui.screen.UserScreen.Screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.R
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.common.UserSession
import com.example.bookstore.common.saveImageToInternalStorage
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.di.ActualBookDao
import com.example.bookstore.data.di.ActualUserDao
import com.example.bookstore.data.ipackage.IBookDao
import com.example.bookstore.data.ipackage.IUserDao
import com.example.bookstore.data.model.User
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserScreen(
        navController: NavHostController,
        @ActualUserDao userDao: IUserDao,
        currentUser: User)
{
    var firstName by remember { mutableStateOf(currentUser.firstName ?: "") }
    var lastName by remember { mutableStateOf(currentUser.lastName ?: "") }
    var email by remember { mutableStateOf(currentUser.email ?: "") }
    var contactNumber by remember { mutableStateOf(currentUser.contactNumber ?: "") }
    var userProfile by remember { mutableStateOf(currentUser.userProfile) }

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
            if (savedPath != null)
            {
                userProfile = savedPath
            }
            else
            {
                dialogTitle = "Error"
                dialogMessage = "Failed to save image"
                showDialog = true
            }
        }
    }

    UserScreenContent(
            firstName = firstName,
            onFirstNameChange = { firstName = it },
            lastName = lastName,
            onLastNameChange = { lastName = it },
            email = email,
            onEmailChange = { email = it },
            contactNumber = contactNumber,
            onContactNumberChange = { contactNumber = it },
            userProfile = userProfile,
            onProfileImageClick = { imagePickerLauncher.launch("image/*") },
            onUpdateClick = {
                coroutineScope.launch {
                    val updatedUser = currentUser.copy(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            contactNumber = contactNumber,
                            userProfile = userProfile
                    )
                    if (updatedUser.isEmailValid() && updatedUser.isContactNumberValid() && updatedUser.isFirstNameValid()
                        && updatedUser.isLastNameValid())
                    {
                        userDao.updateUser(updatedUser)
                        UserSession.login(updatedUser)
                        dialogTitle = "Success"
                        dialogMessage = "Profile updated successfully."
                        showDialog = true
                    }
                    else
                    {
                        dialogTitle = "Error"
                        dialogMessage = "Please fill up all the required fields correctly."
                        showDialog = true
                    }
                }
            },
            onLogoutClick = {
                UserSession.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            showDialog = showDialog,
            dialogTitle = dialogTitle,
            dialogMessage = dialogMessage,
            onDismissDialog = { showDialog = false },
            onConfirmDialog = {
                showDialog = false
                if (dialogTitle == "Success")
                {
                    navController.popBackStack()
                }
            }
    )
}

@Composable
fun UserScreenContent(
        firstName: String,
        onFirstNameChange: (String) -> Unit,
        lastName: String,
        onLastNameChange: (String) -> Unit,
        email: String,
        onEmailChange: (String) -> Unit,
        contactNumber: String,
        onContactNumberChange: (String) -> Unit,
        userProfile: String?,
        onProfileImageClick: () -> Unit,
        onUpdateClick: () -> Unit,
        onLogoutClick: () -> Unit,
        showDialog: Boolean,
        dialogTitle: String,
        dialogMessage: String,
        onDismissDialog: () -> Unit,
        onConfirmDialog: () -> Unit
) {
    Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                TopAppBar(
                        title = { Text("User Profile") },
                        actions = {
                            IconButton(onClick = { onLogoutClick() }) {
                                Icon(painterResource(id = R.drawable.ic_logout), contentDescription = "Logout",
                                        modifier = Modifier.size(75.dp) )
                            }
                        }
                )
            }
    ) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userProfile?.let {
                Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clip(CircleShape)
                            .clickable { onProfileImageClick() }
                )
            } ?: Image(
                    painter = painterResource(id = R.drawable.ic_default_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clip(CircleShape)
                        .clickable { onProfileImageClick() }
            )

            Text(text = "Edit Profile", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                    value = firstName,
                    onValueChange = onFirstNameChange,
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = lastName,
                    onValueChange = onLastNameChange,
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = contactNumber,
                    onValueChange = onContactNumberChange,
                    label = { Text("Contact Number") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = onUpdateClick,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }

        if (showDialog) {
            CustomDialog(
                    title = dialogTitle,
                    message = dialogMessage,
                    onDismiss = onDismissDialog,
                    onConfirm = onConfirmDialog
            )
        }
    }
}
