package com.dandanbiyori.coffeebooksapp.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dandanbiyori.coffeebooksapp.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingComponent(
    navController: NavController,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_setting),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black,
                    )
                },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780))
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier,
                containerColor = Color(0xFFD3A780),
                contentColor = Color(0xFFD3A780)
            ) {
                BottomNavigationBar(
                    navController = navController,
                    color = 0xFFD3A780
                )
            }
        },
    ) {
        // innerPadding は、Scaffold コンポーネントの中で他のコンポーネントを配置する際に、その内側の余白を表す
            innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp,
                    60.dp,
                    0.dp,
                    innerPadding.calculateBottomPadding()
                )
            )
        ) {
            Column {
                Column(
                    modifier = Modifier.clickable {
                        startOssLicensesMenuActivity(context)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "バージョン情報",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Left,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "version_info",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }
                Column(
                    modifier = Modifier.clickable {
                        // todo クリック時にヘルプ画面を表示する
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "ヘルプ",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Left,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "version_info",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(30.dp))

                    }
                }
            }
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