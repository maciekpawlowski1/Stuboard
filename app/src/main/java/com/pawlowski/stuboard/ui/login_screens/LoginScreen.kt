package com.pawlowski.stuboard.ui.login_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun LoginScreen() {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sign_in_animation))
        val progress by animateLottieCompositionAsState(
            lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
        LottieAnimation(modifier = Modifier
            .padding(top = 10.dp)
            .height(200.dp), composition = lottieComposition, progress = { progress })

        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp),
            text = "Witaj!",
            fontFamily = montserratFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            text = "Zacznijmy, zaloguj się!",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .align(CenterHorizontally),
            value = "",
            label = { Text(text = "Adres e-mail") },
            onValueChange = {},
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors
        )


        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .align(CenterHorizontally),
            value = "",
            label = { Text(text = "Hasło") },
            onValueChange = {},
            leadingIcon =
            {
                Icon(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = ""
                )
            },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        
        
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { /*TODO*/ }, 
            colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
            Text(text = "Zaloguj się", color = Color.White)
        }

        HorizontalDividerWithLabelInTheMiddle()
        Spacer(modifier = Modifier.height(10.dp))
        LogInByGoogleButton()
        Spacer(modifier = Modifier.height(10.dp))
        Text(modifier = Modifier.align(CenterHorizontally).clickable {  },
            fontFamily = montserratFont,
            text = buildAnnotatedString {
            append("Nie masz konta? ")
            withStyle(style = SpanStyle(color = Green, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.SemiBold))
            {
                append("Zarejestruj się")
            }
        })
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDividerWithLabelInTheMiddle()
        Spacer(modifier = Modifier.height(10.dp))
        ContinueAnonymousButton()
        Spacer(modifier = Modifier.height(10.dp))

    }

}

@Composable
fun HorizontalDividerWithLabelInTheMiddle()
{
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = "Lub",
            color = MidGrey,
            fontSize = 13.sp,
            fontFamily = montserratFont
        )
        Divider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LogInByGoogleButton()
{
    OutlinedButton(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(36.dp)
            .fillMaxWidth(),
        onClick = { /*TODO*/ }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier= Modifier.align(CenterStart),
                painter = painterResource(id = R.drawable.google_icon), contentDescription = "")
            Text(modifier= Modifier.align(Center),text = "Zaloguj się przez Google", color = Green)
        }

    }
}

@Composable
fun ContinueAnonymousButton()
{
    OutlinedButton(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(36.dp)
            .fillMaxWidth(),
        onClick = { /*TODO*/ }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier= Modifier.align(CenterStart),
                painter = painterResource(id = R.drawable.incognito_icon), contentDescription = "")
            Text(modifier= Modifier.align(Center),text = "Zacznij bez logowania", color = Green)
        }

    }
}