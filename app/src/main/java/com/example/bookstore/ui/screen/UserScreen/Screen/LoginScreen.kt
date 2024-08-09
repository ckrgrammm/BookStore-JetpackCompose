package com.example.bookstore.ui.screen.UserScreen.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
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
import com.example.bookstore.ui.screen.UserScreen.ViewModel.LoginViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
        navController: NavHostController,
        onLoginSuccess: (User) -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val isLoginSuccessful by loginViewModel.isLoginSuccessful.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        if (isLoginSuccessful) {
            LaunchedEffect(isLoginSuccessful) {
                loginViewModel.currentUser?.let { onLoginSuccess(it) }
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color(0xFFF5F5F5)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 8.dp
            ) {
                Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                ) {
                    Image(
                            painter = painterResource(id = R.drawable.ic_book),
                            contentDescription = "Login Logo",
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                            text = "SS Book Store",
                            style = MaterialTheme.typography.h4.copy(color = Color(0xFF6200EE)),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it.trim() },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                            onClick = {
                                loginViewModel.login(userName, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Login", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                            text = "Not a member? Register here",
                            color = Color(0xFF6200EE),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    navController.navigate("register")
                                }
                    )
                }
            }
        }

        if (!isLoginSuccessful && errorMessage != null) {
            LaunchedEffect(errorMessage) {
                dialogTitle = "Error"
                dialogMessage = errorMessage ?: "Invalid Credentials, Try again!"
                showDialog = true
            }
        }

        if (showDialog) {
            CustomDialog(
                    title = dialogTitle,
                    message = dialogMessage,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        showDialog = false
                    }
            )
        }

    }
}

