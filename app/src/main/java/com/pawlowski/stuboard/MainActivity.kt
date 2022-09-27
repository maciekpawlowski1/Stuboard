package com.pawlowski.stuboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.pawlowski.stuboard.presentation.activity.MainViewModel
import com.pawlowski.stuboard.ui.navigation.LoginRootComposable
import com.pawlowski.stuboard.ui.theme.StuboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val activityViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StuboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val startingLoginState by remember {
                        mutableStateOf(activityViewModel.getLoginState())
                    }
                    LoginRootComposable(startingLoginState)
                    //RootComposable()

                }
            }
        }

/*        try {
            val apiKey: String = System.getenv("APP_CENTER_KEY")?:getString(R.string.appCenterKey)
            AppCenter.start(application, apiKey, Analytics::class.java, Crashes::class.java)
        }
        catch (e: Exception)
        {}*/

    }
}