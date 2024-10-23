package com.dandanbiyori.coffeebooksapp.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.dandanbiyori.coffeebooksapp.NavigationItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyView(
    onClickBack: () -> Unit
){
    // WebViewを表示する（githubサイト）
    val language = Locale.getDefault().language // 言語設定を取得
    val url = when (language) { // 言語設定に基づいてURLを決定
        "ja" -> "https://coffee-bonfire.github.io/coffeebooks.io/" // デフォルトURL
        else -> "https://coffee-bonfire.github.io/coffeebooks.io/en/index-en.html"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD3A780)),

            )
        }

    ) {
        padding ->
        AndroidView(
            factory = {
                    context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            },
            modifier = Modifier.padding(padding)
        )
    }
}