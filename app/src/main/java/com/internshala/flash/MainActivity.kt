package com.internshala.flash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.internshala.flash.ui.FlashApp
import com.internshala.flash.ui.theme.Flash_GroceryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Flash_GroceryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    FlashApp()
                }
//
            }
        }
    }
}

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Flash_GroceryAppTheme {
            FlashApp()
        }
    }