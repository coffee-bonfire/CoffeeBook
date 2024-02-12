package com.dandanbiyori.coffeebooksapp.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

@Preview
@Composable
private fun BookItemDetailView(

) {
    Box(
        Modifier.fillMaxSize()
    ) {
        // TODO Item Deteil layout
        BookItemDetailContent()

        // TODO Tool bar back botton
    }

}

@Composable
fun BookItemDetailContent(){
    Column() {
        ConstraintLayout {
            // image

            // title

            // text

        }
    }
}