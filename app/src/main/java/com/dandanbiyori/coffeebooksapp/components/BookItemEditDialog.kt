package com.dandanbiyori.coffeebooksapp.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dandanbiyori.coffeebooksapp.BookItemViewModel

@Composable
fun BookItemEditDialog(
    context: Context,
    bookItemViewModel: BookItemViewModel = hiltViewModel(),
) {
    AlertDialog(
        onDismissRequest = { bookItemViewModel.isShowDialog = false },
        confirmButton = { /*TODO*/ },
        text = {
            Text(text = "")
        }
    )

}