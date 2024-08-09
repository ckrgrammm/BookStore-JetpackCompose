package com.example.bookstore.ui.screen.UserScreen.Screen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.bookstore.R
import com.example.bookstore.common.CustomDialog
import com.example.bookstore.data.model.User
import com.example.bookstore.ui.screen.UserScreen.ViewModel.RegisterViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavHostController)
{
    val registerViewModel: RegisterViewModel = hiltViewModel()

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val isLoggedIn by registerViewModel.isLoggedIn.collectAsState()
    val errorMessage by registerViewModel.errorMessage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    RegisterContent(
            userName = userName,
            onUserNameChange = { userName = it.trim() },
            password = password,
            onPasswordChange = { password = it },
            rePassword = rePassword,
            onRePasswordChange = { rePassword = it },
            onRegisterClick = {
                val user = User(
                        id = 0,
                        firstName = null,
                        lastName = null,
                        userName = userName,
                        email = null,
                        contactNumber = null,
                        password = password,
                        rePassword = rePassword,
                        userStatus = null
                )
                coroutineScope.launch {
                    when
                    {
                        !user.isPasswordValid() ->
                        {
                            dialogTitle = "Error"
                            dialogMessage = "Passwords do not match."
                            showDialog = true
                        }

                        !user.areFieldsNotEmpty() ->
                        {
                            dialogTitle = "Error"
                            dialogMessage = "Please fill up all the required fields."
                            showDialog = true
                        }

                        !user.isUsernameValid() ->
                        {
                            dialogTitle = "Error"
                            dialogMessage = "Username should be between 4 - 10 characters."
                            showDialog = true
                        }

                        registerViewModel.isUsernameTaken(userName) ->
                        {
                            dialogTitle = "Error"
                            dialogMessage = "Username is already taken."
                            showDialog = true
                        }

                        else ->
                        {
                            registerViewModel.register(user)
                            dialogTitle = "Success"
                            dialogMessage = "Registered Successfully!"
                            showDialog = true
                        }
                    }
                }
            },
            errorMessage = errorMessage,
            showDialog = showDialog,
            dialogTitle = dialogTitle,
            dialogMessage = dialogMessage,
            onDismissDialog = { showDialog = false },
            onConfirmDialog = {
                showDialog = false
                if (dialogTitle == "Success")
                {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterContent(
        userName: String,
        onUserNameChange: (String) -> Unit,
        password: String,
        onPasswordChange: (String) -> Unit,
        rePassword: String,
        onRePasswordChange: (String) -> Unit,
        onRegisterClick: () -> Unit,
        errorMessage: String?,
        showDialog: Boolean,
        dialogTitle: String,
        dialogMessage: String,
        onDismissDialog: () -> Unit,
        onConfirmDialog: () -> Unit
)
{
    Scaffold {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color(0xFFF0F0F0)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                    painter = painterResource(id = R.drawable.ic_register),
                    contentDescription = "Register Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                    text = "Book Store Register",
                    style = MaterialTheme.typography.h4.copy(color = Color(0xFF6200EE)),
                    modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 8.dp
            ) {
                Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                ) {
                    OutlinedTextField(
                            value = userName,
                            onValueChange = onUserNameChange,
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = rePassword,
                            onValueChange = onRePasswordChange,
                            label = { Text("Re-enter Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                            onClick = onRegisterClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Register", fontSize = 18.sp, color = Color.White)
                    }

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = it,
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            if (showDialog)
            {
                CustomDialog(
                        title = dialogTitle,
                        message = dialogMessage,
                        onDismiss = onDismissDialog,
                        onConfirm = onConfirmDialog
                )
            }
        }
    }
}
