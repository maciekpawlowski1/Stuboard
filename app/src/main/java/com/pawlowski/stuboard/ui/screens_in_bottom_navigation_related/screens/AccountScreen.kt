package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.pawlowski.stuboard.ui.theme.Green

@Composable
fun AccountScreen(onNavigateToLoginScreen: () -> Unit = {})
{
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Account")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter)
        {
            Button(
                modifier = Modifier.padding(bottom = 80.dp),
                onClick =
            {
                /* TODO: move to ViewModel */
                FirebaseAuth.getInstance().signOut()
                onNavigateToLoginScreen.invoke()
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
                Text(text = "Logout", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview()
{
    AccountScreen()
}