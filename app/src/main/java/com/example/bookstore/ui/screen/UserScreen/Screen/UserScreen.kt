package com.example.bookstore.ui.screen.UserScreen.Screen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.R
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.common.UserSession
import com.example.bookstore.common.saveImageToInternalStorage
import com.example.bookstore.data.dao.UserDao
import com.example.bookstore.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserScreenWrapper : ComponentActivity() {
    @Inject
    lateinit var userDao: UserDao

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentUser = UserSession.getCurrentUser()
            val navController = rememberNavController()

            currentUser?.let {
                UserScreen(navController, userDao, it)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserScreen(navController: NavHostController, userDao: UserDao, currentUser: User) {
    var firstName by remember { mutableStateOf(currentUser.firstName ?: "") }
    var lastName by remember { mutableStateOf(currentUser.lastName ?: "") }
    var email by remember { mutableStateOf(currentUser.email ?: "") }
    var contactNumber by remember { mutableStateOf(currentUser.contactNumber ?: "") }
    var userProfile by remember { mutableStateOf(currentUser.userProfile) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var navigateToLogin by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(context, it)
            if (savedPath != null) {
                userProfile = savedPath
            } else {
                dialogTitle = "Error"
                dialogMessage = "Failed to save image"
                showDialog = true
            }
        }
    }

    Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                        title = { Text("User Profile") }
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
                            .clickable { imagePickerLauncher.launch("image/*") }
                )
            } ?: Image(
                    painter = painterResource(id = R.drawable.ic_default_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clip(CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }
            )

            Text(text = "Edit Profile", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text("Contact Number") },
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = {
                        coroutineScope.launch {
                            val updatedUser = currentUser.copy(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    contactNumber = contactNumber,
                                    userProfile = userProfile
                            )
                            if (updatedUser.isEmailValid() && updatedUser.isContactNumberValid()) {
                                userDao.updateUser(updatedUser)
                                UserSession.login(updatedUser)
                                dialogTitle = "Success"
                                dialogMessage = "Profile updated successfully."
                                showDialog = true
                            } else {
                                dialogTitle = "Error"
                                dialogMessage = "Please fill up all the required fields correctly."
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                    onClick = {
                        UserSession.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
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
