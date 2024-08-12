package com.example.bookstore.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDialog(
        title: String,
        message: String,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
) {
    AlertDialog(
            modifier = Modifier.testTag("CustomDialog"),
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            text = {
                Text(text = message)
            },
            buttons = {
                Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm() }) {
                        Text("Confirm")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White,
            contentColor = Color.Black
    )
}
