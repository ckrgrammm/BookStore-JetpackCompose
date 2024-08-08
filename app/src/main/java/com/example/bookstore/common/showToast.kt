package com.example.bookstore.common

import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showToast(scope: CoroutineScope, scaffoldState: ScaffoldState, msg: String) {
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(msg)
    }
}

