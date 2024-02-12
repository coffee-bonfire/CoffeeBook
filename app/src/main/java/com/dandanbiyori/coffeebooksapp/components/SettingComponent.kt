package com.dandanbiyori.coffeebooksapp.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun SettingComponent() {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .clickable {
                    startOssLicensesMenuActivity(context)
                }
        ) {
            Text(

                text = "バージョン情報",
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "version_info" )
            Spacer(modifier = Modifier.width(30.dp))

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