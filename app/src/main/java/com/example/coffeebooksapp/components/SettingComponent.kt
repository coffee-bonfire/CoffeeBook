package com.example.coffeebooksapp.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingComponent() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 20.dp).clickable {
                startOssLicensesMenuActivity(context)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "バージョン情報",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

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