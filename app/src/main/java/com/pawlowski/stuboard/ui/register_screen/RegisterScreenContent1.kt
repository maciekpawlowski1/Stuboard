package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
internal fun RegisterScreenContent1(
    horizontalPadding: Dp,
    selectedAccountType: AccountType,
    onAccountTypeSelected: (AccountType) -> Unit,
    onNextClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            selectedAccountType = selectedAccountType,
            onAccountTypeSelected = { onAccountTypeSelected.invoke(it) }
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
            onClick = { onNextClick.invoke() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)
        ) {
            Text(text = "Dalej", color = Color.White)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

}