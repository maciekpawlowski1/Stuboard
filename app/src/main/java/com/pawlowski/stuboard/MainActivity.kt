package com.pawlowski.stuboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.pawlowski.stuboard.ui.navigation.LoginRootComposable
import com.pawlowski.stuboard.ui.navigation.RootComposable
import com.pawlowski.stuboard.ui.theme.StuboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StuboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //RootComposable()
                    LoginRootComposable()
                }
            }
        }

        try {
            val apiKey: String = System.getenv("APP_CENTER_KEY")?:getString(R.string.appCenterKey)
            AppCenter.start(application, apiKey, Analytics::class.java, Crashes::class.java)
        }
        catch (e: Exception)
        {}

    }
}