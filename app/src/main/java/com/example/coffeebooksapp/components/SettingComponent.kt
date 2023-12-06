package com.example.coffeebooksapp.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingComponent() {
    val context = LocalContext.current
    Column {
        ListItem(
            headlineText = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        startOssLicensesMenuActivity(context)
                    }
                ) {
                    Text(text = "Setting")
                }
            },
        )

    }
}

fun startOssLicensesMenuActivity(context: Context) {
    try {
        val intent = Intent(context, OssLicensesMenuActivity::class.java)
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("Error", e.toString())
    }

}