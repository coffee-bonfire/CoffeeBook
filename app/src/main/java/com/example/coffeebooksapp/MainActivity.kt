package com.example.coffeebooksapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.coffeebooksapp.components.BottomNavigationBar
import com.example.coffeebooksapp.components.EditDialog
import com.example.coffeebooksapp.components.Navigations
import com.example.coffeebooksapp.ui.theme.CoffeeBooksAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CoffeeBooksAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(navController = navController)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    bookViewModel: BookViewModel = hiltViewModel(),
    navController: NavHostController
){
    val context = LocalContext.current

    if (bookViewModel.isShowDialog){
        EditDialog(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "コーヒー図鑑"
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier) {
                BottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
        FloatingActionButton(
            onClick = {
                bookViewModel.isShowDialog = true
            })
        {
            Icon(imageVector = Icons.Default.Add, contentDescription = "図鑑新規作成" )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp,
                    50.dp,
                    0.dp,
                    innerPadding.calculateBottomPadding()
                )
            )
        ) {
            Navigations(navController = navController)
        }
    }
}