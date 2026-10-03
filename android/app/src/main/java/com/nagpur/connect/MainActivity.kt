package com.nagpur.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nagpur.connect.ui.navigation.AppNavGraph
import com.nagpur.connect.ui.theme.NagpurConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NagpurConnectTheme {
                AppNavGraph()
            }
        }
    }
}
