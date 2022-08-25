package com.pawlowski.stuboard.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.R


val montserratFont = FontFamily(
    //Font(R.font.montserrat_black),
    Font(R.font.montserrat_blackitalic, style = FontStyle.Italic),
    Font(R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(R.font.montserrat_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.montserrat_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.montserrat_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.montserrat_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.montserrat_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
    Font(R.font.montserrat_light, weight = FontWeight.Light),
    Font(R.font.montserrat_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.montserrat_medium, weight = FontWeight.Medium),
    Font(R.font.montserrat_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.montserrat_regular, weight = FontWeight.Normal),
    Font(R.font.montserrat_semibold, weight = FontWeight.SemiBold),
    Font(R.font.montserrat_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.montserrat_thin, weight = FontWeight.Thin),
    Font(R.font.montserrat_thin, weight = FontWeight.Thin, style = FontStyle.Italic),
)

val jostFontNormalWeight = FontFamily(
    Font(R.font.jost_regular, style = FontStyle.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = montserratFont,
    ),
    h2 = TextStyle(
        fontFamily = montserratFont
    ),
    h3 = TextStyle(
        fontFamily = montserratFont
    ),
    h4 = TextStyle(
        fontFamily = montserratFont
    ),
    h5 = TextStyle(
        fontFamily = montserratFont
    ),
    h6 = TextStyle(
        fontFamily = montserratFont
    ),
    subtitle1 = TextStyle(
        fontFamily = montserratFont
    ),
    subtitle2 = TextStyle(
        fontFamily = montserratFont
    ),


    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)