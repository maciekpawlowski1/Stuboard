package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun RegisterScreen1(onNavigateBack: () -> Unit = {})
{
    val selectedAccountType = remember {
        mutableStateOf(AccountType.NORMAL)
    }

    val horizontalPadding = 10.dp
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier
                    .clickable { onNavigateBack.invoke() }
                    .padding(horizontal = horizontalPadding - 5.dp)
                    .padding(top = 3.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.arrow_back_icon),
                contentDescription = ""
            )
            Text(
                text = "Rejestracja",
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
        }
        Text(
            text = "1 of 3",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        LinearProgressIndicator(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
            color = Green,
            progress = 0.33f
        )

        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))
        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(modifier = Modifier
            .padding(top = 15.dp)
            .height(193.dp),
            composition = lottieComposition, 
            progress = { progress })

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Wybierz typ konta",
            fontFamily = montserratFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))
        AccountTypeSwitch(
            modifier = Modifier
                .padding(horizontal = horizontalPadding), 
            selectedAccountType = selectedAccountType.value,
            onAccountTypeSelected = { selectedAccountType.value = it }
        )
        
        Spacer(modifier = Modifier.height(15.dp))
        
        Text(
            modifier = Modifier.padding(horizontal = horizontalPadding),
            text = "* Konta o typie Organizacja Studencka wymagają  dodatkowej weryfikacji",
            fontFamily = montserratFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Dalej", color = Color.White)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}

enum class AccountType(val accountTypeId: Int) {
    NORMAL(0),
    STUDENT_ORGANISATION(1)
}

@Composable
fun AccountTypeSwitch(modifier: Modifier = Modifier, selectedAccountType: AccountType, onAccountTypeSelected: (AccountType) -> Unit = {})
{
    Card(modifier = modifier
        .height(36.dp)
        .fillMaxWidth()) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if(selectedAccountType == AccountType.NORMAL)
                            Green
                        else
                            Color.White
                    )
                    .clickable { onAccountTypeSelected.invoke(AccountType.NORMAL) },
                contentAlignment = Alignment.Center)
            {
                if(selectedAccountType == AccountType.NORMAL)
                {
                    SelectedText(text = "Zwykłe konto")
                }
                else
                {
                    NotSelectedText(text = "Zwykłe konto")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if(selectedAccountType == AccountType.NORMAL)
                            Color.White
                        else
                            Green
                    )
                    .clickable { onAccountTypeSelected.invoke(AccountType.STUDENT_ORGANISATION) },
                contentAlignment = Alignment.Center)
            {
                if(selectedAccountType == AccountType.STUDENT_ORGANISATION)
                {
                    SelectedText(text = "Organizacja studencka")
                }
                else
                {
                    NotSelectedText(text = "Organizacja studencka")
                }
            }
        }
    }
}

@Composable
private fun SelectedText(text: String)
{
    Text(
        text = text,
        color = Color.White,
        fontFamily = montserratFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    )
}

@Composable
private fun NotSelectedText(text: String)
{
    Text(
        text = text,
        color = MidGrey,
        fontFamily = montserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )}

@Preview(showBackground = true)
@Composable
fun RegisterScreen1Preview()
{
    RegisterScreen1()
}