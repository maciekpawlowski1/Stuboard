package com.pawlowski.stuboard.ui.register_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.components.MySwitch
import com.pawlowski.stuboard.ui.components.SwitchButtonType
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
internal fun ProgressBarWithLabel(
    modifier: Modifier = Modifier,
    labelTextValue: () -> String,
    progress: () -> Float
) {
    Text(
        text = labelTextValue.invoke(),
        fontFamily = montserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth(),
        color = Green,
        progress = animateFloatAsState(targetValue = progress.invoke()).value
    )
}

@Composable
internal fun RegisterScreenHeader(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    backIconHorizontalPadding: Dp
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth()) {
        Icon(
            modifier = Modifier
                .clickable { onNavigateBack.invoke() }
                .padding(horizontal = backIconHorizontalPadding)
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
}

@Composable
internal fun RegisterLottieAnimation(modifier: Modifier = Modifier) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))
    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(modifier = modifier
        .height(193.dp),
        composition = lottieComposition,
        progress = { progress })
}

@Composable
internal fun AccountTypeSwitch(
    modifier: Modifier = Modifier,
    selectedAccountType: AccountType,
    onAccountTypeSelected: (AccountType) -> Unit = {}
) {
    MySwitch(
        modifier = modifier,
        selectedButton = if (selectedAccountType == AccountType.NORMAL)
            SwitchButtonType.FIRST
        else
            SwitchButtonType.SECOND,
        firstButtonLabel = "Zwyk??e konto",
        secondButtonLabel ="Organizacja studencka",
        onButtonSelected = {
            if(it == SwitchButtonType.FIRST)
                onAccountTypeSelected(AccountType.NORMAL)
            else
                onAccountTypeSelected(AccountType.STUDENT_ORGANISATION)
        }
    )
}
