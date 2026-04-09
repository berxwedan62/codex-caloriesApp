package com.lokma.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lokma.app.ui.navigation.LokmaApp
import com.lokma.app.ui.theme.LokmaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LokmaTheme {
                LokmaApp()
            }
        }
    }
}
