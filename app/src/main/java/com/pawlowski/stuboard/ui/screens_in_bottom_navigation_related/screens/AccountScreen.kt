package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.MviPreviewViewModel
import com.pawlowski.stuboard.presentation.account.AccountSingleEvent
import com.pawlowski.stuboard.presentation.account.AccountUiSate
import com.pawlowski.stuboard.presentation.account.AccountViewModel
import com.pawlowski.stuboard.presentation.account.IAccountViewModel
import com.pawlowski.stuboard.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.annotation.OrbitInternal

@Composable
fun AccountScreen(
    onNavigateToLoginScreen: () -> Unit = {},
    onNavigateToMyEventsScreen: () -> Unit = {},
    onNavigateToAdminPanel: () -> Unit = {},
    viewModel: IAccountViewModel = hiltViewModel<AccountViewModel>()
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val isAdminState = derivedStateOf { uiState.value.isAdmin }

    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when (event) {
                is AccountSingleEvent.NavigateToLogIn -> {
                    onNavigateToLoginScreen()
                }
                is AccountSingleEvent.NavigateToMyEventsScreen -> {
                    onNavigateToMyEventsScreen()
                }
                AccountSingleEvent.NavigateToAdminPanelScreen -> {
                    onNavigateToAdminPanel()
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Konto",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        AccountCard(
            modifier = Modifier.padding(horizontal = 10.dp),
            displayName = uiState.value.displayName,
            mail = uiState.value.mail,
            profilePhoto = uiState.value.profilePhoto
        )
        Spacer(modifier = Modifier.height(20.dp))
        OptionsCard(Modifier.padding(horizontal = 10.dp), onLogOutClick = {
            viewModel.signOut()
        }, onMyEventsClick = {
            viewModel.myEventsClick()
        },
            onMyAccountClick = {
                Toast.makeText(
                    context,
                    "Ekran mojego konta będzie dostępny wkrótce!",
                    Toast.LENGTH_LONG
                ).show()
            },
            onMyPreferencesClick = {
                Toast.makeText(
                    context,
                    "Ekran moje preferencje będzie dostępny wkrótce!",
                    Toast.LENGTH_LONG
                ).show()
            },
            showAdminPanel = {
                 isAdminState.value == true
            },
            onAdminPanelClick = {
                viewModel.adminPanelClick()
            })


    }
}

@Composable
fun OptionsCard(
    modifier: Modifier = Modifier,
    onMyEventsClick: () -> Unit,
    onMyAccountClick: () -> Unit,
    onMyPreferencesClick: () -> Unit,
    showAdminPanel: () -> Boolean,
    onAdminPanelClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 10.dp,
    ) {
        Column {

            Spacer(modifier = Modifier.height(5.dp))

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.account_icon,
                tittle = "Moje konto",
                label = "Dokonaj zmian na swoim koncie"
            ) {
                onMyAccountClick()
            }

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.guitar_icon,
                tittle = "Moje wydarzenia",
                label = "Dodawaj własne wydarzenia"
            ) {
                onMyEventsClick()
            }

            if (showAdminPanel())
            {
                OptionRow(
                    padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                    iconId = R.drawable.guitar_icon,
                    tittle = "Akceptowanie wydarzeń",
                    label = "Panel adminów"
                ) {
                    onAdminPanelClick()
                }
            }

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.lightning_icon,
                tittle = "Moje preferencje",
                label = "Dokonaj zmian w swoich preferencjach"
            ) {
                onMyPreferencesClick()
            }

            OptionRow(
                padding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
                iconId = R.drawable.sign_out_icon,
                tittle = "Wyloguj się",
                label = ""
            ) {
                onLogOutClick()
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun OptionRow(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    iconId: Int,
    tittle: String,
    label: String,
    onClick: () -> Unit
) {
    Row(modifier = modifier
        .clickable { onClick() }
        .padding(padding)
        .height(40.dp)
        .fillMaxWidth()

    ) {

        Card(
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
            elevation = 7.dp,
            backgroundColor = LightGray
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                painter = painterResource(id = iconId),
                contentDescription = "",
                tint = Green
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = tittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = MidGrey
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "")

    }
}

@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    displayName: String,
    mail: String,
    profilePhoto: Uri?,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(89.dp),
        elevation = 10.dp,
        backgroundColor = LightGreen
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(15.dp))
            Card(
                shape = CircleShape,
                modifier = Modifier.size(53.dp),
                elevation = 7.dp,
                backgroundColor = Color.White
            ) {
                profilePhoto?.let {
                    AsyncImage(model = it, contentDescription = "")
                } ?: kotlin.run {
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle_icon),
                        contentDescription = "",
                        tint = LightGray
                    )
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = displayName,
                    color = Color.White,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = mail,
                    color = LightGray,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen(viewModel = object : MviPreviewViewModel<AccountUiSate, AccountSingleEvent>(),
        IAccountViewModel {
        override fun stateForPreview(): StateFlow<AccountUiSate> {
            return MutableStateFlow(AccountUiSate("Mariusz Kowalski", "kowalski@onet.pl"))
        }

        override fun signOut() {}
        override fun myEventsClick() {}
        override fun adminPanelClick() {}
    })

}