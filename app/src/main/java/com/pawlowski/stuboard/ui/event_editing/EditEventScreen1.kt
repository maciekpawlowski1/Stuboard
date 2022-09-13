package com.pawlowski.stuboard.ui.event_editing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LighterMidGrey
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditEventScreen1(
    tittleInput: () -> String = { "" },
    onTittleInputChange: (String) -> Unit = {},
    sinceTime: Long? = null,
    toTime: Long? = null,
    onSinceTimeChange: (Long) -> Unit = {},
    onToTimeChange: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val isImageSelected = true
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val screenWidth = LocalConfiguration.current.screenWidthDp



        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        val bitmap =  remember {
            mutableStateOf<Bitmap?>(null)
        }

        val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the cropped image
                imageUri = result.uriContent
                bitmap.value = getBitmapFromUri(imageUri, context)
            } else {
                // an error occurred cropping
                val exception = result.error
            }
        }
        val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cropOptions = options(uri = uri) {
                setAspectRatio(3, 2)
            }
            imageCropLauncher.launch(cropOptions)
        }


        Box {
            if (isImageSelected) {
                AsyncImage(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth()
                        .height((screenWidth / 1.5).dp),
                    model = bitmap.value,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Surface(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth()
                        .height((screenWidth / 1.5).dp), color = LighterMidGrey
                ) {

                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp), onClick = {
                    imagePickerLauncher.launch("image/*")
                                                     }, backgroundColor = Green
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.image_icon),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = tittleInput(),
            onValueChange = onTittleInputChange,
            colors = textFieldColors,
            label = { Text(text = "Tytuł wydarzenia (max. 35 znaków)") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Od:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(10.dp))



            ClickToPickDateCard(
                modifier = Modifier
                    .clickable {
                        MaterialDialog(context).show {
                            dateTimePicker(show24HoursView = true, requireFutureDateTime = true) { _, datetime ->
                                onSinceTimeChange(datetime.time.time)
                            }
                        }
                    },
                timeInput = sinceTime
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Do:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            ClickToPickDateCard(
                modifier = Modifier
                    .clickable {
                        MaterialDialog(context).show {
                            dateTimePicker(requireFutureDateTime = true) { _, datetime ->
                                onToTimeChange(datetime.time.time)
                            }
                        }
                    },
                timeInput = toTime
            )
        }

    }
}

fun getBitmapFromUri(imageUri: Uri?, context: Context): Bitmap?
{
    return imageUri?.let { imageUri ->
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}

@Composable
fun ClickToPickDateCard(modifier: Modifier = Modifier, timeInput: Long? = null) {
    Card(
        elevation = 10.dp, modifier = modifier
            .width(195.dp)
            .height(48.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            val simpleDateFormat = remember {
                SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            }
            val timeInputString = remember(timeInput) {
                if (timeInput != null) {
                    simpleDateFormat.format(Date(timeInput))
                } else
                    "Kliknij aby wybrać datę"
            }
            Text(
                text = timeInputString,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Icon(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = "",
                    tint = MidGrey
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditEventScreen1Preview() {
    EditEventScreen1()
}